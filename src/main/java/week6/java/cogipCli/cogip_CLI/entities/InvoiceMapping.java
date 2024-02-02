package week6.java.cogipCli.cogip_CLI.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
public class InvoiceMapping {
    @Getter
    private Short id;
    @Getter
    private String timestamp;
    @Getter
    private int company_id;
    private Object company;
    @Getter
    private int contact_id;
    private Object contact;

    public void setCompany(Object company) {
        this.company = company;
        unpackedNested();
    }

    public void setContact(Object contact) {
        this.contact = contact;
        unpackedNested();
    }

    @JsonProperty("contacts")
    public void unpackedNested() {
        if (company == null) {
            System.out.println("Company object is null");
        }
        else if (!(company instanceof  Map)){
            if (company instanceof Integer){
                company_id = (int) company;
            }
            else{
                System.out.println("Not Mapped");
            }
        }
        else{
            Map<String, Object> companyMap = (Map<String, Object>) company;
            company_id = (int) companyMap.get("id");
        }

        if (contact == null) {
            System.out.println("Contact object is null");
        }
        else if (!(contact instanceof  Map)){
            if (contact instanceof Integer){
                contact = (int) contact;
            }
            else{
                System.out.println("Not Mapped");
            }
        }
        else{
            Map<String, Object> companyMap = (Map<String, Object>) contact;
            contact = (int) companyMap.get("id");
        }
    }
}
