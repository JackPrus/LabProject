package by.prus.LabProject.service.impl;

import by.prus.LabProject.model.dto.GiftCertificateDTO;
import by.prus.LabProject.model.entity.GiftCertificateEntity;
import by.prus.LabProject.repository.GiftCertificateRepository;
import by.prus.LabProject.service.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    @Autowired
    GiftCertificateRepository giftCertificateRepository;

    @Override
    public GiftCertificateDTO createCertificate(GiftCertificateDTO giftCertificateDTO) {

        GiftCertificateEntity giftCertificateEntity = giftCertificateRepository.findById(giftCertificateDTO.getId());

        return null;
    }

    @Override
    public GiftCertificateDTO getCertificate(GiftCertificateDTO giftCertificateDTO) {
        return null;
    }
}
