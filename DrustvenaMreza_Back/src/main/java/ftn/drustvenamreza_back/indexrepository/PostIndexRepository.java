package ftn.drustvenamreza_back.indexrepository;

import ftn.drustvenamreza_back.indexmodel.PostIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostIndexRepository extends ElasticsearchRepository<PostIndex, String> {
    Optional<PostIndex> findPostIndexById(String postId);
}
