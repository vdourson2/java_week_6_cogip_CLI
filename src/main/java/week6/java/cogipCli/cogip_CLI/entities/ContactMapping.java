package week6.java.cogipCli.cogip_CLI.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

//@Getter
@Setter
public class ContactMapping {
  @Getter
  private Short id;
  @Getter
  private String firstName;
  @Getter
  private String lastName;
  @Getter
  private String phone;
  @Getter
  private String email;
  @Getter
  private int company_id;
  @Getter
  private String timestamp;
  private Object company;
  @JsonIgnore
  private Object invoices;
  
  public void setCompany(Object company) {
    this.company = company;
    unpackedNested();
  }
  
  @SuppressWarnings("unchecked")
  @JsonProperty("contacts")
  public void unpackedNested() {
    if (this.company == null) {
      System.out.println("Company object is null");
      return;
    }
    if (this.company instanceof Integer){
      this.company_id = (int) this.company;
      return;
    }
    else if (!(this.company instanceof Map)) {
      System.out.println("Not Mapped");
      return;
    }
    Map<String, Object> companyMap = (Map<String, Object>) this.company;
    this.company_id = (int) companyMap.get("id");
  }
}
