package by.prus.LabProject.service.impl;

import by.prus.LabProject.exception.CertificateServiceException;
import by.prus.LabProject.exception.TagServiceException;
import by.prus.LabProject.model.dto.GiftCertificateDTO;
import by.prus.LabProject.model.dto.TagDTO;
import by.prus.LabProject.model.entity.GiftCertificateEntity;
import by.prus.LabProject.model.entity.TagEntity;
import by.prus.LabProject.model.response.ErrorMessages;
import by.prus.LabProject.repository.CertificateTagRepository;
import by.prus.LabProject.repository.TagRepository;
import by.prus.LabProject.service.TagService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class TagServiceImpl implements TagService {

    @Autowired
    TagRepository tagRepository;
    @Autowired
    CertificateTagRepository certificateTagRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    @Transactional
    public TagDTO createTag(TagDTO tagDTO) {

        Optional<TagEntity> optionalTag =tagRepository.findById(tagDTO.getId());
        if (optionalTag.isPresent()){throw new TagServiceException("Error with creation new tag. Tag wit this ID already exists");}

        TagEntity tagEntity = modelMapper.map(tagDTO, TagEntity.class);
        TagEntity storedTag = tagRepository.save(tagEntity);
        TagDTO returnValue = modelMapper.map(storedTag, TagDTO.class);

        return returnValue;
    }

    @Override
    public TagDTO getTag(Long id) {

        Optional<TagEntity> optionalTag =tagRepository.findById(id);
        if (optionalTag.isEmpty()){throw new TagServiceException("Tag with this ID does not exist");}

        TagEntity storedTag = optionalTag.get();
        TagDTO returnValue = modelMapper.map(storedTag, TagDTO.class);

        return returnValue;
    }

    @Override
    @Transactional
    public TagDTO updateTag(Long tagId, TagDTO tagDTO) {

        Optional<TagEntity> optionalTag =tagRepository.findById(tagId);
        if (optionalTag.isEmpty()){throw new TagServiceException("Tag with this ID does not exist");}

        TagEntity tagToUpdate = optionalTag.get();
        tagToUpdate.setName(tagDTO.getName());
        TagEntity updatedTag = tagRepository.save(tagToUpdate);

        TagDTO returnValue = modelMapper.map(updatedTag, TagDTO.class);

        return returnValue;
    }

    @Override
    @Transactional
    public void deleteTag(Long tagId) {
        Optional<TagEntity> tagOptional = tagRepository.findById(tagId);
        if (tagOptional.isEmpty()){
            throw new TagServiceException(ErrorMessages.MISSING_TAG_WITH_THIS_PARAMETR.getErrorMessage());
        }
        tagRepository.deleteTag(tagId);
    }

    /**
     * The method returning List of Tags, that have field having part of 'partOfCertName'
     * param. In order to do it TagRepository use native @Query request.
     * @param partOfName - pattern with regars to which one the method looking for certificates
     * @return- List of tags having field 'name' including 'partOfName' param
     */
    @Override
    public List<TagDTO> findTagByNamePart(String partOfName) {
        List<TagEntity> tagEntityList = tagRepository.findTagsByNamePart(partOfName);
        List<TagDTO> returnValue = new ArrayList<>();

        for (TagEntity entity : tagEntityList){
            returnValue.add(modelMapper.map(entity, TagDTO.class));
        }

        return returnValue;
    }

    @Override
    public List<TagDTO> getTags(int page, int limit) {
        List<TagDTO> returnValue = new ArrayList<>();
        if(page>0) { page = page-1;}
        Pageable pageableRequest = PageRequest.of(page, limit); // объект который мы засунем в репозиторий, чтобы достать результат по страницам
        Page<TagEntity> tagPage = tagRepository.findAll(pageableRequest); // находит нужную страницу юзеров
        List<TagEntity> tags = tagPage.getContent();

        for (TagEntity tagEntity : tags) {
            TagDTO tagDTO = modelMapper.map(tagEntity, TagDTO.class);
            returnValue.add(tagDTO);
        }
        return returnValue;
    }

}
