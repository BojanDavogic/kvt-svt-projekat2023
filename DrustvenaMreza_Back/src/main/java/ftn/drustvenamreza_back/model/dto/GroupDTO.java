package ftn.drustvenamreza_back.model.dto;

import ftn.drustvenamreza_back.model.entity.Group;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GroupDTO {
    private Long id;
    private String name;
    private String description;

    public GroupDTO(Group createdGroup){
        this.id = createdGroup.getId();
        this.name = createdGroup.getName();
        this.description = createdGroup.getDescription();
    }

}
