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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;


@Service
public class CertificateTagServiceImpl implements CertificateTagService {

    @Autowired
    GiftCertificateRepository giftCertificateRepository;
    @Autowired
    TagRepository tagRepository;
    @Autowired
    ModelMapper modelMapper;

    /**
     *
     * @param certificateId - id of certificate
     * @param tagId - id of tag
     * @return GifCertificateDTO object is middle betwen certificate and tag connection
     * the method adds tag to certificate
     */
    @Override
    @Transactional
    public GiftCertificateDTO addTagToCertificate(Long certificateId, Long tagId) {

        GiftCertificateEntity certificateEntity = getCertificateWithId(certificateId);
        TagEntity tagEntity = getTageWithId(tagId);

        if (isTagExistsForCertificate(certificateEntity, tagEntity)){
            throw new CertificateServiceException(ErrorMessages.TAG_FOR_CERTIFICATE_ALREADY_EXISTS.getErrorMessage());
        }

        CertificateTag certificateTag = new CertificateTag();
        certificateTag.setGiftCertificate(certificateEntity);
        certificateTag.setTag(tagEntity);

        certificateEntity.getCertificateTags().add(certificateTag);

        GiftCertificateEntity updatedCertificate = giftCertificateRepository.save(certificateEntity);
        GiftCertificateDTO returnValue = modelMapper.map(updatedCertificate, GiftCertificateDTO.class);

        return returnValue;
    }

    @Override
    @Transactional
    public TagDTO addCertificateToTag(Long tagId, Long certificateId) {

        GiftCertificateEntity certificateEntity = getCertificateWithId(certificateId);
        TagEntity tagEntity = getTageWithId(tagId);

        if (isTagExistsForCertificate(certificateEntity, tagEntity)){
            throw new CertificateServiceException(ErrorMessages.TAG_FOR_CERTIFICATE_ALREADY_EXISTS.getErrorMessage());
        }

        CertificateTag certificateTag = new CertificateTag();
        certificateTag.setGiftCertificate(certificateEntity);
        certificateTag.setTag(tagEntity);

        tagEntity.getCertificateTags().add(certificateTag);

        TagEntity updatedTag = tagRepository.save(tagEntity);
        TagDTO returnValue = modelMapper.map(updatedTag, TagDTO.class);

        return returnValue;
    }

    /**
     * The method ask part of tag's name and page and limit to page for pageable request.
     * @param tagName - part of name of tag
     * @param page - number of page
     * @param limit - limit of objects on page
     * @return - List of GiftCertificateDTO that include information aboit connection
     * between certificate and tag, tgat include tags as 'page' parametr
     */

    @Override
    public List<GiftCertificateDTO> findCertificatesByTagName(String tagName, int page, int limit) {

        List<GiftCertificateDTO> returnValue = new ArrayList<>();
        if (page>0) {page = page-1;}
        Pageable pageableRequest = PageRequest.of(page,limit);
        Page<TagEntity> tagPage = tagRepository.findTagsByNamePartAbdReturnPage(tagName, pageableRequest);
        List<TagEntity> tagEntityList = tagPage.getContent();

        for (TagEntity tag : tagEntityList){
            List<CertificateTag> connections = tag.getCertificateTags();
            for (CertificateTag certTag : connections){
                GiftCertificateEntity certificateEntity = certTag.getGiftCertificate();
                GiftCertificateDTO certificateDTO = modelMapper.map(certificateEntity, GiftCertificateDTO.class);
                returnValue.add(certificateDTO);
            }
        }

        returnValue.sort(Comparator.comparing(GiftCertificateDTO::getCreateDate).thenComparing(GiftCertificateDTO::getName));

        return returnValue;
    }

    // всопомгательные методы ниже

    /**
     * methods below are supporting methods for actions above
     */
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

    private boolean isTagExistsForCertificate (GiftCertificateEntity giftCertificateEntity, TagEntity tagEntity){
        for (CertificateTag ct : giftCertificateEntity.getCertificateTags()){
            if (ct.getTag().getId()==tagEntity.getId()){
                return true;
            }
        }
        return false;
    }

}
