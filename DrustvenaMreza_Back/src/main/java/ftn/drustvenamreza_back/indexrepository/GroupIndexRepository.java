package ftn.drustvenamreza_back.indexrepository;

import ftn.drustvenamreza_back.indexmodel.GroupIndex;
import ftn.drustvenamreza_back.indexmodel.PostIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface GroupIndexRepository extends ElasticsearchRepository<GroupIndex, String> {
    Optional<GroupIndex> findGroupIndexById(String groupId);
}
