package by.prus.LabProject.model.entity;

import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "password_reset_token")
@EqualsAndHashCode
public class PasswordResetTokenEntity implements Serializable {

    private static final long serialVersionUID = 5879785695135817255L;
    @Id
    @GeneratedValue
    private long id;
    private String token;
    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity userDetails;

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public UserEntity getUserDetails() { return userDetails; }
    public void setUserDetails(UserEntity userDetails) { this.userDetails = userDetails; }
}
