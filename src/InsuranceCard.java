package src;

import java.io.Serializable;
import java.util.*;
import java.time.LocalDate;

public class InsuranceCard implements Serializable {
    private static final long serialVersionUID = 1L;
    private String cardNumber;
    private String cardHolder;
    private String policyOwner;
    private LocalDate expirationDate;

    // 생성자
    public InsuranceCard(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setCardInfo(String cardHolder, String policyOwner, LocalDate expirationDate) {
        this.cardHolder = cardHolder;
        this.policyOwner = policyOwner;
        this.expirationDate = expirationDate;
    }



    public String getCardNumber() {
        return cardNumber;
    }


    public String getCardHolder() {
        return cardHolder;
    }


    public String getPolicyOwner() {
        return policyOwner;
    }


    public LocalDate getExpirationDate() {
        return expirationDate;
    }
    @Override
    public String toString() {
        return "InsuranceCard{" +
                "cardNumber='" + cardNumber + '\'' +
                '}';
    }

}
