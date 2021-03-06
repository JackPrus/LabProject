package by.prus.LabProject.repository;

import by.prus.LabProject.model.entity.GiftCertificateEntity;
import by.prus.LabProject.model.entity.TagEntity;
import org.springframework.data.domain.Page;
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
public interface TagRepository extends PagingAndSortingRepository<TagEntity, Long> {

    /**
     * Clear all records in associated table certificate_tag in order to clean this tag from others certificates.
     * @param tagId - id of tag need to delete
     *              Translation of native Query:
     *              delete from table certificate_tag where tag_id=?1, if table tag has id = tag_id of certificate_tag
     *              delete tagt raw of tag table
     */

    @Transactional
    @Modifying
    @Query(value="delete ct.*, t.* from certificate_tag ct LEFT JOIN  tag t ON t.id=ct.tag_id where ct.tag_id =?1", nativeQuery=true)
    void deleteTag(Long tagId);

    @Query(value="select * from tag t where name LIKE %:namePart%",nativeQuery=true)
    List<TagEntity> findTagsByNamePart(@Param("namePart") String partOfName);

    @Query(value="select * from tag t where name LIKE %:namePart%",nativeQuery=true)
    Page<TagEntity> findTagsByNamePartAbdReturnPage(@Param("namePart") String partOfName, Pageable pageAble);

}
