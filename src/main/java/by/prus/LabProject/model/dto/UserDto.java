package by.prus.LabProject.model.dto;

import java.util.Collection;
import java.util.Set;

public class UserDto {

    private static final long serialVersionUID = 2345356456723452346L;

    private long id;
    private String userId;
    private String email;
    private String password;
    private String encryptedPassword;
    private String emailVerificationToken;
    private Boolean emailVerificationStatus = false;
    private Set<GiftCertificateDTO> certificates;
    private Collection<String> roles;

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getEncryptedPassword() { return encryptedPassword; }
    public void setEncryptedPassword(String encryptedPassword) { this.encryptedPassword = encryptedPassword; }
    public String getEmailVerificationToken() { return emailVerificationToken; }
    public void setEmailVerificationToken(String emailVerificationToken) { this.emailVerificationToken = emailVerificationToken; }
    public Boolean getEmailVerificationStatus() { return emailVerificationStatus; }
    public void setEmailVerificationStatus(Boolean evailVerificationStatus) { this.emailVerificationStatus = evailVerificationStatus; }
    public Collection<String> getRoles() { return roles; }
    public void setRoles(Collection<String> roles) { this.roles = roles; }
    public Set<GiftCertificateDTO> getCertificates() { return certificates; }
    public void setCertificates(Set<GiftCertificateDTO> certificates) { this.certificates = certificates; }
}
