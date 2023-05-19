package ftn.drustvenamreza_back.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "social_group")
@NoArgsConstructor
@Getter
@Setter
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @JsonFormat(pattern = "dd.MM.yyyy")
    @Column(nullable = false)
    private LocalDateTime creationDate;

    @Column(nullable = false)
    private Boolean isSuspended;

    @Column(nullable = false)
    private String suspendedReason;

    @Column(nullable = false)
    private Boolean isDeleted = false;
    @JsonIgnore
    @OneToMany(mappedBy = "group", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

//    @OneToMany(mappedBy = "group")
//    private List<Banned> bannedList;
//
//    @OneToMany(mappedBy = "group")
//    private List<GroupAdmin> groupAdmins;
//
//    @OneToMany(mappedBy = "group")
//    private List<GroupRequest> groupRequests;

    public Group(String name, String description) {
        this.name = name;
        this.description = description;
        this.creationDate = LocalDateTime.now();
        this.isSuspended = false;
        this.suspendedReason = "";
        this.isDeleted = false;
    }

    public void removePost(Post post) {
        posts.remove(post);
        post.setGroup(null);
    }

    public void addPost(Post post) {
        posts.add(post);
        post.setGroup(this);
    }

//    public void addBanned(Banned banned) {
//        bannedList.add(banned);
//        banned.setGroup(this);
//    }
//
//    public void removeBanned(Banned banned) {
//        bannedList.remove(banned);
//        banned.setGroup(null);
//    }
//
//    public void addGroupAdmin(GroupAdmin groupAdmin) {
//        groupAdmins.add(groupAdmin);
//        groupAdmin.setGroup(this);
//    }
//
//    public void removeGroupAdmin(GroupAdmin groupAdmin) {
//        groupAdmins.remove(groupAdmin);
//        groupAdmin.setGroup(null);
//    }
//
//    public void addGroupRequest(GroupRequest groupRequest) {
//        groupRequests.add(groupRequest);
//        groupRequest.setGroup(this);
//    }
//
//    public void removeGroupRequest(GroupRequest groupRequest) {
//        groupRequests.remove(groupRequest);
//        groupRequest.setGroup(null);
//    }
}
