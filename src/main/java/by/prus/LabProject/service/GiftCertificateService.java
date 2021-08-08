package by.prus.LabProject.service;

import by.prus.LabProject.model.dto.GiftCertificateDTO;
import by.prus.LabProject.model.dto.TagDTO;

import java.util.List;

public interface GiftCertificateService {

    GiftCertificateDTO createCertificate(GiftCertificateDTO giftCertificateDTO);
    GiftCertificateDTO getCertificate (Long certificateId);
    GiftCertificateDTO updateCertificate(Long certificateId, GiftCertificateDTO giftCertificateDTO);
    void deleteCertificate(Long certificateId);
    List<GiftCertificateDTO> findCertificatesByNamePart(String partOfCertName);
    List<GiftCertificateDTO> getCertificates(int page,int limit);

}
