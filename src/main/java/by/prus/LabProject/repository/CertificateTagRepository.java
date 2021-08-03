package by.prus.LabProject.repository;

import by.prus.LabProject.model.entity.supporting.CertificateTag;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface CertificateTagRepository extends CrudRepository<CertificateTag, Long> {

    @Transactional
    @Modifying
    @Query(value="delete from certificate_tag ct where ct.tag_id = ?1", nativeQuery=true)
    void deleteAllConnectionsFromRemovingTag(Long tagId);

    @Transactional
    @Modifying
    @Query(value="delete from certificate_tag ct where ct.gift_certificate_id =: certificateId", nativeQuery=true)
    void deleteAllConnectionsFromRemovingCertificate(@Param("certificateId") Long certificateId);

}
