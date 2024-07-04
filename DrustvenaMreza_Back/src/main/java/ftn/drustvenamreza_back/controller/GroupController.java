package ftn.drustvenamreza_back.controller;

import ftn.drustvenamreza_back.indexmodel.GroupIndex;
import ftn.drustvenamreza_back.indexmodel.PostIndex;
import ftn.drustvenamreza_back.indexservice.GroupIndexService;
import ftn.drustvenamreza_back.indexservice.IndexingServiceImpl;
import ftn.drustvenamreza_back.indexservice.SearchServiceImpl;
import ftn.drustvenamreza_back.model.dto.GroupDTO;
import ftn.drustvenamreza_back.model.entity.*;
import ftn.drustvenamreza_back.service.implementation.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/groups")
public class GroupController {

    private final GroupServiceImpl groupService;
    private final UserServiceImpl userService;
    private final PostServiceImpl postService;
    private final CommentServiceImpl commentService;
    private final ReactionServiceImpl reactionService;
    private final IndexingServiceImpl indexingService;
    private final SearchServiceImpl searchService;
    private final GroupIndexService groupIndexService;

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

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Group> createGroup(@RequestParam("file") MultipartFile file, @RequestPart("group") Group group) {
        try {
            User creator = userService.getCurrentUser();
            Group createdGroup = groupService.createGroup(group, creator);
            indexingService.indexGroup(file, createdGroup.getId());
            return ResponseEntity.ok(createdGroup);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

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

    @PutMapping("/{groupId}/updateRules")
    public ResponseEntity<Group> updateGroupRules(@PathVariable Long groupId, @RequestBody String rules) {
        Group updatedGroup = groupService.updateGroupRules(groupId, rules);
        if (updatedGroup != null) {

            Optional<GroupIndex> existingGroupIndexOptional = groupIndexService.findById(updatedGroup.getId().toString());
            if (existingGroupIndexOptional.isPresent()) {
                GroupIndex existingGroupIndex = existingGroupIndexOptional.get();
                existingGroupIndex.setRules(rules);
                groupIndexService.updateGroupIndex(existingGroupIndex);
            }

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

    @GetMapping("/search/simple")
    public ResponseEntity<Page<GroupIndex>> simpleSearch(@RequestParam String query, @RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<String> keywords = Arrays.asList(query.split("\\s+"));
        Page<GroupIndex> groups = searchService.simpleGroupSearch(keywords, pageable);
        return ResponseEntity.ok(groups);
    }

    @GetMapping("/search/advanced")
    public ResponseEntity<Page<GroupIndex>> advancedSearch(@RequestParam List<String> expression, @RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<GroupIndex> groups = searchService.advancedGroupSearch(expression, pageable);
        return ResponseEntity.ok(groups);
    }
}
