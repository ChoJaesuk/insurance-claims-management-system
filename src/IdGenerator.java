package src;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class IdGenerator {

    // 고객 ID와 청구 ID의 중복을 확인하기 위해 기존 ID를 저장할 세트
    private Set<String> existingCustomerIds = new HashSet<>();
    private Set<String> existingClaimIds = new HashSet<>();

    public IdGenerator() {
        loadExistingIds();
    }

    private void loadExistingIds() {
        // 고객 ID들 로드
        loadIds("customer/", existingCustomerIds);

        // 청구 ID들 로드
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
