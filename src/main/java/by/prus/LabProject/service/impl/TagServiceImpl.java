package by.prus.LabProject.service.impl;

import by.prus.LabProject.exception.TagServiceException;
import by.prus.LabProject.model.dto.TagDTO;
import by.prus.LabProject.model.entity.TagEntity;
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
    public TagDTO getTag(long id) {

        Optional<TagEntity> optionalTag =tagRepository.findById(id);
        if (optionalTag.isEmpty()){throw new TagServiceException("Tag with this ID does not exist");}

        ModelMapper modelMapper = new ModelMapper();

        TagEntity storedTag = optionalTag.get();
        TagDTO returnValue = modelMapper.map(storedTag, TagDTO.class);

        return returnValue;
    }
}
