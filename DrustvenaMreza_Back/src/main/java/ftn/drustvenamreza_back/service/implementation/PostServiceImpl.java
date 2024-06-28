package ftn.drustvenamreza_back.service.implementation;

import ftn.drustvenamreza_back.indexmodel.PostIndex;
import ftn.drustvenamreza_back.indexrepository.PostIndexRepository;
import ftn.drustvenamreza_back.indexservice.PostIndexService;
import ftn.drustvenamreza_back.model.entity.*;
import ftn.drustvenamreza_back.repository.CommentRepository;
import ftn.drustvenamreza_back.repository.PostRepository;
import ftn.drustvenamreza_back.service.PostService;
import io.minio.MinioClient;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    private static final int MAX_CONTENT_LENGTH = 255;
    private final PostRepository postRepository;
    private final UserServiceImpl userService;
    private final CommentRepository commentRepository;
    private final PostIndexService postIndexService;
    private final MinioServiceImpl minioService;

    public PostServiceImpl(PostRepository postRepository, UserServiceImpl userService, CommentRepository commentRepository, PostIndexService postIndexService, MinioServiceImpl minioService) {
        this.postRepository = postRepository;
        this.userService = userService;
        this.commentRepository = commentRepository;
        this.postIndexService = postIndexService;
        this.minioService = minioService;
    }

    public Post createPost(Post post, User user, MultipartFile file) throws IOException {
        if (user == null) {
            user = userService.getCurrentUser();
        }
        post.setCreationDate(LocalDateTime.now());
        post.setPostedBy(user);
        String fileContent = "";
        if (file != null && !file.isEmpty()) {
            String fileName = minioService.uploadFile(file);
            post.setFileName(fileName);
            fileContent = new String(file.getBytes(), StandardCharsets.UTF_8);
        }

        Post savedPost = postRepository.save(post);
        PostIndex postIndex = new PostIndex();
        postIndex.setId(post.getId());
        postIndex.setTitle(post.getContent());
        postIndex.setFullContent(post.getContent());
        postIndex.setFileContent(fileContent);
        postIndex.setNumberOfLikes(0L);
        postIndex.setCommentContent("");
//        postIndex.setNumberOfLikes(calculateLikes(post.getReactions()));
//        postIndex.setCommentContent(post.getComments().stream()
//                .map(Comment::getText)
//                .collect(Collectors.joining(" ")));

        postIndexService.indexPost(postIndex);
        return savedPost;
    }

//    private Long calculateLikes(List<Reaction> reactions) {
//        return reactions.stream()
//                .mapToLong(reaction -> {
//                    switch (reaction.getType()) {
//                        case LIKE: return 1;
//                        case DISLIKE: return -1;
//                        case HEART: return 5;
//                        default: return 0;
//                    }
//                })
//                .sum();
//    }

    @Override
    public Post createGroupPost(Group group, Post post, User user) {
        if (user == null) {
            user = userService.getCurrentUser();
        }
        post.setCreationDate(LocalDateTime.now());
        post.setPostedBy(user);
        post.setGroup(group);
        Post savedPost = postRepository.save(post);
//        postIndexService.indexPost(savedPost);
        return savedPost;
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
//            postIndexService.indexPost(existingPost);
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
            postIndexService.deletePostIndex(postId);
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
