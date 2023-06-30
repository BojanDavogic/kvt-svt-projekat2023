package ftn.drustvenamreza_back.service;

import ftn.drustvenamreza_back.model.dto.GroupDTO;
import ftn.drustvenamreza_back.model.entity.Group;
import ftn.drustvenamreza_back.model.entity.Post;

import java.util.List;

public interface GroupService {
    List<Group> getAllGroups();
    Group createGroup(Group group);
    Group getGroupById(Long groupId);
    void addPostToGroup(Post post, Group group);
    void removePostFromGroup(Post post, Group group);
    List<Post> getGroupPosts(Long groupId);
    void suspendGroup(Long groupId, String reason);
    Group updateGroup(Long groupId, GroupDTO updatedGroup);
    void deleteGroup(Long groupId);
}
