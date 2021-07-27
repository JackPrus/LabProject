package by.prus.LabProject.repository;

import by.prus.LabProject.model.entity.GiftCertificateEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GiftCertificateRepository extends CrudRepository<GiftCertificateEntity, Long> {
}
