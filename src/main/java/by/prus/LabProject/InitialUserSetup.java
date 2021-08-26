package by.prus.LabProject;

import by.prus.LabProject.model.Authority;
import by.prus.LabProject.model.Role;
import by.prus.LabProject.model.entity.AuthorityEntity;
import by.prus.LabProject.model.entity.RoleEntity;
import by.prus.LabProject.model.entity.UserEntity;
import by.prus.LabProject.repository.AuthorityRepository;
import by.prus.LabProject.repository.RoleRepository;
import by.prus.LabProject.repository.UserRepository;
import by.prus.LabProject.service.UserService;
import by.prus.LabProject.service.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Component
public class InitialUserSetup {

    @Autowired
    AuthorityRepository authorityRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    Utils utils;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    UserRepository userRepository;

    @EventListener
    @Transactional
    public void onApplicationEvent(ApplicationReadyEvent event){
        System.out.println("Form application ready event");

        AuthorityEntity readAuthority = createAuthority(Authority.READ_AUTHORITY.name());
        AuthorityEntity writeAuthority = createAuthority(Authority.WRITE_AUTHORITY.name());
        AuthorityEntity deleteAuthority = createAuthority(Authority.DELETE_AUTHORITY.name());

        RoleEntity roleUser = createRole(Role.ROLE_USER.name(), Arrays.asList(readAuthority,writeAuthority));
        RoleEntity roleAdmin = createRole(Role.ROLE_ADMIN.name(), Arrays.asList(readAuthority,writeAuthority,deleteAuthority));

        if (roleAdmin==null)return;

        UserEntity adminUser = new UserEntity();
        adminUser.setEmail("qwer@qwer.com");
        adminUser.setEmailVerificationStatus(true);
        adminUser.setUserId(utils.generateUserId(5));
        adminUser.setEncryptedPassword(bCryptPasswordEncoder.encode("1234"));
        adminUser.setRoles(Arrays.asList(roleAdmin));

        UserEntity storedUserDetails = userRepository.findByEmail("qwer@qwer.com");

        if (storedUserDetails == null) {
            userRepository.save(adminUser);
        }


        //check if users already exitsts. And if not - create 5 users.
        if (!areUsersCreated()){
            for (int i=0; i<5; i++){
                UserEntity userEntity = new UserEntity();
                userEntity.setEmail(utils.generateUserId(4)+"@"+utils.generateUserId(4)+".com");
                userEntity.setUserId(utils.generateUserId(4));
                userEntity.setEncryptedPassword("$2a$12$f.C8ml9gg1cmV2b.m9lskO9OqgyiImTPhvwTiLGxQNdAvX1ljssrC"); //1234
                userEntity.setRoles(Arrays.asList(roleUser));
                userEntity.setEmailVerificationStatus(true);

                UserEntity storedUser = userRepository.findByEmail(userEntity.getEmail());
                if (storedUser==null){userRepository.save(userEntity);}

            }
        }

    }

    //generate 5 users if ve have less than 5 in our database
    private boolean areUsersCreated () {
        Pageable pageableRequest = PageRequest.of(0, 5); // объект который мы засунем в репозиторий, чтобы достать результат по страницам
        Page<UserEntity> userPage = userRepository.findAllAndSortByEmail(pageableRequest); // находит нужную страницу юзеров
        List<UserEntity> users = userPage.getContent();
        return users.size()>=5;
    }

    @Transactional
    private AuthorityEntity createAuthority (String name){
        AuthorityEntity authority = authorityRepository.findByName(name);
        if (authority == null){
            authority = new AuthorityEntity(name);
            authorityRepository.save(authority);
        }
        return authority;
    }

    @Transactional
    private RoleEntity createRole(String name, Collection<AuthorityEntity> authorities){
        RoleEntity role = roleRepository.findByName(name);
        if (role == null){
            role = new RoleEntity(name);
            role.setAuthorities(authorities);
            roleRepository.save(role);
        }
        return role;
    }

}
