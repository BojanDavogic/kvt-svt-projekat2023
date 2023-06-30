package ftn.drustvenamreza_back.controller;

import ftn.drustvenamreza_back.model.dto.LoginDTO;
import ftn.drustvenamreza_back.model.dto.TokenResponseDTO;
import ftn.drustvenamreza_back.model.entity.User;
import ftn.drustvenamreza_back.security.TokenUtils;
import ftn.drustvenamreza_back.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;

@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    UserService userService;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    TokenUtils tokenUtils;

    @PostMapping("")
    public ResponseEntity<TokenResponseDTO> login(@RequestBody LoginDTO userCredentials) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userCredentials.getUsername(), userCredentials.getPassword());

        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(authenticationToken);
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails;
        try {
            userDetails = userDetailsService.loadUserByUsername(userCredentials.getUsername());
        } catch (UsernameNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        String username = authentication.getName();
        User user = userService.getUserByUsername(username);

        userService.updateLastLogin(user, LocalDateTime.now());
        userService.updateUser(user);
        String accessToken = tokenUtils.generateToken(userDetails);
        Date expiresIn = tokenUtils.getExpirationDateFromToken(accessToken);
        return new ResponseEntity<>(new TokenResponseDTO(accessToken, expiresIn), HttpStatus.OK);
    }

}
