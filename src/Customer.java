package src;

import java.util.*;
public class Customer {

    private String id;
    private String fullName;
    private String insuranceCard;
    private List<Customer> dependents;
    private List<Claim> claims;

    public Customer() {

    }

    public Customer(String id, String fullName, String insuranceCard, List<Customer>dependents, List<Claim> claims) {
        this.id = id;
        this.fullName = fullName;
        this.insuranceCard = insuranceCard;
        this.claims = claims;
        this.dependents = dependents;
    }

    public Customer(String id, String fullName, String insuranceCard, String dependents, String claims) {
        this.id = id;
        this.fullName = fullName;
        this.insuranceCard = insuranceCard;
        this.dependents = new ArrayList<>();
        this.claims = new ArrayList<>();
    }


    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public String getInsuranceCard() {
        return insuranceCard;
    }
    public void setInsuranceCard(String insuranceCard) {
        this.insuranceCard = insuranceCard;
    }
    public List<Claim> getClaims() {
        return claims;
    }
    public void setClaims(List<Claim> claims) {
        this.claims = claims;
    }
    public List<Customer> getDependents() {
        return dependents;
    }
    public void setDependents(List<Customer> dependents) {
        this.dependents = dependents;
    }

}
