package ftn.drustvenamreza_back.indexservice;

import ftn.drustvenamreza_back.indexmodel.PostIndex;
import ftn.drustvenamreza_back.indexrepository.PostIndexRepository;
import ftn.drustvenamreza_back.model.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@RequiredArgsConstructor
@Service
public class PostIndexService {
    private final PostIndexRepository postIndexRepository;
    private final ElasticsearchOperations elasticsearchTemplate;

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

    public List<PostIndex> searchPostsByTitle(String title) {
        return postIndexRepository.findByTitleContaining(title);
    }

    public List<PostIndex> searchPostsByFullContent(String content) {
        return postIndexRepository.findByFullContentContaining(content);
    }

    public List<PostIndex> searchPostsByFileContent(String fileContent) {
        return postIndexRepository.findByFileContentContaining(fileContent);
    }

    public List<PostIndex> searchPostsByCommentContent(String commentContent) {
        return postIndexRepository.findByCommentContentContaining(commentContent);
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
