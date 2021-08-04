package by.prus.LabProject.controller;

import by.prus.LabProject.exception.CertificateServiceException;
import by.prus.LabProject.model.dto.GiftCertificateDTO;
import by.prus.LabProject.model.dto.TagDTO;
import by.prus.LabProject.model.request.GiftCertificateRequest;
import by.prus.LabProject.model.response.ErrorMessages;
import by.prus.LabProject.model.response.GiftCertificateResponse;
import by.prus.LabProject.model.response.OperationStatusModel;
import by.prus.LabProject.service.GiftCertificateService;
import jdk.dynalink.Operation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("certificate")
//http://localhost:8080/labproject/certificate/
public class GiftCertificateController {

    @Autowired
    GiftCertificateService giftCertificateService;

    @PostMapping(
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
    )
    public GiftCertificateResponse createUser(@RequestBody GiftCertificateRequest certificate) { // конвертирует Java объект в JSON файл

        GiftCertificateResponse returnValue = new GiftCertificateResponse();

        if (!validateRequestCertificate(certificate)) {
            throw new CertificateServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
        }

        ModelMapper modelMapper = new ModelMapper();
        GiftCertificateDTO giftCertificateDTO = modelMapper.map(certificate, GiftCertificateDTO.class); // копируются поля из одного класса в одноименные поля другого.

        GiftCertificateDTO createdCertificate = giftCertificateService.createCertificate(giftCertificateDTO); // валидируем юзера и создаем юзера без пассворда
        returnValue = modelMapper.map(createdCertificate, GiftCertificateResponse.class);

        return  returnValue;
    }

    @GetMapping(
            path = "{id}",
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
    )
    public GiftCertificateResponse getCertificate (@PathVariable Long id){

        ModelMapper modelMapper = new ModelMapper();

        GiftCertificateDTO giftCertificateDTO = giftCertificateService.getCertificate(id);
        GiftCertificateResponse returnValue = modelMapper.map(giftCertificateDTO, GiftCertificateResponse.class);

        return returnValue;
    }

    @PutMapping(
            path = "{certificateId}",
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
    )
    public GiftCertificateResponse updateCertificate (@PathVariable Long certificateId, @RequestBody GiftCertificateRequest giftCertificateRequest){

        if (!validateRequestCertificate(giftCertificateRequest)) {
            throw new CertificateServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
        }

        ModelMapper modelMapper = new ModelMapper();
        GiftCertificateDTO certificateDTO = modelMapper.map(giftCertificateRequest, GiftCertificateDTO.class);
        GiftCertificateDTO updatedCertificate = giftCertificateService.updateCertificate(certificateId, certificateDTO);

        GiftCertificateResponse returnValue = modelMapper.map(updatedCertificate, GiftCertificateResponse.class);

        return returnValue;
    }

    public boolean validateRequestCertificate(GiftCertificateRequest certificate){

        if
        (       certificate.getDescription().isEmpty() ||
                certificate.getCreateDate()==null ||
                certificate.getDuration()==0 ||
                certificate.getLastUpdateDate()==null ||
                certificate.getName().isEmpty() ||
                certificate.getPrice() == null
        ) {
            return false;
        }
        return true;
    }

    @DeleteMapping(
            path = "{certificateId}",
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
    )
    public OperationStatusModel deleteCertificate (@PathVariable Long certificateId){

        OperationStatusModel returnValue = new OperationStatusModel();
        returnValue.setOperationName("Delete of Certificate with Id: " +certificateId);

        giftCertificateService.deleteCertificate(certificateId);
        returnValue.setOperationResult("Status : Deleted successfuly");
        return returnValue;
    }

    @GetMapping(
            path = "/search/{partOfCertName}",
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
    )
    public List<GiftCertificateResponse> getListOfCertificatesByPartOfName (@PathVariable String partOfCertName){
        List<GiftCertificateDTO> certificatesDTO = giftCertificateService.findCertificatesByNamePart(partOfCertName);
        List <GiftCertificateResponse> returnValue = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();

        for (GiftCertificateDTO gcDTO : certificatesDTO){
            returnValue.add(modelMapper.map(gcDTO, GiftCertificateResponse.class));
        }

        return returnValue;
    }

}
