package week6.java.cogipCli.cogip_CLI.commands;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.*;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import week6.java.cogipCli.cogip_CLI.entities.ContactMapping;
import week6.java.cogipCli.cogip_CLI.security.BearerTokenWrapper;

import java.util.List;
import java.util.concurrent.ExecutionException;

@ShellComponent
public class ContactCommands {
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

  HttpHeaders headers = new HttpHeaders();

  public ContactCommands(BearerTokenWrapper tokenWrapper){
    this.tokenWrapper = tokenWrapper;
    headers.setContentType(MediaType.APPLICATION_JSON);
  }
 
  
  // Get All Contact Command (allcontact, allcontact --pretty)
  @ShellMethod(value = "Get All Contacts", key = "allcontacts", group = "Contact")
  public String getAllContacts(@ShellOption(value = {"--pretty"})boolean pretty) throws JsonProcessingException {
    if (tokenWrapper.getToken() != null) headers.setBearerAuth(tokenWrapper.getToken());
    HttpEntity <String> request = new HttpEntity <> (headers);

    try{
      List<ContactMapping> contactMappings = new ObjectMapper()
              .readerFor(new TypeReference<List<ContactMapping>>() {})
              .readValue(restTemplate.exchange("http://localhost:8080/api/contact", HttpMethod.GET ,request, String.class).getBody());

      String json = new ObjectMapper().writeValueAsString(contactMappings);
      return getString(pretty, json);
    }catch (Exception ex){
      return ex.getMessage();
    }
    
  }
  
  // Get Contact By ID Command (contactid {ID}, contactid {ID} --pretty)
  @ShellMethod(value = "Get Contact by ID", key = "contactid", group = "Contact")
  public String getContactById(int id, @ShellOption(value = {"--pretty"}) boolean pretty) throws JsonProcessingException {
    if (tokenWrapper.getToken() != null) headers.setBearerAuth(tokenWrapper.getToken());
    HttpEntity <String> request = new HttpEntity <> (headers);

    try {
      ContactMapping contactMappings = new ObjectMapper()
              .readerFor(new TypeReference<ContactMapping>() {})
              .readValue(restTemplate.exchange("http://localhost:8080/api/contact/" + id, HttpMethod.GET, request, String.class).getBody());

      String json = new ObjectMapper().writeValueAsString(contactMappings);
      return getString(pretty, json);
    }catch (Exception ex){
      return ex.getMessage();
    }
  }
  
  // Post Contact Command (addcontact {FIRSTNAME} {LASTNAME} {PHONE} {EMAIL} {COMPANYID})
  @ShellMethod(value = "Post Contact", key = "addcontact", group = "Contact")
  public String postContact(String firstname, String lastname, String phone, String email, Integer companyId) {
    if (tokenWrapper.getToken() != null) headers.setBearerAuth(tokenWrapper.getToken());

    ObjectMapper mapper = new ObjectMapper();
    ObjectNode contactJsonObject = mapper.createObjectNode();
    contactJsonObject.put("firstname", firstname);
    contactJsonObject.put("lastname", lastname);
    contactJsonObject.put("phone", phone);
    contactJsonObject.put("email", email);

    String url = "http://localhost:8080/api/contact";
    System.out.println("company id");
    if (companyId != null) url += "?companyId=" + companyId;
    
    HttpEntity<String> request = new HttpEntity<>(contactJsonObject.toString(), headers);
    
    try {
      restTemplate.postForObject(url, request, String.class);
      return "updated";
    }catch (Exception ex){
      return ex.getMessage();
    }
  }
  
  // Edit Contact Command (editcontact {ID} --firstname {FIRSTNAME} &&/|| --lastname {LASTNAME} &&/|| --phone {PHONE} &&/|| --email {EMAIL} &&/|| --companyid {COMPANYID})
  @ShellMethod(value = "Edit Contact", key = "editcontact", group = "Contact")
  public String putContact(int id, @ShellOption(defaultValue = ShellOption.NULL) String firstname,
                           @ShellOption(defaultValue = ShellOption.NULL) String lastname,
                           @ShellOption(defaultValue = ShellOption.NULL) String phone,
                           @ShellOption(defaultValue = ShellOption.NULL) String email,
                           @ShellOption(defaultValue = ShellOption.NULL)Integer companyid) {

    if (tokenWrapper.getToken() != null) headers.setBearerAuth(tokenWrapper.getToken());
    ObjectMapper mapper = new ObjectMapper();
    ObjectNode contactJsonObject = mapper.createObjectNode();
    
    String url = "http://localhost:8080/api/contact/" + id;
    
    if (firstname != null) contactJsonObject.put("firstname", firstname);
    if (lastname != null) contactJsonObject.put("lastname", lastname);
    if (phone != null) contactJsonObject.put("phone", phone);
    if (email != null) contactJsonObject.put("email", email);
    if (companyid != null) url += "?companyId=" + companyid;
    
    HttpEntity<String> request = new HttpEntity<>(contactJsonObject.toString(), headers);
    
    try {
      return restTemplate.exchange(url, HttpMethod.PUT, request, String.class).getBody();
    }catch (Exception ex){
      return ex.getMessage();
    }
  }
  
  // Delete Contact Command (delcontact {ID})
  @ShellMethod(value = "Delete Contact", key = "delcontact", group = "Contact")
  public String delContact (int id){
    if (tokenWrapper.getToken() != null) headers.setBearerAuth(tokenWrapper.getToken());
    HttpEntity <String> request = new HttpEntity <> (headers);

    try {
      return restTemplate.exchange("http://localhost:8080/api/contact/" + id, HttpMethod.DELETE, request, String.class).getBody();
    }catch (Exception ex){
      return ex.getMessage();
    }
  }
}
