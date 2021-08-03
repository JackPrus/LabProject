package by.prus.LabProject.model.entity.supporting;

import by.prus.LabProject.model.entity.GiftCertificateEntity;
import by.prus.LabProject.model.entity.TagEntity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "certificate_tag")
public class CertificateTag {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "gift_certificate_id")
    GiftCertificateEntity giftCertificate;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "tag_id")
    TagEntity tag;

    public GiftCertificateEntity getGiftCertificate() { return giftCertificate;}
    public void setGiftCertificate(GiftCertificateEntity giftCertificate) { this.giftCertificate = giftCertificate;}
    public TagEntity getTag() { return tag;}
    public void setTag(TagEntity tag) { this.tag = tag;}
}
