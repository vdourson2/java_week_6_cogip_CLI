package week6.java.cogipCli.cogip_CLI.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;
import week6.java.cogipCli.cogip_CLI.security.BearerTokenWrapper;

@Configuration
public class Config {

    @Bean
    public BearerTokenWrapper bearerTokenWrapper() {
        return new BearerTokenWrapper();
    }
}
