package week6.java.cogipCli.cogip_CLI.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.Getter;
import lombok.Setter;

@ShellComponent
@Getter
@Setter
public class CompanyCommand {
	
	private RestTemplate restTemplate;
	
	@Autowired
	public CompanyCommand(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
	
	//This command list the companies with essential information: name, VAT number, associated invoices, and contacts.
	@ShellMethod(key="companies", value="Company listing with essential information: name, VAT number, associated invoices, and contacts")
	public String listCompanies(@ShellOption (defaultValue = "false") boolean pretty) {
		String url = "http://localhost:8080/company";
		
		// Fetch JSON response as String wrapped in ResponseEntity
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        String productsJson = response.getBody();
        
        return productsJson;
	}

	
	
}
