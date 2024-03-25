package src;

import java.io.*;
import java.util.*;
public class Customer implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String fullName;
    private InsuranceCard insuranceCard;
    private boolean isPolicyHolder;
    private List<Customer> dependents;
    private List<Claim> claims;

    private String policyOwner;

    public Customer() {

    }

    public Customer(String id, String fullName, boolean isPolicyHolder, String policyOwner) {
        this.id = id;
        this.fullName = fullName;
        this.isPolicyHolder = isPolicyHolder;
        this.policyOwner = policyOwner;
    }

    public Customer(String id, String fullName, String insuranceCard, String dependents, String claims) {
        this.id = id;
        this.fullName = fullName;
        this.dependents = new ArrayList<>();
        this.claims = new ArrayList<>();
    }

    public void setPolicyOwner(String policyOwner) { this.policyOwner = this.policyOwner; }
    public String getPolicyOwner() { return policyOwner; }
    public void setIsPolicyHolder() { this.isPolicyHolder = isPolicyHolder; }
    public boolean getIsPolicyHolder() { return isPolicyHolder; }
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
    public void setInsuranceCard(InsuranceCard insuranceCard) {
        this.insuranceCard = insuranceCard;
    }

    public void setPolicyHolder(boolean answer) { this.policyOwner = policyOwner;}
    public String getPolicyOwner(String policyOwner) { return policyOwner; }
    public InsuranceCard getInsuranceCard() {
        return insuranceCard;
    }
    public void createInsuranceCard(String cardNumber, String cardHolder, String policyOwner, Date expirationDate) {
        this.insuranceCard = new InsuranceCard(cardNumber);
        this.insuranceCard.setCardInfo(cardHolder, policyOwner, expirationDate);
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
    public String getCustomerInfoString() {
        StringBuilder info = new StringBuilder();
        info.append("Customer ID: ").append(id).append("\n");
        info.append("Customer Name: ").append(fullName).append("\n");
        info.append("Is Policy Holder: ").append(isPolicyHolder).append("\n");
        info.append("Policy Owner: ").append(policyOwner).append("\n");
        return info.toString();
    }

}
