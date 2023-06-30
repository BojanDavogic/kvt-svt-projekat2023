package ftn.drustvenamreza_back.controller;

import ftn.drustvenamreza_back.model.entity.Comment;
import ftn.drustvenamreza_back.model.entity.Post;
import ftn.drustvenamreza_back.model.entity.User;
import ftn.drustvenamreza_back.service.CommentService;
import ftn.drustvenamreza_back.service.implementation.CommentServiceImpl;
import ftn.drustvenamreza_back.service.implementation.PostServiceImpl;
import ftn.drustvenamreza_back.service.implementation.UserServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostServiceImpl postService;
    private final UserServiceImpl userService;
    private final CommentServiceImpl commentService;

    public PostController(PostServiceImpl postService, UserServiceImpl userService, CommentServiceImpl commentService) {
        this.postService = postService;
        this.userService = userService;
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody Post post) {
        User user = userService.getCurrentUser();
        Post createdPost = postService.createPost(post, user);
        return ResponseEntity.ok(createdPost);
    }

    @GetMapping
    public ResponseEntity<List<Post>> getAllPostsWithoutGroup() {
        List<Post> posts = postService.getAllPostsWithoutGroup();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<Post>> getPostsByGroupId(@PathVariable Long groupId) {
        List<Post> posts = postService.getAllPostsWithGroup(groupId);
        return ResponseEntity.ok(posts);
    }


    @GetMapping("/post/{postId}")
    public ResponseEntity<Post> getPostById(@PathVariable Long postId) {
        Post post = postService.getPostById(postId);
        if (post != null) {
            return ResponseEntity.ok(post);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{postId}")
    public ResponseEntity<Void> updatePost(@PathVariable Long postId, @RequestBody String updatedContent) {
        User user = userService.getCurrentUser();
        postService.updatePost(postId, updatedContent, user);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{postId}/comments")
    public ResponseEntity<Comment> addCommentToPost(@PathVariable Long postId, @RequestBody Comment comment) {
        User user = userService.getCurrentUser();
        Comment createdComment = commentService.addCommentToPost(postId, comment, user);
        return ResponseEntity.ok(createdComment);
    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<Comment>> getCommentsForPost(@PathVariable Long postId) {
        List<Comment> comments = commentService.getCommentsForPost(postId);
        return ResponseEntity.ok(comments);
    }
}
