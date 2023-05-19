package ftn.drustvenamreza_back.controller;

import ftn.drustvenamreza_back.model.entity.*;
import ftn.drustvenamreza_back.service.implementation.GroupServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/groups")
public class GroupController {

    private final GroupServiceImpl groupService;

    public GroupController(GroupServiceImpl groupService) {
        this.groupService = groupService;
    }

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
        Group createdGroup = groupService.createGroup(group);
        return ResponseEntity.ok(createdGroup);
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

    @PostMapping("/{id}/posts")
    public ResponseEntity<Void> addPostToGroup(@PathVariable Long id, @RequestBody Post post) {
        Group group = groupService.getGroupById(id);
        if (group != null) {
            groupService.addPostToGroup(post, group);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{groupId}/posts/{postId}")
    public ResponseEntity<Void> removePostFromGroup(@PathVariable Long groupId, @PathVariable Long postId) {
        Group group = groupService.getGroupById(groupId);
        if (group != null) {
            Post post = new Post();
            post.setId(postId);
            groupService.removePostFromGroup(post, group);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/posts")
    public ResponseEntity<List<Post>> getGroupPosts(@PathVariable Long id) {
        List<Post> posts = groupService.getGroupPosts(id);
        return ResponseEntity.ok(posts);
    }

//    @PostMapping("/{id}/banned")
//    public ResponseEntity<Void> addBannedToGroup(@PathVariable Long id, @RequestBody Banned banned) {
//        Group group = groupService.getGroupById(id);
//        if (group != null) {
//            groupService.addBannedToGroup(group, banned);
//            return ResponseEntity.ok().build();
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    @DeleteMapping("/{groupId}/banned/{bannedId}")
//    public ResponseEntity<Void> removeBannedFromGroup(@PathVariable Long groupId, @PathVariable Long bannedId) {
//        Group group = groupService.getGroupById(groupId);
//        if (group != null) {
//            Banned banned = new Banned();
//            banned.setId(bannedId);
//            groupService.removeBannedFromGroup(group, banned);
//            return ResponseEntity.ok().build();
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    @PostMapping("/{id}/admins")
//    public ResponseEntity<Void> addGroupAdmin(@PathVariable Long id, @RequestBody GroupAdmin groupAdmin) {
//        Group group = groupService.getGroupById(id);
//        if (group != null) {
//            groupService.addGroupAdmin(group, groupAdmin);
//            return ResponseEntity.ok().build();
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    @DeleteMapping("/{groupId}/admins/{adminId}")
//    public ResponseEntity<Void> removeGroupAdmin(@PathVariable Long groupId, @PathVariable Long adminId) {
//        Group group = groupService.getGroupById(groupId);
//        if (group != null) {
//            GroupAdmin groupAdmin = new GroupAdmin();
//            groupAdmin.setId(adminId);
//            groupService.removeGroupAdmin(group, groupAdmin);
//            return ResponseEntity.ok().build();
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    @PostMapping("/{id}/requests")
//    public ResponseEntity<Void> addGroupRequest(@PathVariable Long id, @RequestBody GroupRequest groupRequest) {
//        Group group = groupService.getGroupById(id);
//        if (group != null) {
//            groupService.addGroupRequest(group, groupRequest);
//            return ResponseEntity.ok().build();
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    @DeleteMapping("/{groupId}/requests/{requestId}")
//    public ResponseEntity<Void> removeGroupRequest(@PathVariable Long groupId, @PathVariable Long requestId) {
//        Group group = groupService.getGroupById(groupId);
//        if (group != null) {
//            GroupRequest groupRequest = new GroupRequest();
//            groupRequest.setId(requestId);
//            groupService.removeGroupRequest(group, groupRequest);
//            return ResponseEntity.ok().build();
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }

    @PostMapping("/{id}/suspend")
    public ResponseEntity<Void> suspendGroup(@PathVariable Long id, @RequestParam String reason) {
        groupService.suspendGroup(id, reason);
        return ResponseEntity.ok().build();
    }
}
