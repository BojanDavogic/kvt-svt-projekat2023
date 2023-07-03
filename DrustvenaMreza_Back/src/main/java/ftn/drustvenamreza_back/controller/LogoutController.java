package ftn.drustvenamreza_back.controller;

import ftn.drustvenamreza_back.security.TokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/logout")
public class LogoutController {

    @GetMapping
    public ResponseEntity<String> logout(HttpServletRequest request) {
        try {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            request.getSession().invalidate();
            SecurityContextHolder.clearContext();

            return ResponseEntity.ok("Uspešno ste se odjavili.");
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Došlo je do greške prilikom odjavljivanja.");
        }
    }
}
