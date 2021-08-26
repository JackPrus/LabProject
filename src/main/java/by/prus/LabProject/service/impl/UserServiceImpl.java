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

import java.util.*;

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

    @Override
    public UserDto createUser(UserDto userDto) {

        UserEntity storedUserDetails = userRepository.findByEmail(userDto.getEmail());
        if (storedUserDetails!=null){ throw new UserServiceException("record already exists"); }

        ModelMapper modelMapper = new ModelMapper();
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
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(userEntity, UserDto.class);
    }

    @Override
    public UserDto getUserByUserId(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null){ throw new UsernameNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()); }
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(userEntity, UserDto.class);
    }

    //update set of certificates only
    @Override
    public UserDto updateUser(String userId, UserDto user) {
        UserEntity userEntity = userRepository.findByUserId(userId);
        ModelMapper modelMapper = new ModelMapper();
        Set<GiftCertificateEntity> certificateEntitySet = new HashSet<>();

        if (userEntity == null){ throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()); }

        for (GiftCertificateDTO certificate : user.getCertificates()){
            certificateEntitySet.add(modelMapper.map(certificate, GiftCertificateEntity.class));
        }
        userEntity.setCertificatesOfUser(certificateEntitySet);
        UserEntity updatedUserDetails = userRepository.save(userEntity);

        return modelMapper.map(updatedUserDetails, UserDto.class);
    }

    @Override
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
        ModelMapper modelMapper = new ModelMapper();

        for (UserEntity userEntity : users) {
            returnValue.add(modelMapper.map(userEntity, UserDto.class));
        }
        return returnValue;
    }

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

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null){ throw new UsernameNotFoundException(email); }
        return new UserPrincipal(userEntity);
    }
}
