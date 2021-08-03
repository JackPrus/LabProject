package by.prus.LabProject.model.entity;

import by.prus.LabProject.model.entity.supporting.CertificateTag;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tag")
public class TagEntity implements Serializable {

    private static final long serialVerisonUID = 2342353453245L;

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private String name;

    //cascade = {CascadeType.MERGE, CascadeType.PERSIST}
    @JsonIgnore
    @OneToMany(mappedBy = "tag", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.EAGER)
    private Set<CertificateTag> certificateTags;

    /*
    @JsonIgnore // без этой аннотации будет зацикливание
    @ManyToMany(mappedBy = "tagSet",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    Set<GiftCertificateEntity> certificatesSet = new HashSet<>();

 */

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Set<CertificateTag> getCertificateTags() { return certificateTags; }
    public void setCertificateTags(Set<CertificateTag> certificateTags) { this.certificateTags = certificateTags; }

}
