package by.prus.LabProject.model.response;

import by.prus.LabProject.model.dto.GiftCertificateDTO;

import java.util.List;
import java.util.Set;

public class UserResponse {

    private String userId;
    private String email;
    private Set<GiftCertificateResponse> certificates;

    public Set<GiftCertificateResponse> getCertificates() { return certificates; }
    public void setCertificates(Set<GiftCertificateResponse> certificates) { this.certificates = certificates; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

}
