package by.prus.LabProject.repository;

import by.prus.LabProject.model.entity.PasswordResetTokenEntity;
import org.springframework.data.repository.CrudRepository;

public interface PasswordResetTokenRepository extends CrudRepository<PasswordResetTokenEntity, Long> {

    PasswordResetTokenEntity findByToken(String token);
}
