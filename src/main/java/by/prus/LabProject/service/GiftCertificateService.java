package by.prus.LabProject.service;

import by.prus.LabProject.model.dto.GiftCertificateDTO;

public interface GiftCertificateService {

    GiftCertificateDTO createCertificate(GiftCertificateDTO giftCertificateDTO);
    GiftCertificateDTO getCertificate (Long certificateId);

}
