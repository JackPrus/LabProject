package by.prus.LabProject.repository;

import by.prus.LabProject.model.entity.GiftCertificateEntity;
import by.prus.LabProject.model.entity.TagEntity;
import org.springframework.data.domain.Pageable;
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
     * We need to use CascadeType.ALL betwen GiftCertificate and CertificateTag objects.
     * The reason of that is cascade delete and create of every binded parts. When we create
     * a GiftCertificate, CertificateTag list create as well. Pointing tag to certificate we
     * prowide network betwen certificate and tag by this CertificateTag object.
     * But Deleting tag we will delete certificate binded to this tag with CertificateTag object.
     * Because of CascadeType.All. If we change CascadeType to 'PERSIST' for example and try to
     * delete some tag or certificate it will not be deleted because of CertificateTag object save
     * information about binding parts and restore deleted object.
     * In our case we need to delete only certificate and information about it in middle object
     * CertificateTag. Native query below allow us to do it. The tag binded to deleted certificate
     * will stay in our database.
     * @param certificateId - the ID of certificate need to delete
     */
    @Transactional
    @Modifying
    @Query(value="delete ct.*, gc.* from certificate_tag ct LEFT JOIN  gift_certificate gc ON gc.id=ct.tag_id where ct.tag_id =?1", nativeQuery=true)
    void deleteCertificate(Long certificateId);

    @Query(value="select * from gift_certificate gc  where name LIKE %:namePart%",nativeQuery=true)
    List<GiftCertificateEntity> findCertificatesByNamePart(@Param("namePart") String partOfName);

    @Query(value="select * from gift_certificate  where name LIKE %:namePart%",nativeQuery=true)
    List<GiftCertificateEntity> findCertificatesByNamePartAndReturnPage(@Param("namePart") String partOfName, Pageable pageAble);
}
