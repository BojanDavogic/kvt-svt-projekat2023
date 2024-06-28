package ftn.drustvenamreza_back.indexservice;

import ftn.drustvenamreza_back.indexmodel.PostIndex;
import ftn.drustvenamreza_back.indexrepository.PostIndexRepository;
import ftn.drustvenamreza_back.model.entity.Post;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PostIndexService {
    private final PostIndexRepository postIndexRepository;

    public PostIndexService(PostIndexRepository postIndexRepository) {
        this.postIndexRepository = postIndexRepository;
    }

    public void indexPost(PostIndex postIndex) {
        postIndexRepository.save(postIndex);
    }

    public void deletePostIndex(Long postId) {
        postIndexRepository.deleteById(postId.toString());
    }

    public Optional<PostIndex> findById(String postId) {
        Optional<PostIndex> findedPost = postIndexRepository.findPostIndexById(postId);
        return findedPost;
    }

    public void updatePostIndex(PostIndex postIndex) {
        Optional<PostIndex> existingPostIndexOptional = postIndexRepository.findById(postIndex.getId().toString());

        if (existingPostIndexOptional.isPresent()) {
            PostIndex existingPostIndex = existingPostIndexOptional.get();

            existingPostIndex.setTitle(postIndex.getTitle());
            existingPostIndex.setFullContent(postIndex.getFullContent());
            existingPostIndex.setFileContent(postIndex.getFileContent());
            existingPostIndex.setNumberOfLikes(postIndex.getNumberOfLikes());
            existingPostIndex.setCommentContent(postIndex.getCommentContent());

            postIndexRepository.save(existingPostIndex);
        } else {
            postIndexRepository.save(postIndex);
        }
    }
}
