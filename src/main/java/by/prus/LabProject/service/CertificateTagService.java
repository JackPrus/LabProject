package by.prus.LabProject.service;

import by.prus.LabProject.model.dto.GiftCertificateDTO;
import by.prus.LabProject.model.dto.TagDTO;

public interface CertificateTagService {
    GiftCertificateDTO addTagToCertificate(Long certificateId, Long tagId);
    TagDTO addCertificateToTag(Long tagId, Long certificateId);
}
