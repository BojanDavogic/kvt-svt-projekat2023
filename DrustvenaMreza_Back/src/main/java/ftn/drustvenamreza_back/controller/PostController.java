package ftn.drustvenamreza_back.controller;

import ftn.drustvenamreza_back.config.NotFoundException;
import ftn.drustvenamreza_back.indexmodel.PostIndex;
import ftn.drustvenamreza_back.indexservice.IndexingServiceImpl;
import ftn.drustvenamreza_back.indexservice.PostIndexService;
import ftn.drustvenamreza_back.indexservice.SearchServiceImpl;
import ftn.drustvenamreza_back.model.entity.*;
import ftn.drustvenamreza_back.service.implementation.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class PostController {
    private final PostServiceImpl postService;
    private final UserServiceImpl userService;
    private final CommentServiceImpl commentService;
    private final ReactionServiceImpl reactionService;
    private final GroupServiceImpl groupService;
    private final PostIndexService postIndexService;
    private final IndexingServiceImpl indexingService;
    private final SearchServiceImpl searchService;

    @PostMapping(value = "/posts", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Post> createPost(@RequestParam("file") MultipartFile file, @RequestPart("post") Post post) {
        try {
            User user = userService.getCurrentUser();
            Post createdPost = postService.createPost(post, user, file);
            indexingService.indexPost(file, createdPost.getId());
            return ResponseEntity.ok(createdPost);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @RequestMapping(value = "/groups/{groupId}/posts", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Post> createGroupPost(@RequestParam("file") MultipartFile file, @PathVariable Long groupId, @RequestPart("post") Post post) {
        try {
            User user = userService.getCurrentUser();
            Group group = groupService.getGroupById(groupId);
            if (group != null) {
                Post createdPost = postService.createGroupPost(group, post, user, file);
                return ResponseEntity.ok(createdPost);
            }
        }catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
        return null;
    }

    @GetMapping("/posts")
    public ResponseEntity<List<Post>> getAllPostsWithoutGroup() {
        List<Post> posts = postService.getAllPostsWithoutGroup();
        return ResponseEntity.ok(posts);
    }

    @RequestMapping(value = "/groups/{groupId}/posts", method = RequestMethod.GET)
    public ResponseEntity<List<Post>> getPostsByGroupId(@PathVariable Long groupId) {
        List<Post> posts = postService.getAllPostsWithGroup(groupId);
        return ResponseEntity.ok(posts);
    }


    @GetMapping("/posts/{postId}")
    public ResponseEntity<Post> getPostById(@PathVariable Long postId) {
        Post post = postService.getPostById(postId);
        if (post != null) {
            return ResponseEntity.ok(post);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/posts/{postId}")
    public ResponseEntity<Void> updatePost(@PathVariable Long postId, @RequestBody Post updatedPost) {
        User user = userService.getCurrentUser();
        postService.updatePost(postId, updatedPost.getTitle(), updatedPost.getContent(), user);
        return ResponseEntity.ok().build();
    }

//    @RequestMapping(value = "/groups/{groupId}/posts/{postId}", method = RequestMethod.PUT)
//    public ResponseEntity<Void> updateGroupPost(@PathVariable Long groupId,@PathVariable Long postId, @RequestBody String updatedContent) {
//        User user = userService.getCurrentUser();
//        postService.updateGroupPost(groupId, postId, updatedContent, user);
//        return ResponseEntity.ok().build();
//    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/groups/{groupId}/posts/{postId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteGroupPost(@PathVariable Long groupId, @PathVariable Long postId) {
        postService.deleteGroupPost(groupId, postId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<Comment> addCommentToPost(@PathVariable Long postId, @RequestBody Comment comment) {
        User user = userService.getCurrentUser();
        Comment createdComment = commentService.addCommentToPost(postId, comment, user);
        return ResponseEntity.ok(createdComment);
    }

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<Comment>> getCommentsForPost(@PathVariable Long postId) {
        List<Comment> comments = commentService.getCommentsForPost(postId);
        return ResponseEntity.ok(comments);
    }

    @PutMapping("/posts/comments/{commentId}")
    public ResponseEntity<Comment> updateComment(@PathVariable Long commentId, @RequestBody Comment updatedComment) {
        Comment comment = commentService.updateComment(commentId, updatedComment);
        if (comment != null) {
            return ResponseEntity.ok(comment);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/posts/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        Comment comment = commentService.getCommentById(commentId);
        if (comment == null) {
            return ResponseEntity.notFound().build();
        }
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/posts/{postId}/reactions")
    public ResponseEntity<Reaction> addReactionToPost(@PathVariable Long postId, @RequestBody String reactionRequest) {
        User currentUser = userService.getCurrentUser();
        Post post = postService.getPostById(postId);

        boolean userHasReaction = reactionService.hasUserReaction(postId, currentUser.getId());

        if (userHasReaction) {

            Reaction existingReaction = reactionService.getUserReaction(postId, currentUser.getId());

            existingReaction.setType(ReactionType.valueOf(reactionRequest));
            existingReaction.setTimestamp(LocalDate.now());
            Reaction updatedReaction = updateReaction(existingReaction.getId(), reactionRequest).getBody();
            return ResponseEntity.ok(updatedReaction);
        } else {

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


    @GetMapping("/posts/{postId}/reactions")
    public ResponseEntity<List<Reaction>> getReactionsForPost(@PathVariable Long postId) {
        List<Reaction> reactions = reactionService.getReactionsForPost(postId);
        return ResponseEntity.ok(reactions);
    }

    @PutMapping("/posts/reactions/{reactionId}")
    public ResponseEntity<Reaction> updateReaction(@PathVariable Long reactionId, @RequestBody String updatedReaction) {
        Reaction reaction = reactionService.updateReaction(reactionId, updatedReaction);
        if (reaction != null) {
            return ResponseEntity.ok(reaction);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/posts/reactions/{reactionId}")
    public ResponseEntity<Void> deleteReaction(@PathVariable Long reactionId) {
        Reaction reaction = reactionService.getReactionById(reactionId);
        if (reaction == null) {
            return ResponseEntity.notFound().build();
        }
        reactionService.deleteReaction(reactionId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/posts/comments/{commentId}/reactions")
    public ResponseEntity<Reaction> addReactionToComment(@PathVariable Long commentId, @RequestBody String reactionRequest) {
        User currentUser = userService.getCurrentUser();
        Comment comment = commentService.getCommentById(commentId);


        boolean userHasReaction = reactionService.hasUserCommentReaction(commentId, currentUser.getId());

        if (userHasReaction) {

            Reaction existingReaction = reactionService.getUserCommentReaction(commentId, currentUser.getId());

            existingReaction.setType(ReactionType.valueOf(reactionRequest));
            existingReaction.setTimestamp(LocalDate.now());
            Reaction updatedReaction = reactionService.updateCommentReaction(commentId, existingReaction.getId(), reactionRequest);
            return ResponseEntity.ok(updatedReaction);
        } else {

            Reaction reaction = new Reaction();
            ReactionType reactionType = ReactionType.valueOf(reactionRequest);

            reaction.setType(reactionType);
            reaction.setTimestamp(LocalDate.now());
            reaction.setMadeBy(currentUser);
            reaction.setComment(comment);

            Reaction createdReaction = reactionService.addReactionForComment(commentId, reaction, currentUser);
            return ResponseEntity.ok(createdReaction);
        }
    }

    @GetMapping("/posts/comments/{commentId}/reactions")
    public ResponseEntity<List<Reaction>> getReactionsForComment(@PathVariable Long commentId) {
        List<Reaction> reactions = reactionService.getReactionsForComment(commentId);
        return ResponseEntity.ok(reactions);
    }

    @PutMapping("/posts/comments/{commentId}/reactions/{reactionId}")
    public ResponseEntity<Reaction> updateCommentReaction(@PathVariable Long commentId, @PathVariable Long reactionId, @RequestBody String updatedReaction) {
        Reaction reaction = reactionService.updateCommentReaction(commentId, reactionId, updatedReaction);
        if (reaction != null) {
            return ResponseEntity.ok(reaction);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/posts/comments/{commentId}/reactions/{reactionId}")
    public ResponseEntity<Void> deleteCommentReaction(@PathVariable Long commentId, @PathVariable Long reactionId) {
        Reaction reaction = reactionService.getReactionById(reactionId);
        if (reaction == null) {
            return ResponseEntity.notFound().build();
        }
        reactionService.deleteCommentReaction(commentId, reactionId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/posts/comments/{commentId}/replies")
    public ResponseEntity<Comment> addReplyToComment(@PathVariable Long commentId, @RequestBody Comment reply) {
        User user = userService.getCurrentUser();
        Comment addedReply = commentService.addReplyToComment(commentId, reply, user);
        return ResponseEntity.ok(addedReply);
    }

    @GetMapping("/posts/comments/{commentId}/replies")
    public ResponseEntity<List<Comment>> getRepliesForComment(@PathVariable Long commentId) {
        List<Comment> replies = commentService.getRepliesForComment(commentId);
        return ResponseEntity.ok(replies);
    }

    @GetMapping("/posts/searchByTitle")
    public ResponseEntity<List<PostIndex>> searchPostsByTitle(@RequestParam String title) {
        List<PostIndex> posts = postIndexService.searchPostsByTitle(title);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/posts/searchByFullContent")
    public ResponseEntity<List<PostIndex>> searchPostsByFullContent(@RequestParam String content) {
        List<PostIndex> posts = postIndexService.searchPostsByFullContent(content);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/posts/searchByFileContent")
    public ResponseEntity<List<PostIndex>> searchPostsByFileContent(@RequestParam String fileContent) {
        List<PostIndex> posts = postIndexService.searchPostsByFileContent(fileContent);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/posts/searchByCommentContent")
    public ResponseEntity<List<PostIndex>> searchPostsByCommentContent(@RequestParam String commentContent) {
        List<PostIndex> posts = postIndexService.searchPostsByCommentContent(commentContent);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/posts/search/simple")
    public ResponseEntity<Page<PostIndex>> simpleSearch(@RequestParam String query, @RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<String> keywords = Arrays.asList(query.split("\\s+"));
        Page<PostIndex> posts = searchService.simpleSearch(keywords, pageable);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/posts/search/advanced")
    public ResponseEntity<Page<PostIndex>> advancedSearch(@RequestParam List<String> expression, @RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PostIndex> posts = searchService.advancedSearch(expression, pageable);
        return ResponseEntity.ok(posts);
    }
}
