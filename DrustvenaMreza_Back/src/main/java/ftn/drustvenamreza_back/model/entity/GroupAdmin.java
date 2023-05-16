package ftn.drustvenamreza_back.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "group_admin")
@NoArgsConstructor
@Getter
@Setter
public class GroupAdmin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    private Group group;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(nullable = false)
    private Boolean isDeleted = false;
}
