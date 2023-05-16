package ftn.drustvenamreza_back.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class GroupRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Boolean approved;

    @Column(nullable = false)
    private LocalDateTime created_at;

    @Column(nullable = false)
    private LocalDateTime at;

    @Column(nullable = false)
    private Boolean isDeleted = false;
}
