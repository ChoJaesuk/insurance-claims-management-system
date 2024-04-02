package src;

public class ReceiverBankInfo {

    private String bank;
    private String name;
    private String number;

    public ReceiverBankInfo(String bank, String name, String number) {

        this.bank = bank;
        this.name = name;
        this.number = number;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
