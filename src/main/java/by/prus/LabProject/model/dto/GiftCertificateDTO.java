package by.prus.LabProject.model.dto;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class GiftCertificateDTO implements Serializable {

    private static final long serialVersionUID = 2345356234623452346L;

    private long id;
    private String name;
    private String description;
    private BigDecimal price;
    private int duration;
    private Date createDate;
    private Date lastUpdateDate;

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
