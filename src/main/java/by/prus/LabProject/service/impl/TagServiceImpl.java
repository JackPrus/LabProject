package by.prus.LabProject.service.impl;

import by.prus.LabProject.exception.CertificateServiceException;
import by.prus.LabProject.exception.TagServiceException;
import by.prus.LabProject.model.dto.TagDTO;
import by.prus.LabProject.model.entity.GiftCertificateEntity;
import by.prus.LabProject.model.entity.TagEntity;
import by.prus.LabProject.model.response.ErrorMessages;
import by.prus.LabProject.repository.CertificateTagRepository;
import by.prus.LabProject.repository.TagRepository;
import by.prus.LabProject.service.TagService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    TagRepository tagRepository;
    @Autowired
    CertificateTagRepository certificateTagRepository;

    @Override
    public TagDTO createTag(TagDTO tagDTO) {

        Optional<TagEntity> optionalTag =tagRepository.findById(tagDTO.getId());
        if (optionalTag.isPresent()){throw new TagServiceException("Error with creation new tag. Tag wit this ID already exists");}

        ModelMapper modelMapper = new ModelMapper();
        TagEntity tagEntity = modelMapper.map(tagDTO, TagEntity.class);
        TagEntity storedTag = tagRepository.save(tagEntity);
        TagDTO returnValue = modelMapper.map(storedTag, TagDTO.class);

        return returnValue;
    }

    @Override
    public TagDTO getTag(Long id) {

        Optional<TagEntity> optionalTag =tagRepository.findById(id);
        if (optionalTag.isEmpty()){throw new TagServiceException("Tag with this ID does not exist");}

        ModelMapper modelMapper = new ModelMapper();

        TagEntity storedTag = optionalTag.get();
        TagDTO returnValue = modelMapper.map(storedTag, TagDTO.class);

        return returnValue;
    }

    @Override
    public TagDTO updateTag(Long tagId, TagDTO tagDTO) {

        Optional<TagEntity> optionalTag =tagRepository.findById(tagId);
        if (optionalTag.isEmpty()){throw new TagServiceException("Tag with this ID does not exist");}

        ModelMapper modelMapper = new ModelMapper();
        TagEntity tagToUpdate = optionalTag.get();
        tagToUpdate.setName(tagDTO.getName());
        TagEntity updatedTag = tagRepository.save(tagToUpdate);

        TagDTO returnValue = modelMapper.map(updatedTag, TagDTO.class);

        return returnValue;
    }

    @Override
    public void deleteTag(Long tagId) {
        Optional<TagEntity> tagOptional = tagRepository.findById(tagId);
        if (tagOptional.isEmpty()){
            throw new TagServiceException(ErrorMessages.MISSING_TAG_WITH_THIS_PARAMETR.getErrorMessage());
        }
        tagRepository.deleteTag(tagId);
    }

}
