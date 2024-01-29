package week6.java.cogipCli.cogip_CLI.commands;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@ShellComponent
public class UserCommands {
    private final WebClient webClient;

    public UserCommands() {
        this.webClient = WebClient.create("http://localhost:8080");
    }

    @ShellMethod("Login")
    public Mono<String> login(String username, String password){
        return webClient
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path("/login")
                        .queryParam("username", username)
                        .queryParam("password", password)
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }

    @ShellMethod("Get all contacts")
    public Mono<String> getAllContacts(){
        return webClient
                .get()
                .uri("/api/contact")
                .retrieve()
                .bodyToMono(String.class);
    }
}
