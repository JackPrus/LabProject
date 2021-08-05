package by.prus.LabProject.model.response;

import by.prus.LabProject.model.entity.TagEntity;
import by.prus.LabProject.model.entity.supporting.CertificateTag;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

public class GiftCertificateResponse implements Serializable {

    private static final long serialVersionUID = 2345356234623452346L;

    private long id;
    private String name;
    private String description;
    private BigDecimal price;
    private int duration;
    private Date createDate;
    private Date lastUpdateDate;
    private Set<CertificateTag> certificateTags;

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
