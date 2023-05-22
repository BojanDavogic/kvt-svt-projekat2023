package ftn.drustvenamreza_back.controller;

import ftn.drustvenamreza_back.model.dto.UserDTO;
import ftn.drustvenamreza_back.model.entity.User;
import ftn.drustvenamreza_back.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/register")
public class RegisterController {
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("")
    public ResponseEntity<String> registerUser(@RequestBody UserDTO userDTO) {

        if (userService.getUserByUsername(userDTO.getUsername()) != null) {
            return ResponseEntity.badRequest().body("Korisnicko ime vec postoji");
        }

        if (userService.getUserByEmail(userDTO.getEmail()) != null) {
            return ResponseEntity.badRequest().body("Email već postoji");
        }

        UserDTO newUser = new UserDTO();
        newUser.setUsername(userDTO.getUsername());
        newUser.setEmail(userDTO.getEmail());
        newUser.setFirstName(userDTO.getFirstName());
        newUser.setLastName(userDTO.getLastName());

        newUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        User createdUser = userService.createUser(newUser);
        if (createdUser != null) {
            return ResponseEntity.ok("Uspesna registracija");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Greska prilikom registracije korisnika");
        }
    }

}
