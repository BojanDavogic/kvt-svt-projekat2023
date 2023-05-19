package ftn.drustvenamreza_back.initialization;

import ftn.drustvenamreza_back.model.entity.Administrator;
import ftn.drustvenamreza_back.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;

    public DataInitializer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        if (userRepository.findByUsername("admin2") == null) {
            Administrator administrator = new Administrator();
            administrator.setUsername("admin2");
            administrator.setPassword("$2a$10$WyGU0850Gt6l9niernBpb.58pCPz8XXEaI4qvOyj5rdEYIygCat/u");
            administrator.setLastLogin(LocalDateTime.now());
            administrator.setEmail("admin2@example.com");
            administrator.setFirstName("Adminko");
            administrator.setLastName("Adminović");
            administrator.setIsDeleted(false);

            userRepository.save(administrator);
        }
    }
}
