package ftn.drustvenamreza_back.service.implementation;

import ftn.drustvenamreza_back.indexmodel.PostIndex;
import ftn.drustvenamreza_back.indexservice.GroupIndexService;
import ftn.drustvenamreza_back.indexservice.PostIndexService;
import ftn.drustvenamreza_back.model.entity.*;
import ftn.drustvenamreza_back.repository.ReactionRepository;
import ftn.drustvenamreza_back.service.ReactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@RequiredArgsConstructor
@Service
public class ReactionServiceImpl implements ReactionService {
    private final ReactionRepository reactionRepository;
    private final CommentServiceImpl commentService;
    private final PostIndexService postIndexService;
    private final PostServiceImpl postService;

    @Override
    public Reaction addReactionForPost(Long postId, Reaction reaction, User user) {
        Reaction addedReaction = reactionRepository.save(reaction);
        getReactionsForPost(postId);
        return addedReaction;
    }

    public Long calculateLikes(List<Reaction> reactions) {
        return reactions.stream()
                .mapToLong(reaction -> {
                    switch (reaction.getType()) {
                        case LIKE: return 1;
                        case DISLIKE: return -1;
                        case HEART: return 5;
                        default: return 0;
                    }
                })
                .sum();
    }

    @Override
    public Reaction addReactionForComment(Long commentId, Reaction reaction, User user) {
        return reactionRepository.save(reaction);
    }

    @Override
    public List<Reaction> getReactionsForPost(Long postId) {
        List<Reaction> reactions =  reactionRepository.findByPostIdAndIsDeletedFalse(postId);
        Long calculatedLikes = calculateLikes(reactions);
        Optional<PostIndex> existingPostIndexOptional = postIndexService.findById(postId.toString());
        if (existingPostIndexOptional.isPresent()) {
            PostIndex existingPostIndex = existingPostIndexOptional.get();

            existingPostIndex.setNumberOfLikes(calculatedLikes);

            postIndexService.updatePostIndex(existingPostIndex);
        }

        return reactions;
    }

    public List<Reaction> getReactionsForGroup(Long groupId) {
        List<Post> posts = postService.getAllPostsWithGroup(groupId);
        List<Reaction> allReactions = new ArrayList<>();
        for (Post post : posts) {
            allReactions.addAll(getReactionsForPost(post.getId()));
        }
        return allReactions;
    }

    @Override
    public List<Reaction> getReactionsForComment(Long commentId) {
        return reactionRepository.findByCommentIdAndIsDeletedFalse(commentId);
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
    public Reaction updateCommentReaction(Long commentId, Long reactionId, String updatedReaction) {
        Comment comment = commentService.getCommentById(commentId);
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
    public void deleteCommentReaction(Long commentId, Long reactionId) {
        Comment comment = commentService.getCommentById(commentId);
        Reaction reaction = getReactionById(reactionId);
        if(reaction != null) {
            reaction.setIsDeleted(true);
            reactionRepository.save(reaction);
        }
    }

    @Override
    public boolean hasUserReaction(Long postId, Long userId) {
        return reactionRepository.existsByPostIdAndMadeByIdAndIsDeletedFalse(postId, userId);
    }

    @Override
    public boolean hasUserCommentReaction(Long commentId, Long userId) {
        return reactionRepository.existsByCommentIdAndMadeByIdAndIsDeletedFalse(commentId, userId);
    }

    @Override
    public Reaction getUserReaction(Long postId, Long userId) {
        return reactionRepository.findByPostIdAndMadeByIdAndIsDeletedFalse(postId, userId);
    }

    @Override
    public Reaction getUserCommentReaction(Long commentId, Long userId) {
        return reactionRepository.findByCommentIdAndMadeByIdAndIsDeletedFalse(commentId, userId);
    }
}
