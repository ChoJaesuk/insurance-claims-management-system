package src;

import java.util.*;

import static src.ClaimProcessManagerImpl.deserializeClaims;
import static src.CustomerManagerImpl.serializeObject;


public class InsurancePaymentManager {


    public void processInsurancePayment() {
        List<Claim> claims = deserializeClaims();

        System.out.println("Processing claims:");
        for (Claim claim : claims) {
            if ("Processing".equals(claim.getStatus())) {
                System.out.println(claim); // 여기서 Claim 클래스에 toString 메서드가 오버라이드 되어 있다고 가정
            }
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the ID of the claim to pay:");
        String selectedClaimId = scanner.nextLine();

        for (Claim claim : claims) {
            if (claim.getId().equals(selectedClaimId) && "Processing".equals(claim.getStatus())) {
                System.out.println("Banking Info for payment: " + claim.getBankingInfo());
                System.out.println("Proceed with payment? (true/false):");
                boolean proceed = scanner.nextBoolean();

                if (proceed) {
                    claim.setStatus("Done");
                    System.out.println("Payment has been processed successfully. Claim status updated to 'Done'.");
                    serializeObject(claim, "claim/" + claim.getId() + ".txt");// 변경된 Claim 객체 리스트를 다시 직렬화하여 저장
                    return;
                } else {
                    System.out.println("Payment cancelled.");
                    return;
                }
            }
        }

        System.out.println("Claim not found or already processed.");
    }
}
