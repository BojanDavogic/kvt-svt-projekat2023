package ftn.drustvenamreza_back.service.implementation;

import ftn.drustvenamreza_back.config.NotFoundException;
import ftn.drustvenamreza_back.indexmodel.PostIndex;
import ftn.drustvenamreza_back.indexservice.PostIndexService;
import ftn.drustvenamreza_back.model.entity.Comment;
import ftn.drustvenamreza_back.model.entity.Post;
import ftn.drustvenamreza_back.model.entity.User;
import ftn.drustvenamreza_back.repository.CommentRepository;
import ftn.drustvenamreza_back.service.CommentService;
import ftn.drustvenamreza_back.service.PostService;
import org.elasticsearch.index.IndexService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final PostService postService;
    private final PostIndexService postIndexService;

    public CommentServiceImpl(CommentRepository commentRepository, PostService postService, PostIndexService postIndexService) {
        this.commentRepository = commentRepository;
        this.postService = postService;
        this.postIndexService = postIndexService;
    }
    @Override
    public Comment addCommentToPost(Long postId, Comment comment, User user) {
        Post post = postService.getPostById(postId);
        comment.setPost(post);
        comment.setUser(user);
        comment.setTimestamp(LocalDateTime.now());
        Comment commentSaved = commentRepository.save(comment);
        getCommentsForPost(postId);
        return commentSaved;
    }

    @Override
    public List<Comment> getCommentsForPost(Long postId) {
        List<Comment> comments = commentRepository.findByPostIdAndParentCommentIsNullAndIsDeletedFalse(postId);
        String commentContent = comments.stream().map(Comment::getText).collect(Collectors.joining(" "));
        Optional<PostIndex> existingPostIndexOptional = postIndexService.findById(postId.toString());
        if (existingPostIndexOptional.isPresent()) {
            PostIndex existingPostIndex = existingPostIndexOptional.get();

            existingPostIndex.setCommentContent(commentContent);

            postIndexService.updatePostIndex(existingPostIndex);
        }
        return comments;
    }

    @Override
    public Comment getCommentById(Long commentId) {
        return commentRepository.findById(commentId).orElse(null);
    }

    @Override
    public Comment updateComment(Long commentId, Comment updatedComment) {
        Comment comment = getCommentById(commentId);
        comment.setText(updatedComment.getText());
        comment.setTimestamp(LocalDateTime.now());
        return commentRepository.save(comment);
    }

    @Override
    public void deleteComment(Long commentId) {
        Comment comment = getCommentById(commentId);
        if (comment != null) {
            comment.setIsDeleted(true);
            commentRepository.save(comment);
        }
    }

    @Override
    public Comment addReplyToComment(Long commentId, Comment reply, User user) {
        Comment parentComment = getCommentById(commentId);
        if (parentComment == null) {
            throw new NotFoundException("Komentar nije pronađen.");
        }

        Post post = parentComment.getPost();
        if (post == null) {
            throw new NotFoundException("Post nije pronađen.");
        }

        reply.setUser(user);
        reply.setPost(post);
        reply.setParentComment(parentComment);
        reply.setTimestamp(LocalDateTime.now());

        return commentRepository.save(reply);
    }

    @Override
    public List<Comment> getRepliesForComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Komentar nije pronađen."));

        return commentRepository.findByParentCommentAndIsDeletedFalse(comment);
    }

}
