package ftn.drustvenamreza_back.service;

import ftn.drustvenamreza_back.model.entity.Reaction;
import ftn.drustvenamreza_back.model.entity.User;

import java.util.List;

public interface ReactionService {
    Reaction addReactionForPost(Long postId, Reaction reaction, User user);
    List<Reaction> getReactionsForPost(Long postId);
    Reaction getReactionById(Long reactionId);
    Reaction updateReaction(Long reactionId, String updatedReaction);
    void deleteReaction(Long reactionId);
    boolean hasUserReaction(Long postId, Long userId);
    Reaction getUserReaction(Long postId, Long userId);
}
