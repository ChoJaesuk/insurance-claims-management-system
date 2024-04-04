package src;

import java.io.Serializable;

public class ReceiverBankingInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String bankName;
    private String receiverName;
    private String bankingNumber;

    public ReceiverBankingInfo(String bankName, String receiverName, String bankingNumber) {

        this.bankName = bankName;
        this.receiverName = receiverName;
        this.bankingNumber = bankingNumber;

    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getBankingNumber() {
        return bankingNumber;
    }

    public void setBankingNumber(String bankingNumber) {
        this.bankingNumber = bankingNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    @Override
    public String toString() {
        return "은행 이름: " + bankName + ", 계좌 번호: " + bankingNumber + ", 수령인 이름: " + receiverName;
    }

}
