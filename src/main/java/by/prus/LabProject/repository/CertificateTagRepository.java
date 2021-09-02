package by.prus.LabProject.repository;

import by.prus.LabProject.model.entity.supporting.CertificateTag;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * The repository created for CertificateTag entity. The object CertificateTag used for
 * storage of networks betwen certificate and tag.
 * @ManyToMany connection betwen certificate and tag is implemented as 2 @OneToMany connections
 * so and CertificateTag object is the middle object betwen them
 */
@Repository
public interface CertificateTagRepository extends PagingAndSortingRepository<CertificateTag, Long> {

}
