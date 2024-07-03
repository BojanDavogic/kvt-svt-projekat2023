package ftn.drustvenamreza_back.service.implementation;

import ftn.drustvenamreza_back.indexmodel.GroupIndex;
import ftn.drustvenamreza_back.indexservice.GroupIndexService;
import ftn.drustvenamreza_back.model.entity.*;
import ftn.drustvenamreza_back.repository.GroupAdminRepository;
import ftn.drustvenamreza_back.repository.GroupRepository;
import ftn.drustvenamreza_back.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {
    private final GroupRepository groupRepository;
    private final GroupAdminRepository groupAdminRepository;
    private final GroupIndexService groupIndexService;
    private final MinioServiceImpl minioService;

    public List<Group> getAllGroups() {
        return groupRepository.findByIsDeletedFalse();
    }

    public Group createGroup(Group group, User creator) {
        group.setCreationDate(LocalDateTime.now());
        Group savedGroup = groupRepository.save(group);
        addGroupAdmin(savedGroup, creator);

        GroupIndex groupIndex = new GroupIndex();
        groupIndex.setId(group.getId());
        groupIndex.setName(group.getName());
        groupIndex.setDescription(group.getDescription());
        groupIndex.setRules(group.getRules());

        Long numberOfPosts = groupIndexService.calculateNumberOfPosts(savedGroup.getId());
        groupIndex.setNumberOfPosts(numberOfPosts);

        Double averageLikes = groupIndexService.calculateAverageLikes(savedGroup.getId());
        groupIndex.setAverageLikes(averageLikes);

        groupIndexService.indexGroup(groupIndex);

        return savedGroup;
    }

    public Group getGroupById(Long groupId) {
        return groupRepository.findById(groupId).orElse(null);
    }

    public void addPostToGroup(Post post, Group group) {
        groupRepository.save(group);
    }

    public void removePostFromGroup(Post post, Group group) {
        groupRepository.save(group);
    }

    public List<Post> getGroupPosts(Long groupId) {
        Group group = getGroupById(groupId);
        return null;
    }

    public void suspendGroup(Long groupId, String reason) {
        Group group = getGroupById(groupId);
        group.setIsSuspended(true);
        group.setSuspendedReason(reason);
        groupRepository.save(group);
    }

    public Group updateGroup(Long groupId, Group updatedGroup) {
        Group existingGroup = getGroupById(groupId);
        existingGroup.setName(updatedGroup.getName());
        existingGroup.setDescription(updatedGroup.getDescription());
        existingGroup.setRules(updatedGroup.getRules());
        return groupRepository.save(existingGroup);
    }

    public void deleteGroup(Long groupId) {
        Group group = getGroupById(groupId);
        group.setIsDeleted(true);
        groupRepository.save(group);
        groupIndexService.deleteGroupIndex(groupId);
    }

    public void removeGroupAdmin(Group group, User admin) {
        List<GroupAdmin> groupAdmins = groupAdminRepository.findByGroupAndIsDeletedFalse(group);
        for (GroupAdmin groupAdmin : groupAdmins) {
            if (groupAdmin.getUser().equals(admin)) {
                groupAdmin.setIsDeleted(true);
                groupAdminRepository.save(groupAdmin);
                break;
            }
        }
    }

    public GroupAdmin findGroupAdmin(Group group, User admin) {
//        return group.getGroupAdmins().stream()
//                .filter(groupAdmin -> groupAdmin.getUser().equals(admin))
//                .findFirst()
//                .orElse(null);
        return null;
    }

    public void addGroupAdmin(Group group, User admin) {
        GroupAdmin groupAdmin = new GroupAdmin(admin);
        groupAdmin.setGroup(group);
        groupAdminRepository.save(groupAdmin);
    }
}
