package ftn.drustvenamreza_back.service;

import ftn.drustvenamreza_back.model.entity.Group;
import ftn.drustvenamreza_back.model.entity.Post;
import ftn.drustvenamreza_back.model.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PostService {
    Post createPost(Post post, User user, MultipartFile file) throws IOException;
    Post createGroupPost(Group group, Post post, User user);
    List<Post> getAllPostsWithoutGroup();
    List<Post> getAllPostsWithGroup(Long groupId);
    Post getPostById(Long postId);
    Post getPostByGroupId(Long groupId);
    void updatePost(Long postId, String updatedContent, User user);
    void updateGroupPost(Long groupId, Long postId, String updatedContent, User user);
    void deletePost(Long postId);
    void deleteGroupPost(Long groupId, Long postId);
}
