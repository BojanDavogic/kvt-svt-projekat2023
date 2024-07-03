package ftn.drustvenamreza_back.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy.")
    @Column(nullable = true)
    private LocalDateTime creationDate;

    @Column(nullable = true)
    private Boolean isSuspended;

    @Column(nullable = true)
    private String suspendedReason;

    @Column(nullable = false)
    private Boolean isDeleted = false;

    @Column(nullable = true)
    private String rules;

    @Column(nullable = true)
    private String pdfUrl;
    public Group(String name, String description) {
        this.name = name;
        this.description = description;
        this.creationDate = LocalDateTime.now();
        this.isSuspended = false;
        this.suspendedReason = "";
        this.isDeleted = false;
        this.rules = "";
    }
}
