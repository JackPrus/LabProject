package by.prus.LabProject.model.entity.supporting;

import by.prus.LabProject.model.entity.GiftCertificate;
import by.prus.LabProject.model.entity.Tag;

import javax.persistence.*;

@Entity
public class CertificateTag {

    @Id
    private long id;

    @ManyToOne
    @JoinColumn(name = "gift_certificat_id")
    GiftCertificate giftCertificate;

    @ManyToOne
    @JoinColumn(name = "tag_id")
    Tag tag;

}
