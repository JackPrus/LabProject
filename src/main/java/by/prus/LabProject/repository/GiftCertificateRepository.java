package by.prus.LabProject.repository;

import by.prus.LabProject.model.entity.GiftCertificate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GiftCertificateRepository extends CrudRepository<GiftCertificate, Long> {
}
