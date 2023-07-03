package ftn.drustvenamreza_back.service;

import ftn.drustvenamreza_back.model.entity.Reaction;
import ftn.drustvenamreza_back.model.entity.User;

import java.util.List;

public interface ReactionService {
    Reaction addReactionForPost(Long postId, Reaction reaction, User user);
    Reaction addReactionForComment(Long commentId, Reaction reaction, User user);
    List<Reaction> getReactionsForPost(Long postId);
    List<Reaction> getReactionsForComment(Long commentId);
    Reaction getReactionById(Long reactionId);
    Reaction updateReaction(Long reactionId, String updatedReaction);
    Reaction updateCommentReaction(Long commentId, Long reactionId, String updatedReaction);
    void deleteReaction(Long reactionId);
    void deleteCommentReaction(Long commentId, Long reactionId);
    boolean hasUserReaction(Long postId, Long userId);
    boolean hasUserCommentReaction(Long commentId, Long userId);
    Reaction getUserReaction(Long postId, Long userId);
    Reaction getUserCommentReaction(Long commentId, Long userId);
}
