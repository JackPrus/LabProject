package by.prus.LabProject.service.impl;

import by.prus.LabProject.exception.CertificateServiceException;
import by.prus.LabProject.model.dto.GiftCertificateDTO;
import by.prus.LabProject.model.dto.TagDTO;
import by.prus.LabProject.model.entity.GiftCertificateEntity;
import by.prus.LabProject.model.entity.TagEntity;
import by.prus.LabProject.model.entity.supporting.CertificateTag;
import by.prus.LabProject.model.response.ErrorMessages;
import by.prus.LabProject.model.response.GiftCertificateResponse;
import by.prus.LabProject.repository.CertificateTagRepository;
import by.prus.LabProject.repository.GiftCertificateRepository;
import by.prus.LabProject.service.GiftCertificateService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    @Autowired
    GiftCertificateRepository giftCertificateRepository;
    @Autowired
    ModelMapper modelMapper;


    /**
     * The method creates a certificate. Create date and lastUpdateDate getting today's.
     * @param giftCertificateDTO - object with reagards to which one the certificate must be created
     * @return - resulting object when certificate is created
     */
    @Override
    @Transactional
    public GiftCertificateDTO createCertificate(GiftCertificateDTO giftCertificateDTO) {

        Optional<GiftCertificateEntity> optionalCertificate = giftCertificateRepository.findById(giftCertificateDTO.getId());
        if (optionalCertificate.isPresent()){
            throw new CertificateServiceException("certificate with this ID already exist");
        }

        giftCertificateDTO.setCreateDate(LocalDate.now());
        giftCertificateDTO.setLastUpdateDate(LocalDate.now());

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

        GiftCertificateEntity certificateEntity = optionalCertificate.get();
        GiftCertificateDTO returnValue = modelMapper.map(certificateEntity, GiftCertificateDTO.class);

        return returnValue;
    }

    /**
     * The method updating data of some exact certificate. The 'lastUpdateDate' becames today's.
     * 'createDate' should be passed
     * @param certificateId - id of Certificate need to update
     * @param giftCertificateDTO - an oject including data to update
     * @return -
     */
    @Override
    @Transactional
    public GiftCertificateDTO updateCertificate(Long certificateId, GiftCertificateDTO giftCertificateDTO) {

        Optional<GiftCertificateEntity> optionalCertificate = giftCertificateRepository.findById(certificateId);
        if (optionalCertificate.isEmpty()){
            throw new CertificateServiceException("certificate with this ID does not exist");
        }
        giftCertificateDTO.setLastUpdateDate(LocalDate.now());
        GiftCertificateEntity certificateEntity = optionalCertificate.get();

        certificateEntity.setDescription(giftCertificateDTO.getDescription());
        certificateEntity.setDuration(giftCertificateDTO.getDuration());
        //certificateEntity.setCreateDate(giftCertificateDTO.getCreateDate()); не изменна эта дата, т.к. дата создания.
        certificateEntity.setLastUpdateDate(giftCertificateDTO.getLastUpdateDate());
        certificateEntity.setName(giftCertificateDTO.getName());
        certificateEntity.setPrice(giftCertificateDTO.getPrice());

        GiftCertificateEntity updatedCertificate = giftCertificateRepository.save(certificateEntity);
        GiftCertificateDTO returnValue = modelMapper.map(updatedCertificate, GiftCertificateDTO.class);

        return returnValue;
    }

    @Override
    @Transactional
    public void deleteCertificate(Long certificateId) {
        Optional<GiftCertificateEntity> certificateOptional = giftCertificateRepository.findById(certificateId);
        if (certificateOptional.isEmpty()){
            throw new CertificateServiceException(ErrorMessages.MISSING_CERTIFICATE_WITH_THIS_PARAMETR.getErrorMessage());
        }
        giftCertificateRepository.deleteCertificate(certificateId);
    }

    /**
     * The method returning List of Certificates, that ine name field have part of 'partOfCertName'
     * param. In order to do it GiftCertificateRepository use native @Query request.
     * @param partOfCertName - pattern with regars to which one the method looking for certificates
     * @return- List of certificates having field 'name' including 'partOfCertName' param
     */
    @Override
    public List<GiftCertificateDTO> findCertificatesByNamePart(String partOfCertName) {

        List<GiftCertificateEntity> certificateList = giftCertificateRepository.findCertificatesByNamePart(partOfCertName);
        List<GiftCertificateDTO> returnValue = new ArrayList<>();

        for (GiftCertificateEntity entity : certificateList){
            returnValue.add(modelMapper.map(entity, GiftCertificateDTO.class));
        }

        return returnValue;
    }

    //pagination in action
    @Override
    public List<GiftCertificateDTO> getCertificates(int page, int limit) {
        List<GiftCertificateDTO> returnValue = new ArrayList<>();
        if(page>0) { page = page-1;}
        Pageable pageableRequest = PageRequest.of(page, limit); // объект который мы засунем в репозиторий, чтобы достать результат по страницам
        Page<GiftCertificateEntity> certificatePage = giftCertificateRepository.findAll(pageableRequest); // находит нужную страницу юзеров
        List<GiftCertificateEntity> certificates = certificatePage.getContent();

        for (GiftCertificateEntity certEntity : certificates) {
            GiftCertificateDTO certDTO = modelMapper.map(certEntity, GiftCertificateDTO.class);
            returnValue.add(certDTO);
        }
        return returnValue;
    }

}
