package by.prus.LabProject.model.response;

import by.prus.LabProject.model.entity.TagEntity;
import by.prus.LabProject.model.entity.supporting.CertificateTag;
import org.apache.tomcat.jni.Local;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class GiftCertificateResponse extends RepresentationModel<GiftCertificateResponse> implements Serializable {

    private static final long serialVersionUID = 2345356234623452346L;

    private long id;
    private String name;
    private String description;
    private BigDecimal price;
    private int duration;
    private LocalDate createDate;
    private LocalDate lastUpdateDate;
    private List<CertificateTag> certificateTags;

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
    public LocalDate getCreateDate() { return createDate; }
    public void setCreateDate(LocalDate createDate) { this.createDate = createDate; }
    public LocalDate getLastUpdateDate() { return lastUpdateDate; }
    public void setLastUpdateDate(LocalDate lastUpdateDate) { this.lastUpdateDate = lastUpdateDate; }
    public List<CertificateTag> getCertificateTags() { return certificateTags; }
    public void setCertificateTags(List<CertificateTag> certificateTags) { this.certificateTags = certificateTags; }
}
