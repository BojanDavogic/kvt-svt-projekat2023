package ftn.drustvenamreza_back.service.implementation;

import ftn.drustvenamreza_back.model.entity.Comment;
import ftn.drustvenamreza_back.model.entity.Post;
import ftn.drustvenamreza_back.model.entity.User;
import ftn.drustvenamreza_back.repository.CommentRepository;
import ftn.drustvenamreza_back.repository.PostRepository;
import ftn.drustvenamreza_back.service.CommentService;
import ftn.drustvenamreza_back.service.PostService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final PostService postService;

    public CommentServiceImpl(CommentRepository commentRepository, PostService postService) {
        this.commentRepository = commentRepository;
        this.postService = postService;
    }
    @Override
    public Comment addCommentToPost(Long postId, Comment comment, User user) {
        Post post = postService.getPostById(postId);
        comment.setPost(post);
        comment.setUser(user);
        comment.setTimestamp(LocalDateTime.now());
        return commentRepository.save(comment);
    }

    @Override
    public List<Comment> getCommentsForPost(Long postId) {
        return commentRepository.findByPostIdAndIsDeletedFalse(postId);
    }

    @Override
    public Comment getCommentById(Long commentId) {
        return commentRepository.findById(commentId).orElse(null);
    }

    @Override
    public void updateComment(Long commentId, String updatedText) {
        Comment comment = getCommentById(commentId);
        comment.setText(updatedText);
        comment.setTimestamp(LocalDateTime.now());
        commentRepository.save(comment);
    }

    @Override
    public void deleteComment(Long commentId) {
        Comment comment = getCommentById(commentId);
        if (comment != null) {
            comment.setIsDeleted(true);
            commentRepository.save(comment);
        }
    }
}
