package ftn.drustvenamreza_back.service;

import ftn.drustvenamreza_back.model.entity.Post;
import ftn.drustvenamreza_back.model.entity.User;

import java.util.List;

public interface PostService {
    Post createPost(Post post, User user);
    List<Post> getAllPostsWithoutGroup();
    List<Post> getAllPostsWithGroup(Long groupId);
    Post getPostById(Long postId);
    void updatePost(Long postId, String updatedContent, User user);
    void deletePost(Long postId);
}
