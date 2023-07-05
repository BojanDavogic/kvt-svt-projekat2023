package ftn.drustvenamreza_back.repository;

import ftn.drustvenamreza_back.model.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostIdAndParentCommentIsNullAndIsDeletedFalse(Long postId);
    List<Comment> findByParentCommentAndIsDeletedFalse(Comment comment);
}
