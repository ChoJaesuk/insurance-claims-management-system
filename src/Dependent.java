//package src;
//
//import java.io.Serializable;
//import java.time.LocalDate;
//import java.util.List;
//
//public class Dependent implements Serializable {
//    private static final long serialVersionUID = 1L;
//    private String policyHolderId;
//    private String dependentId;
//    private String dependentFullName;
//
//    private InsuranceCard insuranceCard;
//    private boolean isPolicyHolder;
//    private List<Claim> claims;
//    private String policyOwner;
//    private LocalDate expirationDate;
//    public Dependent(String policyHolderId, String dependentId, String dependentFullName, InsuranceCard insuranceCard, boolean isPolicyHolder, String policyOwner, LocalDate expirationDate) {
//        this.policyHolderId = policyHolderId;
//        this.dependentId = dependentId;
//        this.dependentFullName = dependentFullName;
//        this.insuranceCard = insuranceCard;
//        this.isPolicyHolder = false;
//        this.policyOwner = policyOwner;
//        this.expirationDate = expirationDate;
//        this.isPolicyHolder = isPolicyHolder;
//    }
//
//
//    // Getters and setters
//    public String getDependentId() {
//        return dependentId;
//    }
//
//    public void setDependentId(String dependentId) {
//        this.dependentId = dependentId;
//    }
//
//    public String getDependentFullName() {
//        return dependentFullName;
//    }
//
//    public void setDependentFullName(String dependentFullName) {
//        this.dependentFullName = dependentFullName;
//    }
//
//    public InsuranceCard getInsuranceCard() {
//        return insuranceCard;
//    }
//
//    public void setInsuranceCard(InsuranceCard insuranceCard) {
//        this.insuranceCard = insuranceCard;
//    }
//
//    public boolean isPolicyHolder() {
//        return isPolicyHolder;
//    }
//
//    public void setPolicyHolder(boolean policyHolder) {
//        isPolicyHolder = policyHolder;
//    }
//
//    public String getPolicyOwner() {
//        return policyOwner;
//    }
//
//    public void setPolicyOwner(String policyOwner) {
//        this.policyOwner = policyOwner;
//    }
//
//    public LocalDate getExpirationDate() {
//        return expirationDate;
//    }
//
//    public void setExpirationDate(LocalDate expirationDate) {
//        this.expirationDate = expirationDate;
//    }
//
//    @Override
//    public String toString() {
//        return "Dependent{" +
//                "dependentId='" + dependentId + '\'' +
//                ", dependentFullName='" + dependentFullName + '\'' +
//                ", insuranceCard='" + insuranceCard + '\'' +
//                ", isPolicyHolder=" + isPolicyHolder +
//                ", policyOwner='" + policyOwner + '\'' +
//                ", expirationDate=" + expirationDate +
//                '}';
//    }
//}
