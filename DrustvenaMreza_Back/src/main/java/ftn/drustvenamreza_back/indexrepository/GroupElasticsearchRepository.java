package ftn.drustvenamreza_back.indexrepository;

import ftn.drustvenamreza_back.indexrepository.GroupElasticsearch;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface GroupElasticsearchRepository extends ElasticsearchRepository<GroupElasticsearch, Long> {
    List<GroupElasticsearch> findByNameContainingIgnoreCase(String name);
    List<GroupElasticsearch> findByDescriptionContainingIgnoreCase(String description);
}
