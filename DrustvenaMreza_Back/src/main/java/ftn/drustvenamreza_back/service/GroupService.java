package ftn.drustvenamreza_back.service;

import ftn.drustvenamreza_back.model.dto.GroupDTO;
import ftn.drustvenamreza_back.model.entity.Group;
import ftn.drustvenamreza_back.model.entity.GroupAdmin;
import ftn.drustvenamreza_back.model.entity.Post;
import ftn.drustvenamreza_back.model.entity.User;

import java.util.List;

public interface GroupService {
    List<Group> getAllGroups();
    Group createGroup(Group group, User creator);
    Group getGroupById(Long groupId);
    void removeGroupAdmin(Group group, User admin);
    void addPostToGroup(Post post, Group group);
    GroupAdmin findGroupAdmin(Group group, User admin);
    void removePostFromGroup(Post post, Group group);
    List<Post> getGroupPosts(Long groupId);
    void suspendGroup(Long groupId, String reason);
    Group updateGroup(Long groupId, Group updatedGroup);
    void deleteGroup(Long groupId);
}
