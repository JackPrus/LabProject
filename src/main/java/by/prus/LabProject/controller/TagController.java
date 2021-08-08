package by.prus.LabProject.controller;

import by.prus.LabProject.exception.TagServiceException;
import by.prus.LabProject.model.dto.GiftCertificateDTO;
import by.prus.LabProject.model.dto.TagDTO;
import by.prus.LabProject.model.entity.TagEntity;
import by.prus.LabProject.model.request.TagRequest;
import by.prus.LabProject.model.response.ErrorMessages;
import by.prus.LabProject.model.response.GiftCertificateResponse;
import by.prus.LabProject.model.response.OperationStatusModel;
import by.prus.LabProject.model.response.TagResponse;
import by.prus.LabProject.service.TagService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping ("tag")
//http://localhost:8080/labproject/tag/
public class TagController {

    @Autowired
    TagService tagService;

    @PostMapping(
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
    )
    public TagResponse createTag (@RequestBody TagRequest tagRequest){

        TagResponse returnValue = new TagResponse();

        if (tagRequest.getName().isEmpty()){
            throw new TagServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
        }

        ModelMapper modelMapper = new ModelMapper();
        TagDTO tagDTO = modelMapper.map(tagRequest, TagDTO.class);

        TagDTO createdTag = tagService.createTag(tagDTO);
        returnValue = modelMapper.map(createdTag, TagResponse.class);

        return returnValue;
    }

    @GetMapping(
            path = "{id}",
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
    )
    public TagResponse getTag (@PathVariable Long id){

        ModelMapper modelMapper = new ModelMapper();
        TagDTO tagDTO = tagService.getTag(id);

        TagResponse returnValue = modelMapper.map(tagDTO, TagResponse.class);

        return returnValue;
    }

    @PutMapping(
            path = "{tagId}",
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
    )
    public TagResponse updateTag (@PathVariable Long tagId, @RequestBody TagRequest tagRequest){

        if (tagRequest.getName().isEmpty()){
            throw new TagServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
        }

        ModelMapper modelMapper = new ModelMapper();
        TagDTO tagToUpdate = modelMapper.map(tagRequest, TagDTO.class);

        TagDTO updatedTag = tagService.updateTag(tagId, tagToUpdate);
        TagResponse returnValue = modelMapper.map(updatedTag, TagResponse.class);

        return returnValue;
    }

    @DeleteMapping(
            path = "{tagId}",
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
    )
    public OperationStatusModel deleteCertificate (@PathVariable Long tagId){

        OperationStatusModel returnValue = new OperationStatusModel();
        returnValue.setOperationName("Delete of Tag with Id: " +tagId);

        tagService.deleteTag(tagId);
        returnValue.setOperationResult("Status : Deleted successfuly");
        return returnValue;
    }

    @GetMapping(
            path = "/search/{partOfTagName}",
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
    )
    public List<TagResponse> getListOfTagsByPartOfName (@PathVariable String partOfTagName){
        List<TagDTO> tagsDTO = tagService.findTagByNamePart(partOfTagName);
        List<TagResponse> returnValue = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();

        for (TagDTO tagDTO : tagsDTO){
            returnValue.add(modelMapper.map(tagDTO, TagResponse.class));
        }
        return returnValue;
    }

}
