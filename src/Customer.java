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
// Customer 클래스 내에 추가

    public boolean hasDependent(String dependentId) {
        if (dependents != null) {
            for (Customer dependent : dependents) {
                if (dependent.getId().equals(dependentId)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void updateDependent(Customer updatedDependent) {
        if (dependents != null) {
            for (int i = 0; i < dependents.size(); i++) {
                Customer dependent = dependents.get(i);
                if (dependent.getId().equals(updatedDependent.getId())) {
                    dependents.set(i, updatedDependent);
                    return;
                }
            }
        }
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
        StringBuilder sb = new StringBuilder();
        sb.append("ID: ").append(id).append("\n")
                .append("이름: ").append(fullName).append("\n")
                .append("Policy Holder: ").append(isPolicyHolder ? "Yes" : "No").append("\n")
                .append("Policy Owner: ").append(policyOwner).append("\n");

        // 보험 카드 정보가 있는 경우
        if (insuranceCard != null) {
            sb.append("보험 카드 번호: ").append(insuranceCard.getCardNumber()).append("\n")
                    .append("보험 만료일: ").append(insuranceCard.getExpirationDate()).append("\n");
        } else {
            sb.append("보험 카드 정보가 없습니다.\n");
        }

        if (claims != null && !claims.isEmpty()) {
            sb.append("클레임 목록:\n");
            for (Claim claim : claims) {
                sb.append("\t클레임 ID: ").append(claim.getId()).append("\n")
                        .append("\t클레임 날짜: ").append(claim.getClaimDate()).append("\n")
                        .append("\tInsured person: ").append(claim.getInsuredPersonFullName()).append("\n")
                        .append("\tCard number: ").append(insuranceCard.getCardNumber()).append("\n")
                        .append("\t검사 날짜: ").append(claim.getExamDate()).append("\n")
                        .append("\t청구 금액: ").append(claim.getClaimAmount()).append("\n")
                        .append("\t상태: ").append(claim.getStatus()).append("\n")
                        .append("\t수령인 은행 정보: ").append(claim.getReceiverBankingInfo()).append("\n\n");
            }
        } else {
            sb.append("클레임 정보가 없습니다.\n");
        }

        // 종속자 정보는 Policy Holder일 경우에만 출력
        if (isPolicyHolder && dependents != null && !dependents.isEmpty()) {
            sb.append("종속자 목록:\n");
            for (Customer dependent : dependents) {
                sb.append("\tID: ").append(dependent.getId()).append("\n")
                        .append("\t이름: ").append(dependent.getFullName()).append("\n")
                        // 종속자의 보험 카드 번호 출력
                        .append("\t보험 카드 번호: ").append(dependent.getInsuranceCard() != null ? dependent.getInsuranceCard().getCardNumber() : "정보 없음").append("\n\n");
            }
        }


        return sb.toString();
    }

}
