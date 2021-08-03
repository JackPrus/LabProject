package by.prus.LabProject.service.impl;

import by.prus.LabProject.exception.CertificateServiceException;
import by.prus.LabProject.model.dto.GiftCertificateDTO;
import by.prus.LabProject.model.dto.TagDTO;
import by.prus.LabProject.model.entity.GiftCertificateEntity;
import by.prus.LabProject.model.entity.TagEntity;
import by.prus.LabProject.model.entity.supporting.CertificateTag;
import by.prus.LabProject.model.response.ErrorMessages;
import by.prus.LabProject.repository.CertificateTagRepository;
import by.prus.LabProject.repository.GiftCertificateRepository;
import by.prus.LabProject.service.GiftCertificateService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    @Autowired
    GiftCertificateRepository giftCertificateRepository;
    @Autowired
    CertificateTagRepository certificateTagRepository;

    @Override
    public GiftCertificateDTO createCertificate(GiftCertificateDTO giftCertificateDTO) {

        Optional<GiftCertificateEntity> optionalCertificate = giftCertificateRepository.findById(giftCertificateDTO.getId());
        if (optionalCertificate.isPresent()){
            throw new CertificateServiceException("certificate with this ID already exist");
        }

        ModelMapper modelMapper = new ModelMapper();
        GiftCertificateEntity giftCertificateEntity =modelMapper.map(giftCertificateDTO, GiftCertificateEntity.class);
        GiftCertificateEntity storedCertificate = giftCertificateRepository.save(giftCertificateEntity);
        GiftCertificateDTO returnValue = modelMapper.map(storedCertificate, GiftCertificateDTO.class);

        return returnValue;
    }

    @Override
    public GiftCertificateDTO getCertificate(Long certififcateId) {

        Optional<GiftCertificateEntity> optionalCertificate = giftCertificateRepository.findById(certififcateId);
        if (optionalCertificate.isEmpty()){
            throw new CertificateServiceException("certificate with this ID does not exist");
        }

        ModelMapper modelMapper = new ModelMapper();
        GiftCertificateEntity certificateEntity = optionalCertificate.get();
        GiftCertificateDTO returnValue = modelMapper.map(certificateEntity, GiftCertificateDTO.class);

        return returnValue;
    }

    @Override
    public GiftCertificateDTO updateCertificate(Long certificateId, GiftCertificateDTO giftCertificateDTO) {

        Optional<GiftCertificateEntity> optionalCertificate = giftCertificateRepository.findById(certificateId);
        if (optionalCertificate.isEmpty()){
            throw new CertificateServiceException("certificate with this ID does not exist");
        }

        GiftCertificateEntity certificateEntity = optionalCertificate.get();

        certificateEntity.setDescription(giftCertificateDTO.getDescription());
        certificateEntity.setDuration(giftCertificateDTO.getDuration());
        certificateEntity.setCreateDate(giftCertificateDTO.getCreateDate());
        certificateEntity.setLastUpdateDate(giftCertificateDTO.getLastUpdateDate());
        certificateEntity.setName(giftCertificateDTO.getName());
        certificateEntity.setPrice(giftCertificateDTO.getPrice());

        GiftCertificateEntity updatedCertificate = giftCertificateRepository.save(certificateEntity);
        ModelMapper modelMapper = new ModelMapper();
        GiftCertificateDTO returnValue = modelMapper.map(updatedCertificate, GiftCertificateDTO.class);

        return returnValue;
    }

    @Override
    public void deleteCertificate(Long certificateId) {
        Optional<GiftCertificateEntity> certificateOptional = giftCertificateRepository.findById(certificateId);
        if (certificateOptional.isEmpty()){
            throw new CertificateServiceException(ErrorMessages.MISSING_CERTIFICATE_WITH_THIS_PARAMETR.getErrorMessage());
        }
        giftCertificateRepository.deleteById(certificateId);
    }

}
