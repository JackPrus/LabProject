package by.prus.LabProject.controller;

import by.prus.LabProject.model.dto.GiftCertificateDTO;
import by.prus.LabProject.model.dto.TagDTO;
import by.prus.LabProject.model.response.GiftCertificateResponse;
import by.prus.LabProject.model.response.TagResponse;
import by.prus.LabProject.service.CertificateTagService;
import by.prus.LabProject.service.GiftCertificateService;
import by.prus.LabProject.service.TagService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
//http://localhost:8080/labproject/
public class CertificateTagController {

    @Autowired
    GiftCertificateService giftCertificateService;
    @Autowired
    TagService tagService;
    @Autowired
    CertificateTagService certificateTagService;

    @PutMapping(
            path = "certificate/{certificateId}/tag/{tagId}",
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
    )
    public GiftCertificateResponse addTagToCertificate(
            @PathVariable Long certificateId,
            @PathVariable Long tagId){

        GiftCertificateDTO certificateDTO = certificateTagService.addTagToCertificate(certificateId, tagId);
        ModelMapper modelMapper = new ModelMapper();
        GiftCertificateResponse returnValue = modelMapper.map(certificateDTO, GiftCertificateResponse.class);

        return returnValue;
    }

    @PutMapping(
            path = "tag/{tagId}/certificate/{certificateId}",
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
    )
    public TagResponse addCertificateToTag(
            @PathVariable Long tagId,
            @PathVariable Long certificateId){

        TagDTO tagDTO = certificateTagService.addCertificateToTag(tagId, certificateId);
        ModelMapper modelMapper = new ModelMapper();
        TagResponse returnValue = modelMapper.map(tagDTO, TagResponse.class);

        return returnValue;
    }

    @GetMapping(
            path = "certificate/search/byTag/{tagName}",
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
    )
    public List<GiftCertificateResponse> findCertificatesByTag(@PathVariable String tagName){

        List<GiftCertificateDTO> certificatesDTO = certificateTagService.findCertificatesByTagName(tagName);
        List <GiftCertificateResponse> returnValue = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();

        for (GiftCertificateDTO gcDTO : certificatesDTO){
            returnValue.add(modelMapper.map(gcDTO, GiftCertificateResponse.class));
        }
        return returnValue;
    }

}
