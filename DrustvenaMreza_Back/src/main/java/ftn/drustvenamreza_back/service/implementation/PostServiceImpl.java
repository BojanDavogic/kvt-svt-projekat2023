package ftn.drustvenamreza_back.service.implementation;

import ftn.drustvenamreza_back.model.entity.Comment;
import ftn.drustvenamreza_back.model.entity.Group;
import ftn.drustvenamreza_back.model.entity.Post;
import ftn.drustvenamreza_back.model.entity.User;
import ftn.drustvenamreza_back.repository.CommentRepository;
import ftn.drustvenamreza_back.repository.PostRepository;
import ftn.drustvenamreza_back.service.PostService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class PostServiceImpl implements PostService {
    private static final int MAX_CONTENT_LENGTH = 255;
    private final PostRepository postRepository;
    private final UserServiceImpl userService;
    private final CommentRepository commentRepository;

    public PostServiceImpl(PostRepository postRepository, UserServiceImpl userService, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.userService = userService;
        this.commentRepository = commentRepository;
    }

    public Post createPost(Post post, User user) {
        if (user == null) {
            user = userService.getCurrentUser();
        }
        post.setCreationDate(LocalDateTime.now());
        post.setPostedBy(user);
        return postRepository.save(post);
    }

    @Override
    public Post createGroupPost(Group group, Post post, User user) {
        if (user == null) {
            user = userService.getCurrentUser();
        }
        post.setCreationDate(LocalDateTime.now());
        post.setPostedBy(user);
        post.setGroup(group);
        return postRepository.save(post);
    }

    public List<Post> getAllPostsWithoutGroup() {
        return postRepository.findByGroupIdIsNullAndIsDeletedFalse();
    }

    @Override
    public List<Post> getAllPostsWithGroup(Long groupId) {
        return postRepository.findByGroupIdAndIsDeletedFalse(groupId);
    }

    public Post getPostById(Long postId) {
        return postRepository.findById(postId).orElse(null);
    }

    @Override
    public Post getPostByGroupId(Long groupId) {
        return (Post) postRepository.findByGroupIdAndIsDeletedFalse(groupId);
    }

    public void updatePost(Long postId, String updatedContent, User user) {
        Post existingPost = getPostById(postId);
        if (existingPost != null) {
            if (updatedContent.length() > MAX_CONTENT_LENGTH) {
                throw new IllegalArgumentException("Duzina sadržaja prelazi maksimalnu duzinu.");
            }
            existingPost.setContent(updatedContent);
            existingPost.setPostedBy(user);
            postRepository.save(existingPost);
        }
    }

    @Override
    public void updateGroupPost(Long groupId, Long postId, String updatedContent, User user) {

    }


    public void deletePost(Long postId) {
        Post post = getPostById(postId);
        if (post != null) {
            post.setIsDeleted(true);
            postRepository.save(post);
        }
    }

    @Override
    public void deleteGroupPost(Long groupId, Long postId) {

    }

//    public void addImageToPost(Long postId, Image image) {
//        Post post = getPostById(postId);
//        if (post != null) {
//            image.setPost(post);
//            post.getImages().add(image);
//            postRepository.save(post);
//        }
//    }
}
