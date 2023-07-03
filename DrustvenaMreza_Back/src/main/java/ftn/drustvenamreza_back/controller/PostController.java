package ftn.drustvenamreza_back.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ftn.drustvenamreza_back.model.entity.*;
import ftn.drustvenamreza_back.service.implementation.CommentServiceImpl;
import ftn.drustvenamreza_back.service.implementation.PostServiceImpl;
import ftn.drustvenamreza_back.service.implementation.ReactionServiceImpl;
import ftn.drustvenamreza_back.service.implementation.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostServiceImpl postService;
    private final UserServiceImpl userService;
    private final CommentServiceImpl commentService;
    private final ReactionServiceImpl reactionService;
    @Autowired
    private ObjectMapper objectMapper;

    public PostController(PostServiceImpl postService, UserServiceImpl userService, CommentServiceImpl commentService, ReactionServiceImpl reactionService) {
        this.postService = postService;
        this.userService = userService;
        this.commentService = commentService;
        this.reactionService = reactionService;
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

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<Comment> updateComment(@PathVariable Long commentId, @RequestBody Comment updatedComment) {
        Comment comment = commentService.updateComment(commentId, updatedComment);
        if (comment != null) {
            return ResponseEntity.ok(comment);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        Comment comment = commentService.getCommentById(commentId);
        if (comment == null) {
            return ResponseEntity.notFound().build();
        }
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{postId}/reactions")
    public ResponseEntity<Reaction> addReactionToPost(@PathVariable Long postId, @RequestBody String reactionRequest) {
        User currentUser = userService.getCurrentUser();
        Post post = postService.getPostById(postId);

        // Provera da li korisnik već ima reakciju na objavu
        boolean userHasReaction = reactionService.hasUserReaction(postId, currentUser.getId());

        if (userHasReaction) {
            // Korisnik već ima reakciju na objavu, treba je ažurirati
            Reaction existingReaction = reactionService.getUserReaction(postId, currentUser.getId());
            // Ažurirajte postojeću reakciju sa novom vrednošću
            existingReaction.setType(ReactionType.valueOf(reactionRequest));
            existingReaction.setTimestamp(LocalDate.now());
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
//            existingReaction.setTimestamp(LocalDate.parse("03.07.2023. 01:19:54", formatter));
//            updateReaction(existingReaction.getId(), existingReaction);
            Reaction updatedReaction = updateReaction(existingReaction.getId(), reactionRequest).getBody();
            return ResponseEntity.ok(updatedReaction);
        } else {
            // Korisnik nema prethodnu reakciju, treba dodati novu reakciju na objavu
            Reaction reaction = new Reaction();
            ReactionType reactionType = ReactionType.valueOf(reactionRequest);

            reaction.setType(reactionType);
            reaction.setTimestamp(LocalDate.now());
            reaction.setMadeBy(currentUser);
            reaction.setPost(post);

            Reaction createdReaction = reactionService.addReactionForPost(postId, reaction, currentUser);
            return ResponseEntity.ok(createdReaction);
        }
    }


    @GetMapping("/{postId}/reactions")
    public ResponseEntity<List<Reaction>> getReactionsForPost(@PathVariable Long postId) {
        List<Reaction> reactions = reactionService.getReactionsForPost(postId);
        return ResponseEntity.ok(reactions);
    }

    @PutMapping("/reactions/{reactionId}")
    public ResponseEntity<Reaction> updateReaction(@PathVariable Long reactionId, @RequestBody String updatedReaction) {
        Reaction reaction = reactionService.updateReaction(reactionId, updatedReaction);
        if (reaction != null) {
            return ResponseEntity.ok(reaction);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/reactions/{reactionId}")
    public ResponseEntity<Void> deleteReaction(@PathVariable Long reactionId) {
        Reaction reaction = reactionService.getReactionById(reactionId);
        if (reaction == null) {
            return ResponseEntity.notFound().build();
        }
        reactionService.deleteReaction(reactionId);
        return ResponseEntity.noContent().build();
    }
}
