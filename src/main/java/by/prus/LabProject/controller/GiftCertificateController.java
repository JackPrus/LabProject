package by.prus.LabProject.controller;

import by.prus.LabProject.exception.CertificateServiceException;
import by.prus.LabProject.model.dto.GiftCertificateDTO;
import by.prus.LabProject.model.request.GiftCertificateRequest;
import by.prus.LabProject.model.response.ErrorMessages;
import by.prus.LabProject.model.response.GiftCertificateResponse;
import by.prus.LabProject.service.GiftCertificateService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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

        if (
                certificate.getDescription().isEmpty() ||
                        certificate.getCreateDate()==null ||
                        certificate.getDuration()==0 ||
                        certificate.getLastUpdateDate()==null ||
                        certificate.getName().isEmpty() ||
                        certificate.getPrice() == null
        ) {
            throw new CertificateServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
        }

        ModelMapper modelMapper = new ModelMapper();
        GiftCertificateDTO giftCertificateDTO = modelMapper.map(certificate, GiftCertificateDTO.class); // копируются поля из одного класса в одноименные поля другого.

        GiftCertificateDTO createdCertificate = giftCertificateService.createCertificate(giftCertificateDTO); // валидируем юзера и создаем юзера без пассворда
        returnValue = modelMapper.map(createdCertificate, GiftCertificateResponse.class);

        return  returnValue;

    }

    @GetMapping(
            path = "/{id}",
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
    )
    public GiftCertificateResponse getUser (@PathVariable Long id){

        ModelMapper modelMapper = new ModelMapper();

        GiftCertificateDTO giftCertificateDTO = giftCertificateService.getCertificate(id);
        GiftCertificateResponse returnValue = modelMapper.map(giftCertificateDTO, GiftCertificateResponse.class);

        return returnValue;
    }

}
