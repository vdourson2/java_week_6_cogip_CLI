package week6.java.cogipCli.cogip_CLI.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
public class InvoiceMapping {
    @Getter
    private Short id;
    @Getter
    private int company_id;
    private Object company;
    @Getter
    private int contact_id;
    private Object contact;
    @Getter
    private String timestamp;
    
    public InvoiceMapping(int id){
        this.id = (short) id;
    }
    public void setCompany(Object company) {
        this.company = company;
        unpackedNested();
    }
    
    public void setContact(Object contact) {
        this.contact = contact;
        unpackedNested();
    }
    
    public InvoiceMapping() {
    }
    @SuppressWarnings("unchecked")
    @JsonProperty("invoice")
    public void unpackedNested() {
        if (company == null) {
            System.out.println("Company object is null");
        }
        else if (!(company instanceof  Map)){
            if (company instanceof Integer){
                this.company_id = (int) company;
            }
            else{
                System.out.println("Not Mapped");
            }
        }
        else{
            Map<String, Object> companyMap = (Map<String, Object>) company;
            this.company_id = (int) companyMap.get("id");
        }
        if (contact == null) {
            System.out.println("Contact object is null");
        }
        else if (!(contact instanceof  Map)){
            if (contact instanceof Integer){
                this.contact_id = (int) contact;
            }
            else{
                System.out.println("Not Mapped");
            }
        }
        else{
            Map<String, Object> contactMap = (Map<String, Object>) contact;
            this.contact_id = (int) contactMap.get("id");
        }
    }
}