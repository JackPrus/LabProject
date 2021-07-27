package by.prus.LabProject.model.entity.supporting;

import by.prus.LabProject.model.entity.GiftCertificateEntity;
import by.prus.LabProject.model.entity.TagEntity;

import javax.persistence.*;

@Entity
public class CertificateTag {

    @Id
    private long id;

    @ManyToOne
    @JoinColumn(name = "gift_certificat_id")
    GiftCertificateEntity giftCertificate;

    @ManyToOne
    @JoinColumn(name = "tag_id")
    TagEntity tag;

}
