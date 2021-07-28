package by.prus.LabProject.repository;

import by.prus.LabProject.model.entity.TagEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends CrudRepository<TagEntity, Long> {
}
