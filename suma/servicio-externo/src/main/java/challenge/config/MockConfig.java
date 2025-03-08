package challenge.config;

import challenge.model.Mock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MockConfig {
    @Bean
    public Mock mock() {
        return new Mock();
    }
}
