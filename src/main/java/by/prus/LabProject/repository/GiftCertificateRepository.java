package by.prus.LabProject.repository;

import by.prus.LabProject.model.entity.GiftCertificateEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface GiftCertificateRepository extends PagingAndSortingRepository<GiftCertificateEntity, Long> {

    /**
     * Clear all records in associated table certificate_tag in order to clean this certificate from others tags.
     * @param certificateId - id of certificate need to delete
     */
    @Transactional
    @Modifying
    @Query(value="delete ct.*, gc.* from certificate_tag ct LEFT JOIN  gift_certificate gc ON gc.id=ct.tag_id where ct.tag_id =?1", nativeQuery=true)
    void deleteCertificate(Long certificateId);

    @Query(value="select * from gift_certificate gc where name LIKE %:namePart%",nativeQuery=true)
    List<GiftCertificateEntity> findCertificatesByNamePart(@Param("namePart") String partOfName);
}
