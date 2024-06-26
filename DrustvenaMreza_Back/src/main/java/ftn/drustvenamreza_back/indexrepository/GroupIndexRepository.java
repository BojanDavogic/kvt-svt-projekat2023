package ftn.drustvenamreza_back.indexrepository;

import ftn.drustvenamreza_back.indexmodel.GroupIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface GroupIndexRepository extends ElasticsearchRepository<GroupIndex, String> {
}
