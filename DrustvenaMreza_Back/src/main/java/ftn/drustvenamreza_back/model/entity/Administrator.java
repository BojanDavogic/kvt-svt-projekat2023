package ftn.drustvenamreza_back.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Administrator extends User {
    @OneToMany(mappedBy = "bannedByAdmin")
    private List<Banned> bannedList;
}
