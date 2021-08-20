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

@Repository
public interface CertificateTagRepository extends PagingAndSortingRepository<CertificateTag, Long> {

}
