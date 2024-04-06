package src;

import java.io.*;
import java.util.*;
import java.time.LocalDate;

public class InsuranceCard implements Serializable {
    private static final long serialVersionUID = 1L;
    private String cardNumber;
    private String cardHolder;
    private String policyOwner;
    private LocalDate expirationDate;

    private Set<String> existingCardNumbers;

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public InsuranceCard() {
        existingCardNumbers = new HashSet<>();
        loadExistingCardNumbers();
    }

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

    private void loadExistingCardNumbers() {
        File directory = new File("insurancecard");
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                    InsuranceCard card = (InsuranceCard) ois.readObject();
                    existingCardNumbers.add(card.getCardNumber());
                } catch (Exception e) {
                    System.err.println("Error reading insurance card file: " + e.getMessage());
                }
            }
        }
    }

    public String generateRandomCardNumber() {
        Random random = new Random();
        String cardNumber;
        do {
            StringBuilder cardNumberBuilder = new StringBuilder();
            for (int i = 0; i < 10; i++) {
                int digit = random.nextInt(10);
                cardNumberBuilder.append(digit);
            }
            cardNumber = cardNumberBuilder.toString();
        } while (existingCardNumbers.contains(cardNumber));
        existingCardNumbers.add(cardNumber);
        return cardNumber;
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
