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
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private ReportReason reason;

    @Column(nullable = false)
    private LocalDate timestamp;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User byUser;

    @Column(nullable = false)
    private Boolean accepted;

    @Column(nullable = false)
    private Boolean isDeleted = false;
}
