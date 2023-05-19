package ftn.drustvenamreza_back.service.implementation;

import ftn.drustvenamreza_back.model.entity.Post;
import ftn.drustvenamreza_back.model.entity.User;
import ftn.drustvenamreza_back.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class PostServiceImpl {
    private final PostRepository postRepository;

    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post createPost(String content, User postedBy) {
        Post post = new Post();
        post.setContent(content);
        post.setCreationDate(LocalDateTime.now());
        post.setIsDeleted(false);
        post.setPostedBy(postedBy);
        return postRepository.save(post);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post getPostById(Long postId) {
        return postRepository.findById(postId).orElse(null);
    }

    public void updatePost(Long postId, String updatedContent) {
        Post existingPost = getPostById(postId);
        if (existingPost != null) {
            existingPost.setContent(updatedContent);
            postRepository.save(existingPost);
        }
    }

    public void deletePost(Long postId) {
        Post post = getPostById(postId);
        if (post != null) {
            post.setIsDeleted(true);
            postRepository.save(post);
        }
    }

//    public Comment addCommentToPost(Long postId, String commentContent, User commentedBy) {
//        Post post = getPostById(postId);
//        if (post != null) {
//            Comment comment = new Comment();
//            comment.setText(commentContent);
//            comment.setUser(commentedBy);
//            comment.setPost(post);
//            post.getComments().add(comment);
//            return postRepository.save(post).getComments().stream()
//                    .filter(c -> c.getText().equals(commentContent) && c.getUser().equals(commentedBy))
//                    .findFirst()
//                    .orElse(null);
//        }
//        return null;
//    }
//
//    public void addImageToPost(Long postId, Image image) {
//        Post post = getPostById(postId);
//        if (post != null) {
//            image.setPost(post);
//            post.getImages().add(image);
//            postRepository.save(post);
//        }
//    }
}
