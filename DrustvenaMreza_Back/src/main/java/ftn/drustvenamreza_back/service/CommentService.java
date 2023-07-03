package ftn.drustvenamreza_back.service;

import ftn.drustvenamreza_back.model.entity.Comment;
import ftn.drustvenamreza_back.model.entity.Post;
import ftn.drustvenamreza_back.model.entity.User;

import java.util.List;

public interface CommentService {
    Comment addCommentToPost(Long postId, Comment comment, User user);
    List<Comment> getCommentsForPost(Long postId);
    Comment getCommentById(Long commentId);
    Comment updateComment(Long commentId, Comment updatedComment);
    void deleteComment(Long commentId);
}
