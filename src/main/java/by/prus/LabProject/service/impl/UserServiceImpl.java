package by.prus.LabProject.service.impl;

import by.prus.LabProject.exception.UserServiceException;
import by.prus.LabProject.model.Role;
import by.prus.LabProject.model.dto.GiftCertificateDTO;
import by.prus.LabProject.model.dto.UserDto;
import by.prus.LabProject.model.entity.RoleEntity;
import by.prus.LabProject.model.entity.UserEntity;
import by.prus.LabProject.repository.RoleRepository;
import by.prus.LabProject.repository.UserRepository;
import by.prus.LabProject.security.UserPrincipal;
import by.prus.LabProject.service.UserService;
import by.prus.LabProject.service.Utils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

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
        //amazonSes.verifyEmail(returnValue);

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
        return null;
    }

    @Override
    public UserDto updateUser(String userId, UserDto user) {
        return null;
    }

    @Override
    public void deleteUser(String userId) {

    }

    @Override
    public List<UserDto> getUsers(int page, int limit) {
        return null;
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
        return false;
    }

    @Override
    public boolean resetPassword(String token, String password) {
        return false;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null){ throw new UsernameNotFoundException(email); }

        return new UserPrincipal(userEntity);


//        String login = userEntity.getEmail();
//        String password = userEntity.getEncryptedPassword();
//        boolean verfifcationStatus = userEntity.getEmailVerificationStatus();
//
//        return new User(
//                email, password, verfifcationStatus,
//                true,true, true,
//                new ArrayList<>());
//
//        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());

    }
}
