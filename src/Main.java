package src;

import java.util.*;
public class Main {
    public static void main(String[] args) {
        CustomerManagerImpl CService = new CustomerManagerImpl();
        ClaimProcessManagerImpl service = new ClaimProcessManagerImpl();

        InsuranceClaimManager insuranceClaimManager = new InsuranceClaimManager(CService, service);
        insuranceClaimManager.start();
    }
}
