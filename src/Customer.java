package src;

import java.io.*;
import java.util.*;
import java.time.LocalDate;
public class Customer implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String fullName;
    private InsuranceCard insuranceCard;
    private boolean isPolicyHolder;
    private List<Customer> dependents;
    private List<Claim> claims;

    private String policyOwner;
    private LocalDate expirationDate;

    public Customer() {

    }

//    public Customer(String id, String fullName, boolean isPolicyHolder, String policyOwner) {
//        this.id = id;
//        this.fullName = fullName;
//        this.isPolicyHolder = isPolicyHolder;
//        this.policyOwner = policyOwner;
//    }


    public Customer(String id, String fullName, boolean isPolicyHolder, String policyOwner, LocalDate expirationDate, InsuranceCard insuranceCard) {
        this.id = id;
        this.fullName = fullName;
        this.isPolicyHolder = isPolicyHolder;
        this.policyOwner = policyOwner;
        this.insuranceCard = insuranceCard;
        this.expirationDate = expirationDate;


    }

    public void setPolicyOwner(String policyOwner) {
        this.policyOwner = policyOwner;
    }
    public String getPolicyOwner() { return policyOwner; }
    public void setIsPolicyHolder(boolean isPolicyHolder) {
        this.isPolicyHolder = isPolicyHolder;
    }
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

    public void setExpirationDate() {
        this.expirationDate = expirationDate;

    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setPolicyOwner() {
        this.policyOwner = policyOwner;
    }
    public String getPolicyOwner(String policyOwner) { return policyOwner; }
    public InsuranceCard getInsuranceCard() {
        return insuranceCard;
    }
    public void createInsuranceCard(String cardNumber, String cardHolder, String policyOwner, LocalDate expirationDate) {
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

    @Override
    public String toString() {
        String cardNumber = (insuranceCard != null) ? insuranceCard.getCardNumber() : "N/A";
        return "Customer{" +
                "ID='" + id + '\'' +
                ", NAME='" + fullName + '\'' +
                ", POLICY HOLDER=" + isPolicyHolder +
                ", POLICY OWNER=" + policyOwner +
                ", INSURANCE CARD NUMBER=" + cardNumber+
                ", expirationDate=" + expirationDate +
                '}';
    }






}
