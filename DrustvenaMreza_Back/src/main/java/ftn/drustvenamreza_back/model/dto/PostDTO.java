package ftn.drustvenamreza_back.model.dto;

import ftn.drustvenamreza_back.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {
    private String content;
    private User postedBy;
}
