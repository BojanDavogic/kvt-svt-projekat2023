package ftn.drustvenamreza_back.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy. HH:mm")
    @Column(nullable = false)
    private LocalDateTime creationDate = LocalDateTime.now();

    @Column(nullable = false)
    private Boolean isDeleted = false;

    @ManyToOne
    @JoinColumn(name = "posted_by_id", nullable = false)
    private User postedBy;

    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "group_id")
    private Group group;

    @Column(nullable = true)
    private String fileName;

    private Long numberOfLikes = 0L;
}
