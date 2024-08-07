package ftn.drustvenamreza_back.repository;

import ftn.drustvenamreza_back.model.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    List<Group> findGroupByIsDeletedFalse();
}
