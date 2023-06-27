package ftn.drustvenamreza_back;

import ftn.drustvenamreza_back.config.CorsConfiguration;
import ftn.drustvenamreza_back.config.JacksonConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Import({CorsConfiguration.class, JacksonConfig.class})

@SpringBootApplication
public class DrustvenaMrezaBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(DrustvenaMrezaBackApplication.class, args);
    }

}
