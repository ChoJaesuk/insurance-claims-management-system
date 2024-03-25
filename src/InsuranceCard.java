package src;

import java.io.Serializable;
import java.util.*;

public class InsuranceCard implements Serializable {

    private String cardNumber;
    private String cardHolder;
    private String policyOwner;
    private Date expirationDate;

    // 생성자
    public InsuranceCard(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setCardInfo(String cardHolder, String policyOwner, Date expirationDate) {
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


    public Date getExpirationDate() {
        return expirationDate;
    }


}
