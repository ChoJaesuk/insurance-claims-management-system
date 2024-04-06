package src;

import java.util.*;

import static src.ClaimProcessManagerImpl.deserializeClaims;
import static src.DeserializationHelper.deserializeCustomers;


public class InsurancePaymentManager {


    public void processInsurancePayment() {
        List<Claim> claims = deserializeClaims();
        List<Customer> customers = deserializeCustomers();

        System.out.println("Processing claims:");
        for (Claim claim : claims) {
            if ("Processing".equals(claim.getStatus())) {
                System.out.println(claim);
            }
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the ID of the claim to pay:");
        String selectedClaimId = scanner.nextLine();
        boolean claimFound = false; // 청구를 찾았는지 확인하는 플래그

        for(Customer customer : customers) {
            for (Claim claim : customer.getClaims()) {
                if (claim.getId().equals(selectedClaimId)) {
                    claimFound = true; // 청구를 찾았으므로 플래그를 true로 설정
                    if ("Processing".equals(claim.getStatus())) {
                        System.out.println("Enter the insured Person's Name");
                        String insuredPersonName = scanner.next();
                        if (insuredPersonName.equals(claim.getInsuredPersonFullName())) {
                            System.out.println("Banking Info for payment: " + claim.getBankingInfo());
                            System.out.println("Proceed with payment? (true/false):");
                            boolean proceed = scanner.nextBoolean();

                            if (proceed) {
                                claim.setStatus("Done");
                                String cardNumber = customer.getInsuranceCard().getCardNumber();
                                String documentName = String.format("%s_%s_" + "Insurancepaymentstatement.pdf", claim.getId(), cardNumber);
                                if (claim.getDocuments() == null) {
                                    claim.setDocuments(new ArrayList<>());
                                }
                                claim.getDocuments().add(documentName);

                                System.out.println("Payment has been processed successfully. Claim status updated to 'Done'.");
                                SerializationUtils.serialize(claim, "claim/" + claim.getId() + ".txt"); // Reserialize and save changed Claim object list
                                SerializationUtils.serialize(customer, "customer/" + customer.getId() + ".txt");
                                return;
                            } else {
                                System.out.println("Payment cancelled.");
                                return;
                            }
                        } else {
                            System.out.println("Wrong insured Person's Name! Please write his/her name to pay correctly.");
                            return;
                        }
                    }
                }
            }
        }
        if (!claimFound) {
            System.out.println("Claim not found or already processed.");
        }
    }

}
