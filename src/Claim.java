package src;

import java.io.*;
import java.time.LocalDate;
import java.util.*;


public class Claim implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private LocalDate claimDate;
    private String insuredPersonId;
    private String insuredPersonFullName;
    private String policyHolderFullName;
    private InsuranceCard cardNumber;
    private LocalDate examDate;
    private List<String> documents;
    private double claimAmount;
    private String status;
    private String receiverBankingInfo;

    private InsuranceCard insuranceCard;


    private ReceiverBankingInfo bankingInfo;


    public String getPolicyHolderFullName() {
        return policyHolderFullName;
    }

    public void setPolicyHolderFullName(String policyHolderFullName) {
        this.policyHolderFullName = policyHolderFullName;
    }

    public String getInsuredPersonId() {
        return insuredPersonId;
    }

    public void setInsuredPersonId(String insuredPersonId) {
        this.insuredPersonId = insuredPersonId;
    }

    public Claim(String claimId, String customerId, String insuredPersonFullName, InsuranceCard cardNumber , LocalDate claimDate, LocalDate examDate, double claimAmount, ReceiverBankingInfo bankingInfo) {
        this.id = claimId;
        this.claimDate = claimDate;
        this.insuredPersonId = customerId;
        this.insuredPersonFullName = insuredPersonFullName;
        this.insuranceCard = cardNumber;
        this.examDate = examDate;
        this.claimAmount = claimAmount;
        this.status = "New"; // initialize status
        this.bankingInfo = bankingInfo;
        this.documents = new ArrayList<>();


    }

    public Claim(String id, String status, double claimAmount, ReceiverBankingInfo bankingInfo) {
        this.id = id;
        this.status = status;
        this.claimAmount = claimAmount;
        this.bankingInfo = bankingInfo;
    }

    public ReceiverBankingInfo getBankingInfo() {
        return bankingInfo;
    }

    public void setBankingInfo(ReceiverBankingInfo bankingInfo) {
        this.bankingInfo = bankingInfo;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInsuredPerson() {
        return insuredPersonId;
    }

    public void setInsuredPerson(String insuredPerson) {
        this.insuredPersonId = insuredPerson;
    }

    public LocalDate getExamDate() {
        return examDate;
    }

    public void setExamDate(LocalDate examDate) {
        this.examDate = examDate;
    }

    public double getClaimAmount() {
        return claimAmount;
    }

    public void setClaimAmount(double claimAmount) {
        this.claimAmount = claimAmount;
    }


    public LocalDate getClaimDate() {
        return claimDate;
    }

    public void setClaimDate(LocalDate claimDate) {
        this.claimDate = claimDate;
    }

    public InsuranceCard getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(InsuranceCard cardNumber) {
        this.cardNumber = cardNumber;
    }

    public List<String> getDocuments() {
        return documents;
    }

    public void setDocuments(List<String> documents) {
        this.documents = documents;
    }

    public String getReceiverBankingInfo() {
        return receiverBankingInfo;
    }

    public void setReceiverBankingInfo(String receiverBankingInfo) {
        this.receiverBankingInfo = receiverBankingInfo;
    }

    public String getInsuredPersonFullName() {
        return insuredPersonFullName;
    }

    public void setInsuredPersonFullName(String insuredPersonFullName) {
        this.insuredPersonFullName = insuredPersonFullName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String cardNumber = (insuranceCard != null) ? insuranceCard.getCardNumber() : "N/A";

        // Basic Information Section
        sb.append("Claim Information\n")
                .append("==================\n")
                .append("CLAIM ID: ").append(id).append("\n")
                .append("CUSTOMER ID: ").append(insuredPersonId).append("\n")
                .append("INSURED PERSON NAME: ").append(insuredPersonFullName).append("\n")
                .append("INSURANCE CARD NUMBER: ").append(cardNumber).append("\n");

        // Date Information Section
        sb.append("\nDate Information\n")
                .append("----------------\n")
                .append("CLAIM DATE: ").append(claimDate).append("\n")
                .append("EXAM DATE: ").append(examDate).append("\n");

        // Financial Information Section
        sb.append("\nFinancial Information\n")
                .append("---------------------\n")
                .append("CLAIM AMOUNT: ").append(claimAmount).append("\n")
                .append("STATUS: ").append(status).append("\n");

        // Additional section: For example, recipient bank information
        sb.append("\nReceiver Banking Info\n")
                .append("----------------------\n")
                .append("BANKING INFO: ").append(getBankingInfo().toString()).append("\n");

        return sb.toString();
    }

}

