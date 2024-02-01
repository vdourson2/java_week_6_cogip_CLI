package week6.java.cogipCli.cogip_CLI.command;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

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
								@ShellOption (defaultValue = "all", help = "The two possible types are client or provider") String type) {
		
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
    		//Deserialize from Json to a list of CompanyObject
    		List<CompanyObject> companies = new ArrayList<>();
	    	companies = objectMapper.readValue(productsJson, new TypeReference<List<CompanyObject>>(){});
	    	if (pretty) { //Pretty display
	    		String prettyJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(companies);
	    		return prettyJson;
	    	}
	    	else { //Raw display
	    		return companies.toString();
	    	}
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	return productsJson;
	}
	
	//Register a new company
	@ShellMethod(key="add_company", value="Register a new Company with its name, country, vat and type")
	public String addCompany(@ShellOption (defaultValue = "false", help="Raw or pretty") boolean pretty, 
								@ShellOption (help = "Name of the Company") String name,
								@ShellOption (help = "Country of the Company") String country,
								@ShellOption (help = "VAT of the Company") String tva,
								@ShellOption (help = "client or provider") String type) {
	
		String createUrl = "http://localhost:8080/api/company";
		
		//Create ObjectNode for the company to add
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode companyToAdd = objectMapper.createObjectNode();
		companyToAdd.put("name", name);
		companyToAdd.put("country", country);
		companyToAdd.put("tva", tva);
		companyToAdd.put("type", type);
		
		//Create HTTP entity with The ObjectNode
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> request = new HttpEntity<String>(companyToAdd.toString(), headers);
			    
		//POST company
		String companyResultAsJsonStr = restTemplate.postForObject(createUrl, request, String.class);
		
		return companyResultAsJsonStr;
	}
	
}
