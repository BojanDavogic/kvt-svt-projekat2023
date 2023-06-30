package ftn.drustvenamreza_back.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String text;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy. hh:mm")
    @Column()
    private LocalDateTime timestamp;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parentComment")
    private List<Comment> replies;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;

    @Column(nullable = false)
    private Boolean isDeleted = false;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @OneToMany(mappedBy = "comment")
    private List<Report> reports;

    @OneToMany(mappedBy = "comment")
    private List<Reaction> reactions;

    public void addReply(Comment reply) {
        reply.setParentComment(this);
        replies.add(reply);
        reply.setPost(this.getPost());
    }

    public void removeReply(Comment reply) {
        reply.setParentComment(null);
        replies.remove(reply);
        reply.setPost(null);
    }
}
