package ftn.drustvenamreza_back.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = true)
    private LocalDateTime creationDate = LocalDateTime.now();

    @Column(nullable = false)
    private Boolean isDeleted = false;

    @ManyToOne
    @JoinColumn(name = "posted_by_id")
    private User postedBy;

    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "group_id")
    private Group group;

//    @OneToMany(mappedBy = "post")
//    private List<Image> images;
//
//    @OneToMany(mappedBy = "post")
//    private List<Report> reports;
//
//    @OneToMany(mappedBy = "post")
//    private List<Reaction> reactions;
//
//    @OneToMany(mappedBy = "post")
//    private List<Comment> comments = new ArrayList<>();
}
