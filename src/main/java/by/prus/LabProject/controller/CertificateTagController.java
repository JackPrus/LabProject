package by.prus.LabProject.controller;

import by.prus.LabProject.model.dto.GiftCertificateDTO;
import by.prus.LabProject.model.dto.TagDTO;
import by.prus.LabProject.model.response.GiftCertificateResponse;
import by.prus.LabProject.model.response.TagResponse;
import by.prus.LabProject.service.CertificateTagService;
import by.prus.LabProject.service.GiftCertificateService;
import by.prus.LabProject.service.TagService;
import by.prus.LabProject.service.relgenerator.LinkCreator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dzmitry Prus.
 * @version 1.0
 * The methods of class implement actions between certificate and tag. Add tag to certificate or
 * upside down for example.
 */

@RestController
//http://localhost:8080/labproject/

public class CertificateTagController {

    @Autowired
    GiftCertificateService giftCertificateService;
    @Autowired
    TagService tagService;
    @Autowired
    CertificateTagService certificateTagService;
    @Autowired
    LinkCreator linkCreator;
    @Autowired
    ModelMapper modelMapper;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(
            path = "certificate/{certificateId}/tag/{tagId}",
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
    )
    public GiftCertificateResponse addTagToCertificate(
            @PathVariable Long certificateId,
            @PathVariable Long tagId){

        GiftCertificateDTO certificateDTO = certificateTagService.addTagToCertificate(certificateId, tagId);
        GiftCertificateResponse returnValue = modelMapper.map(certificateDTO, GiftCertificateResponse.class);

        return returnValue;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(
            path = "tag/{tagId}/certificate/{certificateId}",
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
    )
    public TagResponse addCertificateToTag(
            @PathVariable Long tagId,
            @PathVariable Long certificateId){

        TagDTO tagDTO = certificateTagService.addCertificateToTag(tagId, certificateId);
        TagResponse returnValue = modelMapper.map(tagDTO, TagResponse.class);

        return returnValue;
    }

    @GetMapping(
            path = "certificate/search/byTag/{tagName}/{page}/{limit}",
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
    )
    public CollectionModel<GiftCertificateResponse> findCertificatesByTag(
            @PathVariable String tagName,
            @PathVariable int page,
            @PathVariable int limit
    ){

        List<GiftCertificateDTO> certificatesDTO = certificateTagService.findCertificatesByTagName(tagName, page, limit);
        List <GiftCertificateResponse> certValue = new ArrayList<>();

        for (GiftCertificateDTO gcDTO : certificatesDTO){
            certValue.add(modelMapper.map(gcDTO, GiftCertificateResponse.class));
        }
        CollectionModel<GiftCertificateResponse> returnValue = linkCreator.getNavigationCertSearchByTagLinks(certValue, tagName, page,limit);

        return returnValue;
    }

}
