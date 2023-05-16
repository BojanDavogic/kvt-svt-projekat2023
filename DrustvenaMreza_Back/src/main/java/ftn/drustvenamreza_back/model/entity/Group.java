package ftn.drustvenamreza_back.model.entity;

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

    @Column(nullable = false)
    private LocalDateTime creationDate;

    @Column(nullable = false)
    private Boolean isSuspended;

    @Column(nullable = false)
    private String suspendedReason;

    @Column(nullable = false)
    private Boolean isDeleted = false;

    @OneToMany(mappedBy = "group", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();
}
