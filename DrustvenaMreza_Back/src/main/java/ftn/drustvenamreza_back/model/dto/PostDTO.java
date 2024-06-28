package ftn.drustvenamreza_back.model.dto;

import ftn.drustvenamreza_back.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {
    private Long id;
    private String content;
    private String creationDate;
    private Boolean isDeleted;
    private String postedBy;
    private String group;
    private String fileName;
}
