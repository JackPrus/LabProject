package by.prus.LabProject.controller;

import by.prus.LabProject.exception.TagServiceException;
import by.prus.LabProject.model.dto.GiftCertificateDTO;
import by.prus.LabProject.model.dto.TagDTO;
import by.prus.LabProject.model.entity.TagEntity;
import by.prus.LabProject.model.request.RequestOperationName;
import by.prus.LabProject.model.request.TagRequest;
import by.prus.LabProject.model.response.*;
import by.prus.LabProject.service.TagService;
import by.prus.LabProject.service.relgenerator.LinkCreator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dzmitry Prus.
 * @version 1.0
 * The methods of class implement CRUD and others operations with tag.
 */

@RestController
@RequestMapping ("/tag")
//http://localhost:8080/labproject/tag/
public class TagController {

    @Autowired
    TagService tagService;
    @Autowired
    LinkCreator linkCreator;
    @Autowired
    ModelMapper modelMapper;


    @PostMapping(
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
    )
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public TagResponse createTag (@RequestBody TagRequest tagRequest){

        TagResponse returnValue = new TagResponse();

        if (tagRequest.getName().isEmpty()){
            throw new TagServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
        }

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

        TagDTO tagDTO = tagService.getTag(id);

        TagResponse returnValue = modelMapper.map(tagDTO, TagResponse.class);
        linkCreator.addLinkToTagResponse(returnValue);

        return returnValue;
    }


    @PutMapping(
            path = "{tagId}",
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
    )
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public TagResponse updateTag (@PathVariable Long tagId, @RequestBody TagRequest tagRequest){

        if (tagRequest.getName().isEmpty()){
            throw new TagServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
        }

        TagDTO tagToUpdate = modelMapper.map(tagRequest, TagDTO.class);

        TagDTO updatedTag = tagService.updateTag(tagId, tagToUpdate);
        TagResponse returnValue = modelMapper.map(updatedTag, TagResponse.class);

        return returnValue;
    }

    @DeleteMapping(
            path = "{tagId}",
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
    )
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public OperationStatusModel deleteTag (@PathVariable Long tagId){

        OperationStatusModel returnValue = new OperationStatusModel();
        returnValue.setOperationName(RequestOperationName.DELETE.name());

        tagService.deleteTag(tagId);
        returnValue.setOperationResult(ResponseOperationStatus.SUCCESS.name());
        return returnValue;
    }

    @GetMapping(
            path = "/search/{partOfTagName}",
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
    )
    public List<TagResponse> getListOfTagsByPartOfName (@PathVariable String partOfTagName){
        List<TagDTO> tagsDTO = tagService.findTagByNamePart(partOfTagName);
        List<TagResponse> returnValue = new ArrayList<>();

        for (TagDTO tagDTO : tagsDTO){
            returnValue.add(modelMapper.map(tagDTO, TagResponse.class));
        }
        return returnValue;
    }

    /**
     *
     * @param page - number of pagge mapped in URL
     * @param limit - limit of Tag objects represented in JSON or XML view per page.
     * @return - Collection of Tag in quantity equals 'limit' vlue.
     *
     * The pagination and HATEOAS lincs were used here
     * @see - LinkCreator.class
     */

    //http://localhost:8080/labproject/tag?page=3&limit=2 (выведет количество значений в соответствии с defaultValue of limit)
    @GetMapping (produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}) // с пагинацией
    public CollectionModel<TagResponse> getTags(
            @RequestParam(value = "page", defaultValue = "0") int page, //страницы начинаются с нуля
            @RequestParam(value = "limit", defaultValue = "25") int limit
    ){
        List<TagResponse> responseValue = new ArrayList<>();
        List<TagDTO> tags = tagService.getTags(page, limit);


        for (TagDTO tagDTO : tags){
            TagResponse tagResponse = modelMapper.map(tagDTO, TagResponse.class);
            linkCreator.addLinkToTagResponse(tagResponse);
            responseValue.add(tagResponse);
        }

        CollectionModel<TagResponse> returnValue = linkCreator.getNavigationTagLinks(responseValue, page, limit);

        return returnValue;
    }



}
