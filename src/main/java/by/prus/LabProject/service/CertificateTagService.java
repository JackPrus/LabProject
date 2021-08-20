package by.prus.LabProject.service;

import by.prus.LabProject.model.dto.GiftCertificateDTO;
import by.prus.LabProject.model.dto.TagDTO;

import java.util.List;

public interface CertificateTagService {
    GiftCertificateDTO addTagToCertificate(Long certificateId, Long tagId);
    TagDTO addCertificateToTag(Long tagId, Long certificateId);
    List<GiftCertificateDTO> findCertificatesByTagName(String tagName, int page, int limit);
}
