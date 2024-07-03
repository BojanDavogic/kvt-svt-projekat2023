package ftn.drustvenamreza_back.controller;

import ftn.drustvenamreza_back.model.dto.GroupDTO;
import ftn.drustvenamreza_back.model.entity.*;
import ftn.drustvenamreza_back.service.implementation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/groups")
public class GroupController {

    private final GroupServiceImpl groupService;
    private final UserServiceImpl userService;
    private final PostServiceImpl postService;
    private final CommentServiceImpl commentService;
    private final ReactionServiceImpl reactionService;

    public GroupController(GroupServiceImpl groupService, UserServiceImpl userService, PostServiceImpl postService, CommentServiceImpl commentService, ReactionServiceImpl reactionService) {
        this.groupService = groupService;
        this.userService = userService;
        this.postService = postService;
        this.commentService = commentService;
        this.reactionService = reactionService;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/{groupId}")
    public ResponseEntity<Group> getGroupById(@PathVariable Long groupId) {
        Group group = groupService.getGroupById(groupId);
        if (group != null) {
            return ResponseEntity.ok(group);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Group> createGroup(@RequestBody Group group) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User creator = userService.getUserByUsername(authentication.getName());
        Group createdGroup = groupService.createGroup(group, creator);
        return ResponseEntity.ok(createdGroup);
    }

    @PostMapping("/{groupId}/admins")
    public ResponseEntity<Void> addGroupAdmin(@PathVariable Long groupId, @RequestParam Long userId) {
        Group group = groupService.getGroupById(groupId);
        User admin = userService.getUserById(userId);

        if (group != null && admin != null) {

            groupService.addGroupAdmin(group, admin);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{groupId}/admins/{adminId}")
    public ResponseEntity<Void> removeGroupAdmin(@PathVariable Long groupId, @PathVariable Long adminId) {
        Group group = groupService.getGroupById(groupId);
        User admin = userService.getUserById(adminId);

        if (group != null && admin != null) {

            groupService.removeGroupAdmin(group, admin);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{groupId}")
    public ResponseEntity<Group> updateGroup(@PathVariable Long groupId, @RequestBody Group group) {
        Group updatedGroup = groupService.updateGroup(groupId, group);
        if (updatedGroup != null) {
            return ResponseEntity.ok(updatedGroup);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{groupId}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long groupId) {
        groupService.deleteGroup(groupId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Group>> getAllGroups() {
        List<Group> groups = groupService.getAllGroups();
        return ResponseEntity.ok(groups);
    }

    @PostMapping("/{id}/suspend")
    public ResponseEntity<Void> suspendGroup(@PathVariable Long id, @RequestParam String reason) {
        groupService.suspendGroup(id, reason);
        return ResponseEntity.ok().build();
    }
}
