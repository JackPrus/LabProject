package by.prus.LabProject.security;

import by.prus.LabProject.model.entity.AuthorityEntity;
import by.prus.LabProject.model.entity.RoleEntity;
import by.prus.LabProject.model.entity.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;

/**
 * The object returns by Spring Security when call 'loadUserByUsername(String email)' method.
 */
public class UserPrincipal implements UserDetails {

    private static final long serialVersionUID = 587972456817255L;

    private UserEntity userEntity;
    private String userId;

    public UserPrincipal(UserEntity userEntity) {
        this.userEntity = userEntity;
        this.userId = userEntity.getUserId();
    }


    //GrantedAuthority обджект будет создан используя имя каждой юзер-роли и каждой юзер-аутхорити
    // то есть там нужно создать лист имеющий список каждой юзер роли и юзер аутхорити
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> authorities = new HashSet<>();
        Collection<AuthorityEntity> authorityEntities = new HashSet<>();


        //get User Roles
        Collection<RoleEntity> roles = userEntity.getRoles();
        if (roles == null) return authorities;

        roles.forEach((role)->{
            authorities.add(new SimpleGrantedAuthority(role.getName()));
            authorityEntities.addAll(role.getAuthorities());
        });

        authorityEntities.forEach((authorityEntity -> {
            authorities.add(new SimpleGrantedAuthority(authorityEntity.getName()));
        }));

        return authorities;
    }

    @Override
    public String getPassword() { return this.userEntity.getEncryptedPassword();}
    @Override
    public String getUsername() { return this.userEntity.getEmail();}
    @Override
    public boolean isAccountNonExpired() { return true;}
    @Override
    public boolean isAccountNonLocked() { return true;}
    @Override
    public boolean isCredentialsNonExpired() { return true;}
    @Override
    public boolean isEnabled() { return this.userEntity.getEmailVerificationStatus();}
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId;}

}
