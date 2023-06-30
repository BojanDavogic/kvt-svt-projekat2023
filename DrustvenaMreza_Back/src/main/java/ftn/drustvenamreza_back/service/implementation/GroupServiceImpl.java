package ftn.drustvenamreza_back.service.implementation;

import ftn.drustvenamreza_back.model.dto.GroupDTO;
import ftn.drustvenamreza_back.model.entity.*;
import ftn.drustvenamreza_back.repository.GroupRepository;
import ftn.drustvenamreza_back.service.GroupService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    public Group createGroup(Group group) {
        group.setCreationDate(LocalDateTime.now());
        return groupRepository.save(group);
    }

    public Group getGroupById(Long groupId) {
        return groupRepository.findById(groupId).orElse(null);
    }

    public void addPostToGroup(Post post, Group group) {
        group.addPost(post);
        groupRepository.save(group);
    }

    public void removePostFromGroup(Post post, Group group) {
        group.removePost(post);
        groupRepository.save(group);
    }

    public List<Post> getGroupPosts(Long groupId) {
        Group group = getGroupById(groupId);
        return group.getPosts();
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
}
