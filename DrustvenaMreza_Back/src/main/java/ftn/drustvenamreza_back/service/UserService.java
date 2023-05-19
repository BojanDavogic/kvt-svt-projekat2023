package ftn.drustvenamreza_back.service;

import ftn.drustvenamreza_back.model.dto.UserDTO;
import ftn.drustvenamreza_back.model.entity.User;

import java.util.List;

public interface UserService {
    User createUser(UserDTO userDTO);

    List<User> getAllUsers();

    User getUserById(Long userId);

    User getUserByUsername(String username);

    User getUserByEmail(String email);

    void updateUser(User updatedUser);

    void deleteUser(Long userId);
}
