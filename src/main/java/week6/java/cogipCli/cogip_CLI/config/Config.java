package week6.java.cogipCli.cogip_CLI.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import week6.java.cogipCli.cogip_CLI.security.BearerTokenWrapper;

@Configuration
public class Config {

    @Bean
    public BearerTokenWrapper bearerTokenWrapper() {
        return new BearerTokenWrapper();
    }
}
