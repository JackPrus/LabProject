package by.prus.LabProject.repository;

import by.prus.LabProject.model.entity.UserEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {

    UserEntity findByEmail(String email); // магия спринга. Найдёт есть ли такой email
    UserEntity findByUserId(String userId);
    UserEntity findByEmailVerificationToken(String token);

}
