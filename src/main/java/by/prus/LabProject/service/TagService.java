package by.prus.LabProject.service;

import by.prus.LabProject.model.dto.GiftCertificateDTO;
import by.prus.LabProject.model.dto.TagDTO;

import java.util.List;

public interface TagService {

    TagDTO createTag (TagDTO tagDTO);
    TagDTO getTag (Long id);
    TagDTO updateTag(Long tagId, TagDTO tagDTO);
    void deleteTag(Long tagId);
    List<TagDTO> findTagByNamePart(String partOfName);

}
