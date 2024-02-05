package week6.java.cogipCli.cogip_CLI.commands;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.expression.spel.ast.BeanReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.web.client.RestTemplate;
import week6.java.cogipCli.cogip_CLI.security.BearerTokenWrapper;

@ShellComponent
public class UserCommands {
  
  private String getString(@ShellOption({"--pretty"}) boolean pretty, String response) {
    if (pretty) {
      // pretty print the response
      ObjectMapper mapper = new ObjectMapper();
      try {
        Object json = mapper.readValue(response, Object.class);
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
      } catch (JsonProcessingException e) {
        return "Error pretty printing the response: " + e.getMessage();
      }
    }
    return response;
  }
  
  RestTemplate restTemplate = new RestTemplate();
  BearerTokenWrapper tokenWrapper;
  HttpHeaders headers;

  public UserCommands(BearerTokenWrapper tokenWrapper){
    this.tokenWrapper = tokenWrapper;
    this.headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
  }
  
  // Get All Users Command (allusers, allusers --pretty)
  @ShellMethod(value = "Get All Users", key = "allusers", group = "User")
  public String getAllUsers(@ShellOption(value = {"--pretty"})boolean pretty){

    if (tokenWrapper.getToken() != null) headers.setBearerAuth(tokenWrapper.getToken());
    HttpEntity <String> request = new HttpEntity <> (headers);

    try {
      String response = restTemplate.exchange("http://localhost:8080/api/user", HttpMethod.GET, request, String.class).getBody();
      return getString(pretty, response);
    }catch (Exception ex){
      return ex.getMessage();
    }
  }
  
  // Get User By ID Command (userid {ID}, userid {ID} --pretty)
  @ShellMethod(value = "Get User by ID", key = "userid", group = "User")
  public String getUserById(int id, @ShellOption(defaultValue = "false") boolean pretty){

    if (tokenWrapper.getToken() != null) headers.setBearerAuth(tokenWrapper.getToken());
    HttpEntity <String> request = new HttpEntity <> (headers);

    try {
      String response = restTemplate.exchange("http://localhost:8080/api/user/" + id, HttpMethod.GET, request, String.class).getBody();
      return getString(pretty, response);
    }catch (Exception ex){
      return ex.getMessage();
    }
  }
  
  // Post User Command (adduser {USERNAME} {PASSWORD} {ROLE})
  @ShellMethod(value = "Post User", key = "adduser", group = "User")
  public String postUser(String username, String password, @ShellOption String role) {
    if (tokenWrapper.getToken() != null) headers.setBearerAuth(tokenWrapper.getToken());
    
    ObjectMapper mapper = new ObjectMapper();
    ObjectNode userJsonObject = mapper.createObjectNode();
    userJsonObject.put("username", username);
    userJsonObject.put("password", password);
    
    HttpEntity<String> request = new HttpEntity<>(userJsonObject.toString(), headers);

    String url = "http://localhost:8080/api/user";
    if (role != null) url += "?role=" + role.toUpperCase();
    
    try {
      return restTemplate.postForObject(url, request, String.class);
    }catch (Exception ex){
      return ex.getMessage();
    }
  }
  
  // Edit User Command (edituser {ID} --username {USERNAME} &&/|| --password {PASSWORD} &&/|| --role {ROLE})
  @ShellMethod(value = "Edit User", key = "edituser", group = "User")
  public String putUser(int id, @ShellOption(defaultValue = ShellOption.NULL) String username, @ShellOption(defaultValue = ShellOption.NULL) String password, @ShellOption(defaultValue = ShellOption.NULL) String role) {
    if (tokenWrapper.getToken() != null) headers.setBearerAuth(tokenWrapper.getToken());
    
    String usernameParam = username != null ? "username=" + username : "";
    String passwordParam = password != null ? "&password=" + password : "";
    String roleParam = role != null ? "&role=" + role.toUpperCase() : "";
    
    if (!usernameParam.isEmpty() && passwordParam.startsWith("&")) passwordParam = passwordParam.substring(1);
    if ((!usernameParam.isEmpty() || !passwordParam.isEmpty()) && roleParam.startsWith("&")) roleParam = roleParam.substring(1);
    if (!usernameParam.isEmpty() && !passwordParam.isEmpty() && passwordParam.charAt(0) != '&') passwordParam = "&" + passwordParam;
    if ((!usernameParam.isEmpty() || !passwordParam.isEmpty()) && !roleParam.isEmpty() && roleParam.charAt(0) != '&') roleParam = "&" + roleParam;
    
    HttpEntity<String> request = new HttpEntity<>(headers);
    
    try {
      return restTemplate.exchange("http://localhost:8080/api/user/" + id + "?" + usernameParam + passwordParam + roleParam, HttpMethod.PUT, request, String.class).getBody();
    }catch (Exception ex){
      return ex.getMessage();
    }
  }
  
  // Delete User Command (deluser {ID})
  @ShellMethod(value = "Delete User", key = "deluser", group = "User")
  public String delUser (int id){
    if (tokenWrapper.getToken() != null) headers.setBearerAuth(tokenWrapper.getToken());
    HttpEntity <String> request = new HttpEntity <> (headers);

    try {
      return restTemplate.exchange("http://localhost:8080/api/user/" + id, HttpMethod.DELETE, request, String.class).getBody();
    }catch (Exception ex){
      return ex.getMessage();
    }
  }
}