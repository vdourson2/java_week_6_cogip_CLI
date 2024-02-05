package week6.java.cogipCli.cogip_CLI.commands;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.web.client.RestTemplate;
import week6.java.cogipCli.cogip_CLI.model.InvoiceMapping;

import java.util.List;

@ShellComponent
@ShellCommandGroup("Invoice")
public class InvoiceCommands {

    RestTemplate restTemplate = new RestTemplate();

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

    // Get all invoices command.
    // Command : allinvoices (--pretty)
    @ShellMethod(value = "Get All Invoices", key = "allinvoices")
    public String getAllInvoices(@ShellOption(value = {"--pretty"})boolean pretty) throws JsonProcessingException {
        List<InvoiceMapping> invoicesMappings = new ObjectMapper()
                .readerFor(new TypeReference<List<InvoiceMapping>>() {})
                .readValue(restTemplate.getForObject("http://localhost:8080/api/invoice", String.class));

        String json = new ObjectMapper().writeValueAsString(invoicesMappings);
        return getString(pretty, json);
    }
    
    // Get Invoice By ID Command (contactid {ID}, contactid {ID} --pretty)
    @ShellMethod(value = "Get Invoices by ID", key = "invoiceid")
    public String getInvoiceById(int id, @ShellOption(value = {"--pretty"}) boolean pretty) throws JsonProcessingException {
        InvoiceMapping invoiceMappings = new ObjectMapper()
                .readerFor(new TypeReference<InvoiceMapping>() {})
                .readValue(restTemplate.getForObject("http://localhost:8080/api/invoice/" + id, String.class));
        
        String json = new ObjectMapper().writeValueAsString(invoiceMappings);
        return getString(pretty, json);
    }
    
    @ShellMethod(value = "Post Invoice", key = "addinvoice")
    public String postInvoice(Integer contactId, Integer companyId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode contactJsonObject = mapper.createObjectNode();
        
        
        HttpEntity<String> request = new HttpEntity<>(contactJsonObject.toString(), headers);
        
        return restTemplate.postForObject("http://localhost:8080/api/invoice?companyId=" + companyId + "&contactId=" + contactId, request, String.class);
    }
    
    @ShellMethod(value = "Edit Invoice", key = "editinvoice")
    public String putInvoice(int id, @ShellOption(defaultValue = ShellOption.NULL) Integer companyId,
                             @ShellOption(defaultValue = ShellOption.NULL) Integer contactId){
        System.out.println(companyId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode contactJsonObject = mapper.createObjectNode();
        
        String url = "http://localhost:8080/api/invoice/" + id;
        
        if (companyId != null) url += "?companyId=" + companyId;
        if (contactId != null) url += "?contactId=" + contactId;
        if (companyId != null && contactId != null) url += "?companyId=" + companyId + "&contactId=" + contactId;
        
        HttpEntity<String> request = new HttpEntity<>(contactJsonObject.toString(), headers);
        
        return restTemplate.exchange(url, HttpMethod.PUT, request, String.class).getBody();
    }
    
    @ShellMethod(value = "Delete invoice", key = "delinvoice")
    public void delContact (int id){
        restTemplate.delete("http://localhost:8080/api/invoice/" + id, HttpMethod.DELETE, String.class);
    }
    
}
