package src;

import java.io.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;


public class Claim implements Serializable {

    private String id;
    private LocalDate claimDate;
    private String insuredPersonId;
    private String insuredPersonFullName;
    private int cardNumber;
    private LocalDate examDate;
    private List<String> documents;
    private double claimAmount;
    private String status;
    private String receiverBankingInfo;

    private InsuranceCard insuranceCard;

    private Customer customer;


    public Claim(String claimId, String customerId, String customerFullName, InsuranceCard cardNumber ,LocalDate claimDate, LocalDate examDate, double claimAmount) {
        this.id = claimId;
        this.claimDate = claimDate;
        this.insuredPersonId = customerId;
        this.insuranceCard = cardNumber;
        this.insuredPersonFullName = customerFullName;
        this.examDate = examDate;
        this.claimAmount = claimAmount;
        this.status = "New"; // status 초기화

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

//    public String getStatus() {
//        return status;
//    }

//    public void setStatus(String status) {
//        this.status = status;
//    }

    public LocalDate getClaimDate() {
        return claimDate;
    }

    public void setClaimDate(LocalDate claimDate) {
        this.claimDate = claimDate;
    }

    public int getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(int cardNumber) {
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

    public void setInsuredPersonFullName(String InsuredPersonFullName) {
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
        String cardNumber = (insuranceCard != null) ? insuranceCard.getCardNumber() : "N/A";
        return "Claim{" +
                "CLAIM ID='" + id + '\'' +
                "CUSTOMER ID='" + insuredPersonId + '\'' +
                ", NAME='" + insuredPersonFullName + '\'' +
                ", INSURANCE CARD NUMBER=" + cardNumber+
                ", CLAIM DATE=" + claimDate +
                ", EXAM DATE=" + examDate +
                ", CLAIM AMOUNT=" + claimAmount +
                ", STATUS=" + status + // 상태 정보 추가
                '}';
    }
}

