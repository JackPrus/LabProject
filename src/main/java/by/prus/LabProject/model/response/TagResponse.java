package by.prus.LabProject.model.response;

import by.prus.LabProject.model.entity.GiftCertificateEntity;
import by.prus.LabProject.model.entity.supporting.CertificateTag;

import java.util.Set;

public class TagResponse {

    private static final long serialVersionUID = 2344432456723452346L;

    private long id;
    private String name;
//    private Set<GiftCertificateEntity> certificatesSet;
    private Set<CertificateTag> certificateTags;

    public long getId() { return id; }

    public void setId(long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Set<CertificateTag> getCertificateTags() { return certificateTags; }
    public void setCertificateTags(Set<CertificateTag> certificateTags) { this.certificateTags = certificateTags; }

    //    public Set<GiftCertificateEntity> getCertificatesSet() { return certificatesSet; }
//    public void setCertificatesSet(Set<GiftCertificateEntity> certificatesSet) { this.certificatesSet = certificatesSet; }
}
