package by.prus.LabProject.controller;

import by.prus.LabProject.exception.TagServiceException;
import by.prus.LabProject.model.dto.TagDTO;
import by.prus.LabProject.model.entity.TagEntity;
import by.prus.LabProject.model.request.TagRequest;
import by.prus.LabProject.model.response.ErrorMessages;
import by.prus.LabProject.model.response.TagResponse;
import by.prus.LabProject.service.TagService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.awt.*;

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

}