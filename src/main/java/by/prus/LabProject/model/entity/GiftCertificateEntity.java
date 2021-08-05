package by.prus.LabProject.model.entity;


import by.prus.LabProject.model.entity.supporting.CertificateTag;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.aspectj.apache.bcel.generic.Tag;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "gift_certificate")
public class GiftCertificateEntity implements Serializable {

    private static final long serialVerisonUID = 2342353453245L;

    @Id
    @GeneratedValue
    private long id;

    @Column (nullable = false, length = 50)
    private String name;

    @Column (nullable = false, length = 150)
    private String description;

    @Digits(integer=5, fraction=2) //means quantity in front and after coma
    @Column (nullable = false)
    private BigDecimal price;

    @Min(0)
    @Column (nullable = false)
    private int duration;

    @Temporal(TemporalType.DATE) // check @Temporal , .DATE means date only. Timestamp means - yy.mm.dd.hh.mm.
    @Column (nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createDate;

    @Temporal(TemporalType.DATE)
    @Column (nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date lastUpdateDate;

    //cascade = {CascadeType.MERGE, CascadeType.PERSIST}

    @JsonBackReference
    @OneToMany(mappedBy = "giftCertificate", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    private Set<CertificateTag> certificateTags;

    /*
    @ManyToMany
    @JoinTable(
                name = "certificate_tag",
            joinColumns = @JoinColumn (name = "gift_certificate_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
            )
    Set<TagEntity> tagSet = new HashSet<>();

    Many to many examples
    https://github.com/kriscfoster/Spring-Data-JPA-Relationships/blob/c8d46448d5f433ca783db64694c215824995bc52/src/main/java/com/kriscfoster/school/student/Student.java#L11
    https://github.com/bartoszkomin/hibernate-many-to-many-demo/blob/master/src/main/java/com/blogspot/bartoszkomin/hibernate_many_to_many_demo/model/User.java
 */

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }
    public Date getCreateDate() { return createDate; }
    public void setCreateDate(Date createDate) { this.createDate = createDate; }
    public Date getLastUpdateDate() { return lastUpdateDate; }
    public void setLastUpdateDate(Date lastUpdateDate) { this.lastUpdateDate = lastUpdateDate; }

    public Set<CertificateTag> getCertificateTags() { return certificateTags; }
    public void setCertificateTags(Set<CertificateTag> certificateTags) { this.certificateTags = certificateTags; }
}
