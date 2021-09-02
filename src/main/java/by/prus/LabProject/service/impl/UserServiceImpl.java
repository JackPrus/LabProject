package by.prus.LabProject.service.impl;

import by.prus.LabProject.exception.UserServiceException;
import by.prus.LabProject.model.dto.GiftCertificateDTO;
import by.prus.LabProject.model.dto.UserDto;
import by.prus.LabProject.model.entity.*;
import by.prus.LabProject.model.response.ErrorMessages;
import by.prus.LabProject.repository.PasswordResetTokenRepository;
import by.prus.LabProject.repository.RoleRepository;
import by.prus.LabProject.repository.UserRepository;
import by.prus.LabProject.security.UserPrincipal;
import by.prus.LabProject.service.UserService;
import by.prus.LabProject.service.Utils;
import by.prus.LabProject.service.mail.MailSender;
import by.prus.LabProject.service.mail.YandexSES;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

/**
 * The class implementing CRUD operations with User on service layer.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    Utils utils;
    @Autowired
    PasswordResetTokenRepository passwordResetTokenRepository;
    @Autowired
    MailSender mailSender;
    @Autowired
    YandexSES yandexSES;
    @Autowired
    ModelMapper modelMapper;

    /**
     * The method creating a user. When user is created the method yandexSES.verifyEmail
     * send a message with link including email-verification token to email pointed by user as login.
     * When user pass the link with token the UserController method 'verifyEmailToken' take it and
     * verification status of user becames true. Since when that user can log in to system.
     * @param userDto - the object including information about user need to cerate.
     * @return - Created user
     */
    @Override
    @Transactional
    public UserDto createUser(UserDto userDto) {

        UserEntity storedUserDetails = userRepository.findByEmail(userDto.getEmail());
        if (storedUserDetails!=null){ throw new UserServiceException("record already exists"); }

        UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);
        String publishedUserId = utils.generateUserId(5);

        //установка ролей для пользователя
        Collection<RoleEntity> roleEntities = new HashSet<>();
        for (String role : userDto.getRoles()){
            RoleEntity roleEntity = roleRepository.findByName(role);
            if (roleEntity!= null){
                roleEntities.add(roleEntity);
            }
        }
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        userEntity.setUserId(publishedUserId);
        userEntity.setEmailVerificationToken(utils.generateEmailVerificationToken(publishedUserId));
        userEntity.setEmailVerificationStatus(false);
        userEntity.setRoles(roleEntities);

        storedUserDetails = userRepository.save(userEntity);
        UserDto returnValue = modelMapper.map(storedUserDetails, UserDto.class);

        //Send an email message to verify their email address
        yandexSES.verifyEmail(returnValue);

        return returnValue;
    }

    @Override
    public UserDto getUser(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null){ throw new UsernameNotFoundException(email); }
        return modelMapper.map(userEntity, UserDto.class);
    }

    @Override
    public UserDto getUserByUserId(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null){ throw new UsernameNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()); }
        return modelMapper.map(userEntity, UserDto.class);
    }

    //update set of certificates only
    @Override
    @Transactional
    public UserDto updateUser(String userId, UserDto user) {
        UserEntity userEntity = userRepository.findByUserId(userId);
        Set<GiftCertificateEntity> certificateEntityList = new HashSet<>();

        if (userEntity == null){ throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()); }

        for (GiftCertificateDTO certificate : user.getCertificates()){
            certificateEntityList.add(modelMapper.map(certificate, GiftCertificateEntity.class));
        }
        userEntity.setCertificatesOfUser(certificateEntityList);
        UserEntity updatedUserDetails = userRepository.save(userEntity);

        return modelMapper.map(updatedUserDetails, UserDto.class);
    }

    @Override
    @Transactional
    public void deleteUser(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null){throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());}
        userRepository.delete(userEntity);
    }

    @Override
    public List<UserDto> getUsers(int page, int limit) {
        List<UserDto> returnValue = new ArrayList<>();
        if(page>0) { page = page-1;}
        Pageable pageableRequest = PageRequest.of(page, limit); // объект который мы засунем в репозиторий, чтобы достать результат по страницам
        Page<UserEntity> userPage = userRepository.findAllAndSortByEmail(pageableRequest); // находит нужную страницу юзеров
        List<UserEntity> users = userPage.getContent();

        for (UserEntity userEntity : users) {
            returnValue.add(modelMapper.map(userEntity, UserDto.class));
        }
        return returnValue;
    }

    /**
     * The method checking if token is valued for current user.
     * The link validity is 1 day only. If user after geting email with token-link pass
     * forward with link after 1 day the verification will return false and user vill not be verified.
     * @param token - the token from email-link that user received
     * @return - result of verification of user
     */
    @Override
    public boolean verifyEmailToken(String token) {

        boolean returnValue = false;
        //Find user by token
        UserEntity userEntity = userRepository.findByEmailVerificationToken(token);

        if (userEntity!=null){
            boolean hasTokenExpired = Utils.hasTokenExpired(token);
            if (!hasTokenExpired){
                userEntity.setEmailVerificationToken(null);
                userEntity.setEmailVerificationStatus(Boolean.TRUE);
                userRepository.save(userEntity);
                returnValue = true;
            }
        }

        return returnValue;
    }

    /**
     * The method send link with token to email when user deside to reset password.
     * @param email - email of user
     * @return - the rusult of sending email.
     */
    @Override
    public boolean requestPasswordReset(String email) {
        boolean returnValue = false;
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity==null){return  returnValue;}
        String token = new Utils().generatePasswordResetToken(userEntity.getUserId());

        PasswordResetTokenEntity passwordResetTokenEntity = new PasswordResetTokenEntity();
        passwordResetTokenEntity.setToken(token);
        passwordResetTokenEntity.setUserDetails(userEntity);
        passwordResetTokenRepository.save(passwordResetTokenEntity);

