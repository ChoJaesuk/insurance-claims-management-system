package src;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class IdGenerator {

    // Set to store existing IDs to verify duplication of customer ID and billing ID
    private Set<String> existingCustomerIds = new HashSet<>();
    private Set<String> existingClaimIds = new HashSet<>();

    public IdGenerator() {
        loadExistingIds();
    }

    private void loadExistingIds() {
        // Load customer IDs
        loadIds("customer/", existingCustomerIds);

        // Load Claims IDs
        loadIds("claim/", existingClaimIds);
    }

    private void loadIds(String directoryPath, Set<String> idSet) {
        File directory = new File(directoryPath);
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                    Object object = ois.readObject();
                    if (object instanceof Customer) {
                        idSet.add(((Customer)object).getId());
                    } else if (object instanceof Claim) {
                        idSet.add(((Claim)object).getId());
                    }
                } catch (Exception e) {
                    System.err.println("Error reading object from file: " + e.getMessage());
                }
            }
        }
    }

    public String generateUniqueId(Set<String> existingIds, String prefix, int digits) {
        Random random = new Random();
        String id;
        do {
            id = prefix + random.ints((int)Math.pow(10, digits - 1), (int)Math.pow(10, digits))
                    .findFirst()
                    .getAsInt();
        } while (existingIds.contains(id));
        existingIds.add(id);
        return id;
    }

    public String generateCustomerId() {
        return generateUniqueId(existingCustomerIds, "c-", 7);
    }

    public String generateClaimId() {
        return generateUniqueId(existingClaimIds, "f-", 10);
    }

}
