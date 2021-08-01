package by.prus.LabProject.service;

import by.prus.LabProject.model.dto.GiftCertificateDTO;
import by.prus.LabProject.model.dto.TagDTO;

public interface GiftCertificateService {

    GiftCertificateDTO createCertificate(GiftCertificateDTO giftCertificateDTO);
    GiftCertificateDTO getCertificate (Long certificateId);
    GiftCertificateDTO updateCertificate(Long certificateId, GiftCertificateDTO giftCertificateDTO);
    void deleteCertificate(Long certificateId);

}
