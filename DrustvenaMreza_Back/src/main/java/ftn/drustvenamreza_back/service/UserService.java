package ftn.drustvenamreza_back.service;

import ftn.drustvenamreza_back.model.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.List;

public interface UserService {
    User createUser(User user);

    List<User> getAllUsers();

    User getUserById(Long userId);

    User getUserByUsername(String username);

    User getUserByEmail(String email);

    void updateUser(User updatedUser);

    void deleteUser(Long userId);

    void updateLastLogin(User user, LocalDateTime lastLogin);
}
