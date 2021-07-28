package by.prus.LabProject.service;

import by.prus.LabProject.model.dto.TagDTO;

public interface TagService {

    TagDTO createTag (TagDTO tagDTO);
    TagDTO getTag (long id);

}
