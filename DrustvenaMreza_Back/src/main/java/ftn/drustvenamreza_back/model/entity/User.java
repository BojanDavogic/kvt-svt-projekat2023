package ftn.drustvenamreza_back.model.entity;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

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

    @Column
    private String description = "";

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;
//    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy. hh:mm:ss")
    @Column(nullable = true)
    private LocalDateTime lastLogin;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private Boolean isDeleted = false;

    @ManyToMany
    @JoinTable(
            name = "user_friends",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
    private List<User> friends;

//    @ManyToMany
//    @JoinTable(
//            name = "user_groups",
//            joinColumns = @JoinColumn(name = "user_id"),
//            inverseJoinColumns = @JoinColumn(name = "group_id")
//    )
//    private List<Group> userGroups;

    public User(String username) {
    }

    public User(String trim, String trim1, List<GrantedAuthority> grantedAuthorities) {
    }

    public String getDtype() {
        if (this instanceof Administrator) {
            return "Administrator";
        } else if (this instanceof User) {
            return "User";
        } else {
            return "";
        }
    }
}
