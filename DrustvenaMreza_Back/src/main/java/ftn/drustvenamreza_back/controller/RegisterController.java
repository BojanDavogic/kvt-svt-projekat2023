package ftn.drustvenamreza_back.controller;

import ftn.drustvenamreza_back.model.dto.UserDTO;
import ftn.drustvenamreza_back.model.entity.User;
import ftn.drustvenamreza_back.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/register")
public class RegisterController {
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping
    public ResponseEntity<String> registerUser(@RequestBody User user) {

        if (userService.getUserByUsername(user.getUsername()) != null) {
            return ResponseEntity.badRequest().body("Korisnicko ime vec postoji");
        }

        if (userService.getUserByEmail(user.getEmail()) != null) {
            return ResponseEntity.badRequest().body("Email već postoji");
        }

        UserDTO newUser = new UserDTO();
        newUser.setUsername(user.getUsername());
        newUser.setEmail(user.getEmail());
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());

        newUser.setPassword(user.getPassword());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));


        User createdUser = userService.createUser(user);
        if (createdUser != null) {
            return ResponseEntity.ok().body("{\"message\": \"Uspesna registracija\"}");

        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Greska prilikom registracije korisnika");
        }
    }

}
