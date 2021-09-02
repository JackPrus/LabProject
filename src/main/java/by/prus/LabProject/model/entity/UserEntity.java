package by.prus.LabProject.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "user")
@EqualsAndHashCode(exclude = {"id","rolesOfUser","certificatesOfUser"})
public class UserEntity implements Serializable {

    private static final long serialVerisonUID = 2342353453245L;

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false, unique = true)
    private String userId;

    @Column(nullable = false, length = 120, unique = true)
    private String email;

    @Column(nullable = false)
    private String encryptedPassword;

    private String emailVerificationToken;

    @Column(nullable = false)
    private Boolean emailVerificationStatus = false;


    //Persist - если Юзер будет удален, нам не нужно удалять роль, т.к. роль принадлежит разным юзерам.
    // Eager - когда данные юзера будут считаны из базы данных, роли будут загружены сразу же. Это нужно для аутентификации
    @ManyToMany(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JsonManagedReference
    @JoinTable(
            name ="user_role",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name ="role_id", referencedColumnName = "id")
    )
    private Collection<RoleEntity> rolesOfUser;

    // Если тип List и FetchType.Eager - будет MultipleBagFetchException. Если сменить на Set, или поставить Lazy - не будет. Выяснить почему.
    // не ставить Set чтобы избежать проблемы. Т.к. по факту мы от нее не избавляемся а скрываем.
    // https://stackoverflow.com/questions/4334970/hibernate-throws-multiplebagfetchexception-cannot-simultaneously-fetch-multipl/51055523?stw=2#51055523
    @ManyToMany(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JsonManagedReference
    @JoinTable(
            name ="user_gift_certificate",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name ="gift_certificate_id", referencedColumnName = "id")
    )
    private Set<GiftCertificateEntity> certificatesOfUser;

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getEncryptedPassword() { return encryptedPassword; }
    public void setEncryptedPassword(String encryptedPassword) { this.encryptedPassword = encryptedPassword; }
    public String getEmailVerificationToken() { return emailVerificationToken; }
    public void setEmailVerificationToken(String emailVerificationToken) { this.emailVerificationToken = emailVerificationToken; }
    public Boolean getEmailVerificationStatus() { return emailVerificationStatus; }
    public void setEmailVerificationStatus(Boolean emailVerificationStatus) { this.emailVerificationStatus = emailVerificationStatus; }
    public Collection<RoleEntity> getRoles() { return rolesOfUser; }
    public void setRoles(Collection<RoleEntity> roles) { this.rolesOfUser = roles; }
    public Set<GiftCertificateEntity> getCertificatesOfUser() { return certificatesOfUser; }
    public void setCertificatesOfUser(Set<GiftCertificateEntity> certificatesOfUser) { this.certificatesOfUser = certificatesOfUser; }
}
