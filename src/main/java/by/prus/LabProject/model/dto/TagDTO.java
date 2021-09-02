package by.prus.LabProject.model.dto;

import by.prus.LabProject.model.entity.GiftCertificateEntity;
import by.prus.LabProject.model.entity.supporting.CertificateTag;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode
public class TagDTO {

    private static final long serialVersionUID = 2344432456723452346L;

    private long id;
    private String name;
//    private Set<GiftCertificateEntity> certificatesSet;
    private List<CertificateTag> certificateTags;

    public long getId() { return id; }

    public void setId(long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
//    public Set<GiftCertificateEntity> getCertificatesSet() { return certificatesSet; }
//    public void setCertificatesSet(Set<GiftCertificateEntity> certificatesSet) { this.certificatesSet = certificatesSet; }

    public List<CertificateTag> getCertificateTags() { return certificateTags; }
    public void setCertificateTags(List<CertificateTag> certificateTags) { this.certificateTags = certificateTags; }
}
