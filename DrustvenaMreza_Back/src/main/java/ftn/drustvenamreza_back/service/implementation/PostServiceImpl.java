package ftn.drustvenamreza_back.service.implementation;

import ftn.drustvenamreza_back.indexmodel.PostIndex;
import ftn.drustvenamreza_back.indexservice.GroupIndexService;
import ftn.drustvenamreza_back.indexservice.PostIndexService;
import ftn.drustvenamreza_back.model.entity.*;
import ftn.drustvenamreza_back.repository.CommentRepository;
import ftn.drustvenamreza_back.repository.PostRepository;
import ftn.drustvenamreza_back.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService {
    private static final int MAX_TITLE_LENGTH = 50;
    private static final int MAX_CONTENT_LENGTH = 255;
    private final PostRepository postRepository;
    private final UserServiceImpl userService;
    private final CommentRepository commentRepository;
    private final PostIndexService postIndexService;
    private final MinioServiceImpl minioService;

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
        postIndex.setTitle(post.getTitle());
        postIndex.setFullContent(post.getContent());
        postIndex.setFileContent(fileContent);
        postIndex.setNumberOfLikes(0L);
        postIndex.setCommentContent("");
        postIndexService.indexPost(postIndex);
        return savedPost;
    }

    public Post createGroupPost(Group group, Post post, User user, MultipartFile file) throws IOException {
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
        post.setGroup(group);
        Post savedPost = postRepository.save(post);
        PostIndex postIndex = new PostIndex();
        postIndex.setId(post.getId());
        postIndex.setTitle(post.getTitle());
        postIndex.setFullContent(post.getContent());
        postIndex.setFileContent(fileContent);
        postIndex.setNumberOfLikes(0L);
        postIndex.setCommentContent("");
        postIndexService.indexPost(postIndex);

//        groupIndexService.updateGroupIndexStatistics(group.getId());

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

    public void updatePost(Long postId, String updatedTitle, String updatedContent, User user) {
        Post existingPost = getPostById(postId);
        if (existingPost != null) {
            if (updatedContent.length() > MAX_CONTENT_LENGTH) {
                throw new IllegalArgumentException("Duzina sadržaja prelazi maksimalnu duzinu.");
            }
            if (updatedTitle.length() > MAX_CONTENT_LENGTH) {
                throw new IllegalArgumentException("Duzina sadržaja prelazi maksimalnu duzinu.");
            }
            existingPost.setTitle(updatedTitle);
            existingPost.setContent(updatedContent);
            existingPost.setPostedBy(user);
            postRepository.save(existingPost);

            Optional<PostIndex> existingPostIndexOptional = postIndexService.findById(postId.toString());
            if (existingPostIndexOptional.isPresent()) {
                PostIndex existingPostIndex = existingPostIndexOptional.get();

                existingPostIndex.setTitle(existingPost.getTitle());
                existingPostIndex.setFullContent(existingPost.getContent());

                postIndexService.updatePostIndex(existingPostIndex);
            }
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
