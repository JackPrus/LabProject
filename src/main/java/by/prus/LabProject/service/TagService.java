package by.prus.LabProject.service;

import by.prus.LabProject.model.dto.TagDTO;

public interface TagService {

    TagDTO createTag (TagDTO tagDTO);
    TagDTO getTag (Long id);
    TagDTO updateTag(Long tagId, TagDTO tagDTO);
    void deleteTag(Long tagId);

}
