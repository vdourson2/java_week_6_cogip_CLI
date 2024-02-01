package week6.java.cogipCli.cogip_CLI.command;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.Setter;
import week6.java.cogipCli.cogip_CLI.model.CompanyObject;

@ShellComponent
@Getter
@Setter
public class CompanyCommand {
	
	private RestTemplate restTemplate;
	
	@Autowired
	public CompanyCommand(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
	
	//This command list the companies with essential informations: name, VAT number, associated invoices, and contacts.
	@ShellMethod(key="companies", value="Company listing with essential information: name, VAT number, associated invoices, and contacts")
	public String listCompanies(@ShellOption (defaultValue = "false", help="Raw or pretty") boolean pretty, 
								@ShellOption (defaultValue = "all", help = "The two ossible types are client or provider") String type) {
		
		if (!type.equals("all") && !type.equals("client") && !type.equals("provider")) {
			return "No such type of company";
		}
		
		String url = (type.equals("all")) ? "http://localhost:8080/api/company" : "http://localhost:8080/api/company/type/" + type;
		
		// Fetch JSON response as String wrapped in ResponseEntity
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
    	String productsJson = response.getBody();
		
    	//Initialize objectMapper to deserialize from Json to a list of CompanyObjects
    	ObjectMapper objectMapper = new ObjectMapper();
    	objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    	    	
    	try {
    		List<CompanyObject> companies = new ArrayList<>();
	    	companies = objectMapper.readValue(productsJson, new TypeReference<List<CompanyObject>>(){});
	    	if (pretty) {
	    		String prettyJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(companies);
	    		return prettyJson;
	    	}
	    	else {
	    		return companies.toString();
	    	}
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	return productsJson;
	}
	
}
