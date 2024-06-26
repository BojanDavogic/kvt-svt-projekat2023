package ftn.drustvenamreza_back.indexservice;

import ftn.drustvenamreza_back.indexmodel.PostIndex;
import ftn.drustvenamreza_back.indexrepository.PostIndexRepository;
import ftn.drustvenamreza_back.model.entity.Post;
import org.springframework.stereotype.Service;

@Service
public class PostIndexService {
    private final PostIndexRepository postIndexRepository;

    public PostIndexService(PostIndexRepository postIndexRepository) {
        this.postIndexRepository = postIndexRepository;
    }

    public void indexPost(Post post) {
        PostIndex postIndex = new PostIndex();
        postIndex.setId(post.getId().toString());
        postIndex.setContentSr(post.getContent());  // Assuming content is in Serbian, adjust as needed
        postIndex.setContentEn(post.getContent());  // Adjust as needed
        postIndex.setDatabaseId(post.getId().intValue());
        postIndexRepository.save(postIndex);
    }

    public void deletePostIndex(Long postId) {
        postIndexRepository.deleteById(postId.toString());
    }
}
