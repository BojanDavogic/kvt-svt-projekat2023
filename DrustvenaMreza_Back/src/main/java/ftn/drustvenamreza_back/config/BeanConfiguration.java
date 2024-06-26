package ftn.drustvenamreza_back.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.apache.tika.language.detect.LanguageDetector;

import java.io.IOException;

@Configuration
public class BeanConfiguration {

    @Bean
    public LanguageDetector languageDetector() {
        LanguageDetector languageDetector;
        try {
            languageDetector = LanguageDetector.getDefaultLanguageDetector().loadModels();
        } catch (IOException e) {
            throw new NotFoundException("Error while loading language models.");
        }
        return languageDetector;
    }
}
