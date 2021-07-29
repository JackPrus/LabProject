package by.prus.LabProject.service.impl;

import by.prus.LabProject.exception.CertificateServiceException;
import by.prus.LabProject.exception.TagServiceException;
import by.prus.LabProject.model.dto.GiftCertificateDTO;
import by.prus.LabProject.model.dto.TagDTO;
import by.prus.LabProject.model.entity.GiftCertificateEntity;
import by.prus.LabProject.model.entity.TagEntity;
import by.prus.LabProject.model.entity.supporting.CertificateTag;
import by.prus.LabProject.model.response.ErrorMessage;
import by.prus.LabProject.model.response.ErrorMessages;
import by.prus.LabProject.repository.GiftCertificateRepository;
import by.prus.LabProject.repository.TagRepository;
import by.prus.LabProject.service.CertificateTagService;
import by.prus.LabProject.service.GiftCertificateService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CertificateTagServiceImpl implements CertificateTagService {

    @Autowired
    GiftCertificateRepository giftCertificateRepository;
    @Autowired
    TagRepository tagRepository;

    @Override
    public GiftCertificateDTO addTagToCertificate(Long certificateId, Long tagId) {

        GiftCertificateEntity certificateEntity = getCertificateWithId(certificateId);
        TagEntity tagEntity = getTageWithId(tagId);

        CertificateTag certificateTag = new CertificateTag();
        certificateTag.setGiftCertificate(certificateEntity);
        certificateTag.setTag(tagEntity);

        certificateEntity.getCertificateTags().add(certificateTag);

        ModelMapper modelMapper = new ModelMapper();
        GiftCertificateEntity updatedCertificate = giftCertificateRepository.save(certificateEntity);
        GiftCertificateDTO returnValue = modelMapper.map(updatedCertificate, GiftCertificateDTO.class);

        return returnValue;
    }

    @Override
    public TagDTO addCertificateToTag(Long tagId, Long certificateId) {

        GiftCertificateEntity certificateEntity = getCertificateWithId(certificateId);
        TagEntity tagEntity = getTageWithId(tagId);

        CertificateTag certificateTag = new CertificateTag();
        certificateTag.setGiftCertificate(certificateEntity);
        certificateTag.setTag(tagEntity);

        tagEntity.getCertificateTags().add(certificateTag);

        ModelMapper modelMapper = new ModelMapper();
        TagEntity updatedTag = tagRepository.save(tagEntity);
        TagDTO returnValue = modelMapper.map(updatedTag, TagDTO.class);

        return returnValue;
    }



    private GiftCertificateEntity getCertificateWithId(Long certificateId){

        Optional<GiftCertificateEntity> certificateOptional = giftCertificateRepository.findById(certificateId);
        if (certificateOptional.isEmpty()){
            throw new CertificateServiceException(ErrorMessages.MISSING_CERTIFICATE_WITH_THIS_PARAMETR.getErrorMessage());
        }
        return certificateOptional.get();
    }

    private TagEntity getTageWithId(Long tagId){

        Optional<TagEntity> tagOptional = tagRepository.findById(tagId);
        if (tagOptional.isEmpty()){
            throw new TagServiceException(ErrorMessages.MISSING_TAG_WITH_THIS_PARAMETR.getErrorMessage());
        }
        return tagOptional.get();
    }

}
