package by.prus.LabProject.controller;

import by.prus.LabProject.exception.CertificateServiceException;
import by.prus.LabProject.model.dto.GiftCertificateDTO;
import by.prus.LabProject.model.request.GiftCertificateRequest;
import by.prus.LabProject.model.request.RequestOperationName;
import by.prus.LabProject.model.response.ErrorMessages;
import by.prus.LabProject.model.response.GiftCertificateResponse;
import by.prus.LabProject.model.response.OperationStatusModel;
import by.prus.LabProject.model.response.ResponseOperationStatus;
import by.prus.LabProject.service.GiftCertificateService;
import by.prus.LabProject.service.relgenerator.LinkCreator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/certificate")
//http://localhost:8080/labproject/certificate/
public class GiftCertificateController {

    @Autowired
    GiftCertificateService giftCertificateService;
    @Autowired
    LinkCreator linkCreator;

    @PostMapping(
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}, // то что возвращает
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}  // то что принимает от клиента
    )
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public GiftCertificateResponse createCertificate(@RequestBody GiftCertificateRequest certificate) { // конвертирует Java объект в JSON файл

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

        //http://localhost:8080/labproject/certificate/{certificateId}
        linkCreator.addLinkToCertificateResponse(returnValue);

        return returnValue;
    }

    @PutMapping(
            path = "{certificateId}",
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
    )
    @PreAuthorize("hasRole('ROLE_ADMIN')")
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

        if (
                certificate.getDescription().isEmpty() ||
                certificate.getDuration()==0 ||
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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public OperationStatusModel deleteCertificate (@PathVariable Long certificateId){

        OperationStatusModel returnValue = new OperationStatusModel();
        returnValue.setOperationName(RequestOperationName.DELETE.name());

        giftCertificateService.deleteCertificate(certificateId);
        returnValue.setOperationResult(ResponseOperationStatus.SUCCESS.name());
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

    //ПАГИНАЦИЯ + hateoas ссылки
    //http://localhost:8080/labproject/certificate?page=3&limit=2 (выведет количество значений в соответствии с defaultValue of limit)
    @GetMapping (produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}) // с пагинацией
    public CollectionModel<GiftCertificateResponse> getCertificates(
            @RequestParam(value = "page", defaultValue = "0") int page, //страницы начинаются с нуля
            @RequestParam(value = "limit", defaultValue = "25") int limit
    ){
        List<GiftCertificateResponse> responseValue = new ArrayList<>();
        List<GiftCertificateDTO> certificates = giftCertificateService.getCertificates(page, limit);

        ModelMapper modelMapper = new ModelMapper();

        for (GiftCertificateDTO certDTO : certificates){
            GiftCertificateResponse certRespons = modelMapper.map(certDTO, GiftCertificateResponse.class);
            linkCreator.addLinkToCertificateResponse(certRespons);
            responseValue.add(certRespons);
        }

        CollectionModel<GiftCertificateResponse> returnValue = linkCreator.getNavigationCertLinks(responseValue, page, limit);

        return returnValue;
    }

}
