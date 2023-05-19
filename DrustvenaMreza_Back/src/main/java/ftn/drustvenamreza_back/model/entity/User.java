package ftn.drustvenamreza_back.model.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


@Entity
@NoArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private LocalDateTime lastLogin;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private Boolean isDeleted = false;
//
//    @OneToMany(mappedBy = "postedBy")
//    private List<Post> posts;
//
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    private List<Comment> comments;

    @ManyToMany
    @JoinTable(
            name = "user_friends",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
    private List<User> friends;

    public String getDtype() {
        if (this instanceof Administrator) {
            return "Administrator";
        } else if (this instanceof User) {
            return "User";
        } else {
            return "";
        }
    }

//    @OneToMany(mappedBy = "fromUser")
//    private List<FriendRequest> sentFriendRequests;
//
//    @OneToMany(mappedBy = "toUser")
//    private List<FriendRequest> receivedFriendRequests;

//    @OneToMany(mappedBy = "user")
//    private List<Banned> bannedList;
//
//    @OneToMany(mappedBy = "createdBy")
//    private List<GroupRequest> groupRequests;
//
//    @OneToOne(mappedBy = "user")
//    private Image image;
//
//    @OneToMany(mappedBy = "user")
//    private List<Report> reports;
//
//    @OneToMany(mappedBy = "madeBy")
//    private List<Reaction> reactions;
}
