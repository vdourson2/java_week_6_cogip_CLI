package week6.java.cogipCli.cogip_CLI.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.web.client.RestTemplate;

@ShellComponent
public class AuthenticationCommands {
    RestTemplate restTemplate = new RestTemplate();

    // Command for the post login
    @ShellMethod(value = "Login", key = "login", group = "Authentication")
    public String login(String username, String password) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode userJsonObject = mapper.createObjectNode();
        userJsonObject.put("username", username);
        userJsonObject.put("password", password);

        HttpEntity<String> request = new HttpEntity<>(userJsonObject.toString(), headers);

        return restTemplate.postForObject("http://localhost:8080/login", request, String.class);
    }

}
