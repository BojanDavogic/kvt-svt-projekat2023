package ftn.drustvenamreza_back.repository;
import ftn.drustvenamreza_back.model.entity.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Long> {
    List<Reaction> findByPostIdAndIsDeletedFalse(Long postId);
    List<Reaction> findByCommentIdAndIsDeletedFalse(Long commentId);
    boolean existsByPostIdAndMadeByIdAndIsDeletedFalse(Long postId, Long userId);
    boolean existsByCommentIdAndMadeByIdAndIsDeletedFalse(Long commentId, Long userId);

    Reaction findByPostIdAndMadeByIdAndIsDeletedFalse(Long postId, Long userId);
    Reaction findByCommentIdAndMadeByIdAndIsDeletedFalse(Long commentId, Long userId);
}
