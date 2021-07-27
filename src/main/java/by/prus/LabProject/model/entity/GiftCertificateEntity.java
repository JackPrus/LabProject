package by.prus.LabProject.model.entity;


import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
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
    private Date createDate;

    @Temporal(TemporalType.DATE)
    @Column (nullable = false)
    private Date lastUpdateDate;

//    @OneToMany(mappedBy = "tag", fetch = FetchType.LAZY)
//    Set<Tag> tags;

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

}
