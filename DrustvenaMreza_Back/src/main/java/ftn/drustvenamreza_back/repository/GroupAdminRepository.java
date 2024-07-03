package ftn.drustvenamreza_back.repository;

import ftn.drustvenamreza_back.model.entity.Group;
import ftn.drustvenamreza_back.model.entity.GroupAdmin;
import ftn.drustvenamreza_back.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupAdminRepository extends JpaRepository<GroupAdmin, Long> {
    List<GroupAdmin> findByGroupAndIsDeletedFalse(Group group);
    List<GroupAdmin> findByUserAndIsDeletedFalse(User user);
}
