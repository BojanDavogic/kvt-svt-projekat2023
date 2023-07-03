package ftn.drustvenamreza_back.service.implementation;

import ftn.drustvenamreza_back.model.dto.GroupDTO;
import ftn.drustvenamreza_back.model.entity.*;
import ftn.drustvenamreza_back.repository.GroupRepository;
import ftn.drustvenamreza_back.service.GroupService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {
    private final GroupRepository groupRepository;

    public GroupServiceImpl(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public List<Group> getAllGroups() {
        return groupRepository.findByIsDeletedFalse();
    }

    public Group createGroup(Group group, User creator) {
        group.setCreationDate(LocalDateTime.now());
//        GroupAdmin groupAdmin = new GroupAdmin(creator);
//        group.getGroupAdmins().add(groupAdmin);
        return groupRepository.save(group);
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

    public Group updateGroup(Long groupId, GroupDTO updatedGroup) {
        Group existingGroup = getGroupById(groupId);
        existingGroup.setName(updatedGroup.getName());
        existingGroup.setDescription(updatedGroup.getDescription());
        return groupRepository.save(existingGroup);
    }

    public void deleteGroup(Long groupId) {
        Group group = getGroupById(groupId);
        group.setIsDeleted(true);
        groupRepository.save(group);
    }

    public void removeGroupAdmin(Group group, User admin) {
        GroupAdmin groupAdmin = findGroupAdmin(group, admin);
        if (groupAdmin != null) {
            removeGroupAdmin(group, admin);
            // ...
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
//        if (group.getGroupAdmins() == null) {
//            group.setGroupAdmins(new ArrayList<>());
//        }
//        GroupAdmin groupAdmin = new GroupAdmin(admin);
//        group.getGroupAdmins().add(groupAdmin);
    }
}
