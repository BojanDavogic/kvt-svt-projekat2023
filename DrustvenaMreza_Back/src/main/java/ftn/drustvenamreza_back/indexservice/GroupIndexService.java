package ftn.drustvenamreza_back.indexservice;

import ftn.drustvenamreza_back.indexmodel.GroupIndex;
import ftn.drustvenamreza_back.indexrepository.GroupIndexRepository;
import ftn.drustvenamreza_back.model.entity.Reaction;
import ftn.drustvenamreza_back.service.PostService;
import ftn.drustvenamreza_back.service.implementation.PostServiceImpl;
import ftn.drustvenamreza_back.service.implementation.ReactionServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
@RequiredArgsConstructor
@Service
public class GroupIndexService {
    private final GroupIndexRepository groupIndexRepository;
    private final PostServiceImpl postService;
    private final ReactionServiceImpl reactionService;

    public void indexGroup(GroupIndex groupIndex) {
        groupIndexRepository.save(groupIndex);
    }

    public void deleteGroupIndex(Long groupId) {
        groupIndexRepository.deleteById(groupId.toString());
    }

    public Optional<GroupIndex> findById(String groupId) {
        Optional<GroupIndex> findedGroup = groupIndexRepository.findGroupIndexById(groupId);
        return findedGroup;
    }

    public Long calculateNumberOfPosts(Long groupId) {
        return postService.getAllPostsWithGroup(groupId).stream().count();
    }

    public Double calculateAverageLikes(Long groupId) {
        List<Reaction> reactions = reactionService.getReactionsForGroup(groupId);
        Long totalLikes = reactions.stream().filter(reaction -> reaction.getType().toString().equals("LIKE")).count();
        Long totalDislikesCount = reactions.stream().filter(reaction -> reaction.getType().toString().equals("DISLIKE")).count();
        Long totalDislikes = totalDislikesCount * (-1);
        Long totalHeartsCount = reactions.stream().filter(reaction -> reaction.getType().toString().equals("HEART")).count();
        Long totalHearts = totalHeartsCount * 5;
        Long totalReactions = totalLikes + totalDislikes + totalHearts;
        Long numberOfPosts = calculateNumberOfPosts(groupId);
        double avg = numberOfPosts > 0 ? (double) totalReactions / numberOfPosts : 0.0;
        BigDecimal bd = BigDecimal.valueOf(avg);
        bd = bd.setScale(2, RoundingMode.HALF_UP);

        return bd.doubleValue();
    }

    public void updateGroupIndex(GroupIndex groupIndex) {
        Optional<GroupIndex> existingGroupIndexOptional = groupIndexRepository.findById(groupIndex.getId().toString());

        if (existingGroupIndexOptional.isPresent()) {
            GroupIndex existingGroupIndex = existingGroupIndexOptional.get();

            existingGroupIndex.setName(groupIndex.getName());
            existingGroupIndex.setDescription(groupIndex.getDescription());
            existingGroupIndex.setFileContent(groupIndex.getFileContent());
            existingGroupIndex.setNumberOfPosts(groupIndex.getNumberOfPosts());
            existingGroupIndex.setRules(groupIndex.getRules());
            existingGroupIndex.setAverageLikes(groupIndex.getAverageLikes());

            groupIndexRepository.save(existingGroupIndex);
        } else {
            groupIndexRepository.save(groupIndex);
        }
    }

    public void updateGroupIndexStatistics(Long groupId) {
        Long numberOfPosts = calculateNumberOfPosts(groupId);
        Double averageLikes = calculateAverageLikes(groupId);

        Optional<GroupIndex> groupIndexOptional = findById(groupId.toString());
        if (groupIndexOptional.isPresent()) {
            GroupIndex groupIndex = groupIndexOptional.get();
            groupIndex.setNumberOfPosts(numberOfPosts);
            groupIndex.setAverageLikes(averageLikes);
            updateGroupIndex(groupIndex);
        }
    }
}
