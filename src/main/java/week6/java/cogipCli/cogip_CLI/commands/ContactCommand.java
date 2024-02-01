package week6.java.cogipCli.cogip_CLI.commands;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.web.client.RestTemplate;
import week6.java.cogipCli.cogip_CLI.entities.ContactMapping;

import java.util.List;

@ShellComponent
public class ContactCommand {
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
 
  
  // Get All Contact Command (allcontact, allcontact --pretty)
  @ShellMethod(value = "Get All Contacts", key = "allcontacts", group = "Contact")
  public String getAllContacts(@ShellOption(value = {"--pretty"})boolean pretty) throws JsonProcessingException {
    List<ContactMapping> contactMappings = new ObjectMapper()
            .readerFor(new TypeReference<List<ContactMapping>>() {})
            .readValue(restTemplate.getForObject("http://localhost:8080/api/contact", String.class));
    
    String json = new ObjectMapper().writeValueAsString(contactMappings);
    return getString(pretty, json);
    
  }
  
  // Get Contact By ID Command (contactid {ID}, contactid {ID} --pretty)
  @ShellMethod(value = "Get Contact by ID", key = "contactid", group = "Contact")
  public String getContactById(int id, @ShellOption(value = {"--pretty"}) boolean pretty) throws JsonProcessingException {
    ContactMapping contactMappings = new ObjectMapper()
            .readerFor(new TypeReference<ContactMapping>() {})
            .readValue(restTemplate.getForObject("http://localhost:8080/api/contact/" + id, String.class));
    
    String json = new ObjectMapper().writeValueAsString(contactMappings);
    return getString(pretty, json);
  }
  
  // Post Contact Command (addcontact {FIRSTNAME} {LASTNAME} {PHONE} {EMAIL} {COMPANYID})
  @ShellMethod(value = "Post Contact", key = "addcontact", group = "Contact")
  public String postUser(String firstname, String lastname, String phone, String email, Integer companyId) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    
    ObjectMapper mapper = new ObjectMapper();
    ObjectNode userJsonObject = mapper.createObjectNode();
    userJsonObject.put("firstname", firstname);
    userJsonObject.put("lastname", lastname);
    userJsonObject.put("phone", phone);
    userJsonObject.put("email", email);
    
    
    HttpEntity<String> request = new HttpEntity<>(userJsonObject.toString(), headers);
    
    return restTemplate.postForObject("http://localhost:8080/api/contact?companyId=" + companyId, request, String.class);
  }
  
  // Edit User Command (edituser {ID} --username {USERNAME} &&/|| --password {PASSWORD} &&/|| --role {ROLE})
  @ShellMethod(value = "Edit Contact", key = "editcontact", group = "Contact")
  public String putUser(int id, @ShellOption(defaultValue = ShellOption.NULL) String username,
                        @ShellOption(defaultValue = ShellOption.NULL) String password,
                        @ShellOption(defaultValue = ShellOption.NULL) String role) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    
    String usernameParam = username != null ? "username=" + username : "";
    String passwordParam = password != null ? "&password=" + password : "";
    String roleParam = role != null ? "&role=" + role.toUpperCase() : "";
    
    if (!usernameParam.isEmpty() && passwordParam.startsWith("&")) passwordParam = passwordParam.substring(1);
    if ((!usernameParam.isEmpty() || !passwordParam.isEmpty()) && roleParam.startsWith("&")) roleParam = roleParam.substring(1);
    if (!usernameParam.isEmpty() && !passwordParam.isEmpty() && passwordParam.charAt(0) != '&') passwordParam = "&" + passwordParam;
    if ((!usernameParam.isEmpty() || !passwordParam.isEmpty()) && !roleParam.isEmpty() && roleParam.charAt(0) != '&') roleParam = "&" + roleParam;
    
    HttpEntity<String> request = new HttpEntity<>(headers);
    
    return restTemplate.exchange("http://localhost:8080/api/user/" + id + "?" + usernameParam + passwordParam + roleParam, HttpMethod.PUT, request, String.class).getBody();
  }
  
  // Delete User Command (deluser {ID})
  @ShellMethod(value = "Delete Contact", key = "delcontact", group = "Contact")
  public void delUser (int id){
    restTemplate.delete("http://localhost:8080/api/contact/" + id, HttpMethod.DELETE, String.class);
  }
}
