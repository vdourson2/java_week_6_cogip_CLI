package week6.java.cogipCli.cogip_CLI.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CompanyObject {
	
	private String name;
	
	private String tva;
	
	private List<Integer> invoicesId = new ArrayList<>();
	
	private List<Map<String,String>> contacts = new ArrayList<>();
	
	//Mapping nested invoices, keeping just the id for each invoice
	@SuppressWarnings("unchecked")
    @JsonProperty("invoices")
    private void unpackNestedInvoices(List<Map<String, Object>> invoices) {
		invoices.forEach((invoice) -> this.invoicesId.add((Integer)invoice.get("id"))); 
    }
	
	//Mapping nested contacts, keeping just the firstname, lastname, phone and email for each contact
	@SuppressWarnings("unchecked")
    @JsonProperty("contacts")
	private void unpackNestedContacts(List<Map<String, String>> companyContacts) {
		companyContacts.forEach((contact) -> {
			contact.remove("id");
			contact.remove("timestamp");
			this.contacts.add(contact);
		});
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTva() {
		return tva;
	}

	public void setTva(String tva) {
		this.tva = tva;
	}

	public List<Integer> getInvoicesId() {
		return invoicesId;
	}

	public void setInvoicesId(List<Integer> invoicesId) {
		this.invoicesId = invoicesId;
	}

	public List<Map<String, String>> getContacts() {
		return contacts;
	}

	public void setContacts(List<Map<String, String>> contacts) {
		this.contacts = contacts;
	}

	@Override
	public String toString() {
		return "Company [name=" + name + ", tva=" + tva + ", invoicesId=" + invoicesId + ", contacts=" + contacts
				+ "]";
	}
	
	
}