//        returnValue = amazonSes.sendPasswordResetRequest(
//                userEntity.getFirstName(),
//                userEntity.getEmail(),
//                token);
        returnValue = yandexSES.sendPasswordResetRequest(userEntity.getEmail(), token);

        return returnValue;
    }

    /**
     * When the user deside to reset pasword with regards to method abover he receive the link
     * with token to email adress. Passink to this link the user will be captured by html page
     * that contain 2 fields for entrance of new password. After entrance and submitting JavaScript
     * code check and validate information and send reauest to  'resetPassword' method of UserController
     * class. with PasswordResetModel object having new password and token for cecurity.
     * This method set new encryptedPassword and update current user with changed password.
     * @param token - the confirmation token transered from email message and html page.
     * @param password - new password from PasswordResetModel
     * @return - the result of user update.
     */
    @Override
    public boolean resetPassword(String token, String password) {
        boolean returnValue = false;

        if( Utils.hasTokenExpired(token) ) {
            return returnValue;
        }

        PasswordResetTokenEntity passwordResetTokenEntity = passwordResetTokenRepository.findByToken(token);

        if (passwordResetTokenEntity == null) {
            return returnValue;
        }

        String encodedPassword = bCryptPasswordEncoder.encode(password);

        // Update User password in database
        UserEntity userEntity = passwordResetTokenEntity.getUserDetails();
        userEntity.setEncryptedPassword(encodedPassword);
        UserEntity savedUserEntity = userRepository.save(userEntity);

        // Verify if password was saved successfully
        if (savedUserEntity != null && savedUserEntity.getEncryptedPassword().equalsIgnoreCase(encodedPassword)) {
            returnValue = true;
        }
        // удаляем пассворд ресет токен из базы данных
        passwordResetTokenRepository.delete(passwordResetTokenEntity);

        return returnValue;
    }

    /**
     * The method needed for Spring-Security.
     * @param email - email of user
     * @return -
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null){ throw new UsernameNotFoundException(email); }
        return new UserPrincipal(userEntity);
    }
}
