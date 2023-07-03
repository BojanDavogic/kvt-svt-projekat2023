package ftn.drustvenamreza_back.service.implementation;

import ftn.drustvenamreza_back.model.entity.Comment;
import ftn.drustvenamreza_back.model.entity.Reaction;
import ftn.drustvenamreza_back.model.entity.ReactionType;
import ftn.drustvenamreza_back.model.entity.User;
import ftn.drustvenamreza_back.repository.ReactionRepository;
import ftn.drustvenamreza_back.service.ReactionService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReactionServiceImpl implements ReactionService {
    private final ReactionRepository reactionRepository;

    public ReactionServiceImpl(ReactionRepository reactionRepository) {
        this.reactionRepository = reactionRepository;
    }

    @Override
    public Reaction addReactionForPost(Long postId, Reaction reaction, User user) {
        return reactionRepository.save(reaction);
    }

    @Override
    public List<Reaction> getReactionsForPost(Long postId) {
        return reactionRepository.findByPostIdAndIsDeletedFalse(postId);
    }

    @Override
    public Reaction getReactionById(Long reactionId) {
        return reactionRepository.findById(reactionId).orElse(null);
    }

    @Override
    public Reaction updateReaction(Long reactionId, String updatedReaction) {
        Reaction reaction = getReactionById(reactionId);
        reaction.setType(ReactionType.valueOf(updatedReaction));
        return reactionRepository.save(reaction);
    }

    @Override
    public void deleteReaction(Long reactionId) {
        Reaction reaction = getReactionById(reactionId);
        if (reaction != null) {
            reaction.setIsDeleted(true);
            reactionRepository.save(reaction);
        }
    }

    @Override
    public boolean hasUserReaction(Long postId, Long userId) {
        return reactionRepository.existsByPostIdAndMadeByIdAndIsDeletedFalse(postId, userId);
    }

    @Override
    public Reaction getUserReaction(Long postId, Long userId) {
        return reactionRepository.findByPostIdAndMadeByIdAndIsDeletedFalse(postId, userId);
    }
}
