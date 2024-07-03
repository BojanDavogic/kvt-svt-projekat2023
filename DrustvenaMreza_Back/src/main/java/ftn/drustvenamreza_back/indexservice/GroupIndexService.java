package ftn.drustvenamreza_back.indexservice;

import ftn.drustvenamreza_back.indexmodel.GroupIndex;
import ftn.drustvenamreza_back.indexrepository.GroupIndexRepository;
import ftn.drustvenamreza_back.model.entity.Reaction;
import ftn.drustvenamreza_back.service.PostService;
import ftn.drustvenamreza_back.service.implementation.PostServiceImpl;
import ftn.drustvenamreza_back.service.implementation.ReactionServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        Long totalLikes = reactions.stream().filter(reaction -> reaction.getType().equals("LIKE")).count();
        Long numberOfPosts = calculateNumberOfPosts(groupId);
        return numberOfPosts > 0 ? (double) totalLikes / numberOfPosts : 0.0;
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
