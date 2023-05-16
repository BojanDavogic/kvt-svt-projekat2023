package ftn.drustvenamreza_back.service;

import ftn.drustvenamreza_back.model.entity.Group;
import ftn.drustvenamreza_back.repository.GroupRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService {
    private final GroupRepository groupRepository;

    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    public Group createGroup(Group group) {
        return groupRepository.save(group);
    }

    public Group getGroupById(Long groupId) {
        return groupRepository.findById(groupId).orElse(null);
    }

    public Group updateGroup(Long groupId, Group updatedGroup) {
        Group existingGroup = getGroupById(groupId);
        existingGroup.setName(updatedGroup.getName());
        existingGroup.setDescription(updatedGroup.getDescription());
        return groupRepository.save(existingGroup);
    }

    public void deleteGroup(Long groupId) {
        groupRepository.deleteById(groupId);
    }
}
