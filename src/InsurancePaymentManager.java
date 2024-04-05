package src;

import java.util.*;

import static src.ClaimProcessManagerImpl.deserializeClaims;
import static src.CustomerManagerImpl.serializeObject;
import static src.DeserializationHelper.deserializeCustomers;


public class InsurancePaymentManager {


    public void processInsurancePayment() {
        List<Claim> claims = deserializeClaims();
        List<Customer> customers = deserializeCustomers();

        System.out.println("Processing claims:");
        for (Claim claim : claims) {
            if ("Processing".equals(claim.getStatus())) {
                System.out.println(claim); // 여기서 Claim 클래스에 toString 메서드가 오버라이드 되어 있다고 가정
            }
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the ID of the claim to pay:");
        String selectedClaimId = scanner.nextLine();

        for(Customer customer : customers) {
            for (Claim claim : customer.getClaims()) {
                if (claim.getId().equals(selectedClaimId) && "Processing".equals(claim.getStatus())) {
                    System.out.println("Enter the insured Person's Name");
                    String insuredPersonName = scanner.next();
                    if (insuredPersonName.equals(claim.getInsuredPersonFullName())) {
                        System.out.println("Banking Info for payment: " + claim.getBankingInfo());
                        System.out.println("Proceed with payment? (true/false):");
                        boolean proceed = scanner.nextBoolean();

                        if (proceed) {
                            claim.setStatus("Done");
                            String cardNumber = customer.getInsuranceCard().getCardNumber(); // 여기서는 Customer 객체에서 InsuranceCard를 가져와 사용
                            String documentName = String.format("%s_%s_\n" + "Insurance payment statement.pdf", claim.getId(), cardNumber);
                            if (claim.getDocuments() == null) {
                                claim.setDocuments(new ArrayList<>());
                            }
                            claim.getDocuments().add(documentName);

                            System.out.println("Payment has been processed successfully. Claim status updated to 'Done'.");
                            serializeObject(claim, "claim/" + claim.getId() + ".txt");// 변경된 Claim 객체 리스트를 다시 직렬화하여 저장
                            serializeObject(customer, "customer/" + customer.getId() + ".txt");
                            return;
                        } else {
                            System.out.println("Payment cancelled.");
                            return;
                        }
                    }
                    System.out.println("Wrong insured Person's Name! Please wrtie him/her name to pay correctly.");
                }

                System.out.println("Claim not found or already processed.");
            }
        }
        }

}
