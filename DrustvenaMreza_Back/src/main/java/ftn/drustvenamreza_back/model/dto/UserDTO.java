package ftn.drustvenamreza_back.model.dto;

import ftn.drustvenamreza_back.model.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {
    private Long id;
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    private String email;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;

    // Konstruktor, getteri i setteri

    public UserDTO(User createdUser) {
        this.id = createdUser.getId();
        this.username = createdUser.getUsername();
        this.email = createdUser.getEmail();
        this.firstName = createdUser.getFirstName();
        this.lastName = createdUser.getLastName();
    }
}
