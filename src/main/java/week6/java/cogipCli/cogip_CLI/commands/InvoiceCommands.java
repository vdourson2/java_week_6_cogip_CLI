package week6.java.cogipCli.cogip_CLI.commands;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.web.client.RestTemplate;
import week6.java.cogipCli.cogip_CLI.entities.ContactMapping;
import week6.java.cogipCli.cogip_CLI.entities.InvoiceMapping;

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
                .readerFor(new TypeReference<List<ContactMapping>>() {})
                .readValue(restTemplate.getForObject("http://localhost:8080/api/invoice", String.class));

        String json = new ObjectMapper().writeValueAsString(invoicesMappings);
        return getString(pretty, json);

    }

}
