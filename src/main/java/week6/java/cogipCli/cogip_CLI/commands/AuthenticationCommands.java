package week6.java.cogipCli.cogip_CLI.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.web.client.RestTemplate;
import week6.java.cogipCli.cogip_CLI.security.BearerTokenWrapper;

@ShellComponent
public class AuthenticationCommands {
    RestTemplate restTemplate = new RestTemplate();
    BearerTokenWrapper bearerTokenWrapper;

    public AuthenticationCommands(BearerTokenWrapper bearerTokenWrapper){
        this.bearerTokenWrapper = bearerTokenWrapper;
    }

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

        // If login succeed store the token
        try{
            String result = restTemplate.postForObject("http://localhost:8080/login", request, String.class);
            JSONObject json = new JSONObject(result);
            String token = json.get("token").toString();
            bearerTokenWrapper.setToken(token);
            return "Welcome " + username + "!";
        }catch (Exception ex){
            return ex.getMessage();
        }
    }

}
