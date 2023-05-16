package ftn.drustvenamreza_back.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Banned {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate timestamp;

    @Column(nullable = false)
    private Boolean isDeleted = false;
}
