package ftn.drustvenamreza_back.controller;

import ftn.drustvenamreza_back.model.entity.Group;
import ftn.drustvenamreza_back.service.GroupService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/groups")
public class GroupController {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping("/{groupId}")
    public Group getGroupById(@PathVariable Long groupId) {
        return groupService.getGroupById(groupId);
    }

    @PostMapping
    public Group createGroup(@RequestBody Group group) {
        return groupService.createGroup(group);
    }

    @PutMapping("/{groupId}")
    public Group updateGroup(@PathVariable Long groupId, @RequestBody Group group) {
        return groupService.updateGroup(groupId, group);
    }

    @DeleteMapping("/{groupId}")
    public void deleteGroup(@PathVariable Long groupId) {
        groupService.deleteGroup(groupId);
    }

    @GetMapping
    public List<Group> getAllGroups() {
        return groupService.getAllGroups();
    }
}
