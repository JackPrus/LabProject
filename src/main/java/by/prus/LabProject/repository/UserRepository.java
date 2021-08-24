package by.prus.LabProject.repository;

import by.prus.LabProject.model.entity.TagEntity;
import by.prus.LabProject.model.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {

    UserEntity findByEmail(String email); // магия спринга. Найдёт есть ли такой email
    UserEntity findByUserId(String userId);
    UserEntity findByEmailVerificationToken(String token);


    @Query(value="select * from certificate.user u where u.email like %:namePart% order by u.email" ,nativeQuery=true)
    Page<UserEntity> findTagsByEmailPartAndReturnPage(@Param("namePart") String partOfName, Pageable pageAble);

    @Query(value = "select * from certificate.user u order by u.email", nativeQuery = true)
    Page<UserEntity> findAllAndSortByEmail(Pageable pageableRequest);

    @Query(value="select * from certificate.user u where u.email_verification_status = 'true'",
            countQuery="select count(*) from certificate.user u where u.email_verification_status = 'true'",
            nativeQuery = true)
    Page<UserEntity> findAllUsersWithConfirmedEmailAddress( Pageable pageableRequest );
// иметь ввиду что есть java awt pageable, а нам нужен именно org.springframework pageable

}
