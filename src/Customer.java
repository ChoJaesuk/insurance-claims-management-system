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
    private List<Claim> claims;
    private String policyOwner;
    private LocalDate expirationDate;

    private List<Customer> dependents;

    private String dependentId;
    private String dependentFullName;

    private String claimId;

    public Customer() {

    }

    public String getClaimId() {
        return claimId;
    }

    public void setClaimId(String claimId) {
        this.claimId = claimId;
    }

    public String getDependentId() {
        return dependentId;
    }

    public String getDependentFullName() {
        return dependentFullName;
    }

    public void setDependentFullName(String dependentFullName) {
        this.dependentFullName = dependentFullName;
    }

    public void setDependentId(String dependentId) {
        this.dependentId = dependentId;
    }



    // 기타 메소드 생략...

    // 청구를 고객의 청구 리스트에 추가하는 메소드


    // 고객이 접수한 청구 정보를 반환하는 메소드




    public Customer(String id, String fullName, boolean isPolicyHolder, String policyOwner, LocalDate expirationDate, InsuranceCard insuranceCard) {

        this.id = id;
        this.fullName = fullName;
        this.isPolicyHolder = isPolicyHolder;
        this.policyOwner = policyOwner;
        this.insuranceCard = insuranceCard;
        this.expirationDate = expirationDate;
        this.dependents = new ArrayList<>();
        this.claims = new ArrayList<>();

    }

    public void addClaim(Claim claim) {
        this.claims.add(claim);
    }

    // 고객이 접수한 청구 정보를 반환하는 메소드
    public List<Claim> getClaims() {
        return this.claims;
    }
    public Customer(String dependentId, String dependentFullName, boolean isPolicyHolder, String id, LocalDate expirationDate, String policyOwner, InsuranceCard insuranceCard) {
        this.id = id;
        this.expirationDate = expirationDate;
        this.policyOwner = policyOwner;
        this.insuranceCard = insuranceCard;
        this.dependentId = dependentId;
        this.dependentFullName = dependentFullName;
        this.isPolicyHolder = isPolicyHolder;
        this.claimId = getClaimId();

    }

//    public Customer(String dependentId, String dependentFullName, boolean isPolicyHolder) {
//        this.dependentId = dependentId;
//        this.dependentFullName = dependentFullName;
//        this.isPolicyHolder = isPolicyHolder;
////        this.insuranceCard = insuranceCard;
//    }

    public void setDependentsInfo(String id, String policyOwner, LocalDate expirationDate) {
        this.id = id;
        this.policyOwner = policyOwner;
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

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = this.expirationDate;

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

    public void setClaims(List<Claim> claims) {
        this.claims = claims;
    }
//    public void setDependents(List<Customer> dependents) {
//        this.dependents = dependents;
//    }

//    public void setDependentId(String dependentId) {
//        this.dependentId = dependentId;
//    }
//
//
//    public String getDependentId() {
//        return dependentId;
//    }
//
//    public void setDependentFullName(String dependentFullName) {
//        this.dependentFullName = dependentFullName;
//    }
//
//    public String getDependentFullName() {
//        return dependentFullName;
//    }
    public List<Customer> getDependents() {
    return dependents;
    }

    public void setDependents(List<Customer> dependents) {
        this.dependents = dependents;
    }

    public void addDependent(Customer dependent) {
        this.dependents.add(dependent);
    }

    public String getDependentInfo() {
        String cardNumber = (insuranceCard != null) ? insuranceCard.getCardNumber() : "N/A";
        return "Dependent{" +
                "ID='" + dependentId + '\'' +
                ", NAME=" + dependentFullName +
                ", POLICY OWNER=" + policyOwner +
                ", INSURANCE CARD NUMBER=" + cardNumber +
                ", expirationDate=" + expirationDate +
//                ", LIST OF CLAIMS=" + claimList.toString() + // 청구 정보 출력 추가

                '}';
    }
    @Override
    public String toString() {
        String cardNumber = (insuranceCard != null) ? insuranceCard.getCardNumber() : "N/A";
        StringBuilder dependentList = new StringBuilder();
        if (dependents != null) {
            for (Customer dependent : dependents) {
                dependentList.append(dependent.getDependentId()).append("(").append(dependent.getDependentFullName()).append("), ");
            }
            // 리스트의 마지막 ", " 제거
            if (dependentList.length() > 0) {
                dependentList.delete(dependentList.length() - 2, dependentList.length());
            }
        } else {
            dependentList.append("N/A");
        }


        StringBuilder claimList = new StringBuilder();
        if (claims != null) {
            for (Claim claim : claims) {
                claimList.append(claim.toString()).append(", ");
            }
            // 리스트의 마지막 ", " 제거
            if (claimList.length() > 0) {
                claimList.delete(claimList.length() - 2, claimList.length());
            }
        } else {
            claimList.append("N/A");
        }


        return "Customer{" +
                "ID='" + id + '\'' +
                ", NAME='" + fullName + '\'' +
                ", POLICY HOLDER=" + isPolicyHolder +
                ", POLICY OWNER=" + policyOwner +
                ", INSURANCE CARD NUMBER=" + cardNumber +
                ", expirationDate=" + expirationDate +
                ", LIST OF CLAIMS=" + claimList.toString() + // 청구 정보 출력 추가
                ", LIST OF DEPENDENTS=" + dependentList +
                '}';
    }

}
