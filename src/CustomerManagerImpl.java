package src;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

import static src.ClaimProcessManagerImpl.deserializeClaims;
import static src.DeserializationHelper.deserializeCustomers;

public class CustomerManagerImpl implements CustomerManager {
    private static List<Customer> list = new ArrayList<>();
    private Scanner scan = new Scanner(System.in);


    @Override
    public void addCustomer() {

        InsuranceCard insuranceCard2 = new InsuranceCard();
        IdGenerator idGenerator = new IdGenerator();

        String id = idGenerator.generateCustomerId();

        System.out.println("Enter the customer's Full Name");
        String fullName = scan.next();

        System.out.println("Enter the Expiration Date (ex : 2024-12-31): ");
        String dateInput = scan.next();
        LocalDate expirationDate = LocalDate.parse(dateInput);

        System.out.println("is the customer Policy Holder? (true)");
        boolean answer = scan.nextBoolean();

        System.out.println("Enter customer's Policy owner");
        String policyOwner = scan.next();

        // 보험 카드 생성
        String cardNumber = insuranceCard2.generateRandomCardNumber(); // 중복 검사를 포함한 카드 번호 생성
        InsuranceCard insuranceCard = new InsuranceCard(cardNumber);
        insuranceCard.setCardInfo(fullName, policyOwner, expirationDate);

        // 고객 객체 생성
        Customer cus = new Customer(id, fullName, answer, policyOwner, expirationDate, insuranceCard);

        // 고객 리스트에 추가
        list.add(cus);

        // 직렬화
        serializeObject(cus, "customer/" + cus.getId() + ".txt");
        serializeObject(insuranceCard, "insuranceCard/" + insuranceCard.getCardNumber() + ".txt");
        System.out.println(fullName + "has been successfully registered.");


    }

    public void addDependent(String policyHolderId) {

        InsuranceCard insuranceCard2 = new InsuranceCard();
        IdGenerator idGenerator = new IdGenerator();

        // PolicyHolder 찾기
        Customer policyHolder = findCustomerById(policyHolderId);
        if (policyHolder == null) {
            System.out.println("There is no PolicyHolder with the ID you entered.");
            return;
        }

        // Dependent 정보 입력 받기
        String dependentId = idGenerator.generateCustomerId();

        System.out.println("Enter the dependent's Full Name");
        String dependentFullName = scan.next();

        String cardNumber = insuranceCard2.generateRandomCardNumber(); // 보험 카드 번호 생성
        InsuranceCard dependentInsuranceCard = new InsuranceCard(cardNumber);
        dependentInsuranceCard.setCardInfo(dependentFullName, policyHolder.getPolicyOwner(), policyHolder.getInsuranceCard().getExpirationDate());

        // Dependent 객체 생성
        Customer dependent = new Customer(dependentId, dependentFullName, false, policyHolder.getPolicyOwner(), policyHolder.getExpirationDate(), dependentInsuranceCard);

        // PolicyHolder의 dependents 리스트에 추가
        if (policyHolder.getDependents() == null) {
            policyHolder.setDependents(new ArrayList<>());
        }
        policyHolder.getDependents().add(dependent);

        // 변경된 PolicyHolder 정보 직렬화
        serializeObject(policyHolder, "customer/" + policyHolder.getId() + ".txt");
        serializeObject(dependent, "customer/" + dependentId + ".txt");
        serializeObject(dependentInsuranceCard, "insuranceCard/" + dependentInsuranceCard.getCardNumber() + ".txt");

        System.out.println(dependentFullName + "has been successfully registered.");
    }

    private Customer findCustomerById(String customerId) {
        // 이 메소드는 deserializeCustomers를 사용하여 모든 고객을 가져오고,
        // 주어진 customerId와 일치하는 Customer 객체를 찾아 반환합니다.
        List<Customer> customers = deserializeCustomers(); // 가정: 이 메소드가 시스템의 모든 고객을 역직렬화하여 반환
        for (Customer customer : customers) {
            if (customer.getId().equals(customerId)) {
                return customer;
            }
        }
        return null; // 찾지 못했으면 null 반환
    }

    @Override
    public void updateCustomer() {
        listCustomersWithoutDependents();

        System.out.println("Enter the Customer ID to update (NOT DEPENDENT)");
        String customerId = scan.next();

        // 고객 찾기
        Customer customerToUpdate = findCustomerById(customerId);
        if (customerToUpdate == null) {
            System.out.println("There is no Customer with the ID you entered.");
            return;
        }

        System.out.println("Please choose an option to update");
        System.out.println("## [1] Customer's ID [2] Customer's Full Name [3] Customer's Expiration Date ##");
        System.out.println("## [4] Add Dependent [5] Delete Dependent [6] Update Dependent(ID, FullName) [7] 취소 ##");
        int choice = scan.nextInt();
        scan.nextLine(); // 숫자 입력 후 남은 줄바꿈 문자 제거

        boolean customerUpdated = false;

        switch (choice) {
            case 1:
                System.out.println("Enter a NEW Customer's ID. :");
                String newId = scan.next();
                // 기존 파일 삭제 및 새로운 ID로 파일 이름 변경
                if (!customerId.equals(newId)) {
                    renameCustomerFile(customerId, newId);
                    customerToUpdate.setId(newId);
                    customerUpdated = true;
                }
                break;
            case 2:
                System.out.println("Enter a NEW Customer's Full Name. :");
                String newFullName = scan.next();
                customerToUpdate.setFullName(newFullName);
                customerUpdated = true;
                break;
            case 3:
                System.out.println("Enter a NEW Customer's Expiration Date. : (ex : 2024-12-31):");
                String dateInput = scan.next();
                LocalDate newExpirationDate = LocalDate.parse(dateInput);
                customerToUpdate.getInsuranceCard().setExpirationDate(newExpirationDate); // 고객의 보험 카드 만료 날짜 업데이트
                if (customerToUpdate.getDependents() != null) {
                    for (Customer dependent : customerToUpdate.getDependents()) {
                        if(dependent.getInsuranceCard() != null) {
                            dependent.getInsuranceCard().setExpirationDate(newExpirationDate); // 종속자의 보험 카드 만료 날짜 업데이트
                            serializeObject(dependent, "customer/" + dependent.getId() + ".txt"); // 변경된 종속자 정보 직렬화
                        }
                    }
                }
                customerUpdated = true;
                break;
            case 4:
                addDependent(customerId);
                break;

            case 5:
                deleteDependent(customerId);
                break;

            case 6:
                updateDependentInfo(customerId);
                break;
            default:
                System.out.println("Wrong Input!");
                return;

        }

        if (customerUpdated) {
            serializeObject(customerToUpdate, "customer/" + customerToUpdate.getId() + ".txt");
            System.out.println("Customer information has been successfully updated.");
            updateRelatedClaims(customerToUpdate);
        }
    }

    private void updateRelatedClaims(Customer updatedCustomer) {
        List<Claim> updatedClaims = new ArrayList<>(); // 업데이트된 클레임을 저장할 리스트

        for (Claim claim : updatedCustomer.getClaims()) {
            // 고객 정보 업데이트에 따라 클레임 내의 관련 정보를 업데이트합니다.
            claim.setInsuredPersonFullName(updatedCustomer.getFullName());

            if (claim.getBankingInfo() != null) {
                claim.getBankingInfo().setReceiverName(updatedCustomer.getFullName());
            }
            // 필요한 경우 추가 정보 업데이트
            updatedClaims.add(claim); // 업데이트된 클레임 리스트에 추가
            serializeObject(claim, "claim/" + claim.getId() + ".txt"); // 업데이트된 클레임 정보를 다시 직렬화하여 저장
        }

        // 고객 객체의 claims 리스트를 업데이트된 클레임 리스트로 갱신
        updatedCustomer.setClaims(updatedClaims);
        // 변경된 고객 정보를 다시 직렬화하여 저장
        serializeObject(updatedCustomer, "customer/" + updatedCustomer.getId() + ".txt");

        System.out.println("All related claims have been updated with the customer's new information.");
    }

    private void updateDependentInfo(String customerId) {
        Customer policyHolder = findCustomerById(customerId);
        if (policyHolder != null && policyHolder.getDependents() != null) {
            System.out.println("List of " + customerId + " dependents:");
            for (Customer dependent : policyHolder.getDependents()) {
                System.out.println("Dependent ID: " + dependent.getId() + ", Dependent Full Name: " + dependent.getFullName());
            }
            System.out.println("\nEnter a Dependent ID to update");
            String dependentId = scan.next();
            Customer dependentToUpdate = null;

            for (Customer dependent : policyHolder.getDependents()) {
                if (dependent.getId().equals(dependentId)) {
                    dependentToUpdate = dependent;
                    break;
                }
            }

            if (dependentToUpdate == null) {
                System.out.println("Dependent not found.");
                return;
            }

            System.out.println("Please choose an option to update:");
            System.out.println(" [1] Dependent's Full Name");
            int choice = scan.nextInt();
            scan.nextLine(); // 숫자 입력 후 남은 줄바꿈 문자 제거

            switch (choice) {
                case 1:
                    System.out.println("Enter a new Dependent's Full Name: ");
                    String newName = scan.nextLine();
                    dependentToUpdate.setFullName(newName);

                    // 여기서 dependent와 관련된 모든 claims 업데이트
                    updateRelatedDependentClaims(dependentToUpdate);

                    // 변경된 정보를 반영하여 PolicyHolder 객체 직렬화
                    serializeObject(policyHolder, "customer/" + policyHolder.getId() + ".txt");

                    // 여기서는 dependent 객체를 다시 PolicyHolder의 dependents 리스트에 업데이트 해주는 로직이 필요합니다.
                    // 이 부분은 직접적으로 코드를 수정하지 않고, 로직의 필요성만 언급합니다.
                    updatePolicyHolderDependents(policyHolder, dependentToUpdate);

                    System.out.println("Dependent's information has been successfully updated.");
                    break;
                default:
                    System.out.println("Wrong Input!");
                    return;
            }

        } else {
            System.out.println("You can not update dependent's information.");
        }
    }
    private void updatePolicyHolderDependents(Customer policyHolder, Customer updatedDependent) {
        // PolicyHolder 내의 dependents 리스트를 업데이트합니다.
        List<Customer> updatedDependents = new ArrayList<>();
        for (Customer dependent : policyHolder.getDependents()) {
            if (dependent.getId().equals(updatedDependent.getId())) {
                updatedDependents.add(updatedDependent); // 변경된 dependent 추가
            } else {
                updatedDependents.add(dependent);
            }
        }
        policyHolder.setDependents(updatedDependents); // 변경된 리스트를 policyHolder에 설정

        // 변경된 PolicyHolder 객체를 다시 직렬화하여 저장
        serializeObject(policyHolder, "customer/" + policyHolder.getId() + ".txt");
    }
    private void updateRelatedDependentClaims(Customer dependent) {
        List<Claim> updatedClaims = new ArrayList<>();
        List<Claim> allClaims = deserializeClaims(); // 모든 클레임 정보를 역직렬화하여 불러옵니다.

        for (Claim claim : allClaims) {
            if (claim.getInsuredPersonId().equals(dependent.getId())) {
                claim.setInsuredPersonFullName(dependent.getFullName());
                // BankingInfo의 Receiver Name도 업데이트
                if (claim.getBankingInfo() != null) {
                    claim.getBankingInfo().setReceiverName(dependent.getFullName());
                }
                updatedClaims.add(claim); // 업데이트된 클레임을 리스트에 추가
                // 업데이트된 클레임 정보를 다시 직렬화하여 저장
                serializeObject(claim, "claim/" + claim.getId() + ".txt");
            }
        }
        dependent.setClaims(updatedClaims); // dependent 객체 내의 claims 리스트를 업데이트
        serializeObject(dependent, "customer/" + dependent.getId() + ".txt"); // 변경된 dependent 객체를 다시 직렬화하여 저장
        System.out.println("All related claims for the dependent have been updated.");
    }


    private void renameCustomerFile(String oldId, String newId) {
        File oldFile = new File("customer/" + oldId + ".txt");
        File newFile = new File("customer/" + newId + ".txt");
        if (oldFile.exists()) {
            if (oldFile.renameTo(newFile)) {
                System.out.println("파일 이름이 성공적으로 변경되었습니다.");
            } else {
                System.out.println("파일 이름 변경에 실패하였습니다.");
            }
        } else {
            System.out.println("변경할 파일이 존재하지 않습니다.");
        }
    }


    @Override
    public void deleteCustomer() {
        List<Customer> customers = deserializeCustomers();

        listCustomersWithoutDependents();

        System.out.println("Enter the Customer's ID to delete");
        String id = scan.next();

        Customer customerToDelete = null;
        for (Customer customer : customers) {
            if (customer.getId().equals(id)) {
                customerToDelete = customer;
                break;
            }
        }

        if (customerToDelete == null) {
            System.out.println(id + " is not our customer.");
            return;
        }

        // 고객 및 종속자의 Claim 파일 삭제
        deleteClaimsForCustomer(id);
        if (customerToDelete.getDependents() != null) {
            for (Customer dependent : customerToDelete.getDependents()) {
                deleteClaimsForCustomer(dependent.getId());
            }
        }

        // Customer의 Dependent 정보 삭제
        if (customerToDelete.getDependents() != null && !customerToDelete.getDependents().isEmpty()) {
            for (Customer dependent : customerToDelete.getDependents()) {
                File dependentFile = new File("customer/" + dependent.getId() + ".txt");
                if (dependentFile.exists()) {
                    dependentFile.delete();
                    System.out.println("Dependent " + dependent.getId() + " file deleted.");
                }

                if (dependent.getInsuranceCard() != null) {
                    File insuranceCardFile = new File("insuranceCard/" + dependent.getInsuranceCard().getCardNumber() + ".txt");
                    if (insuranceCardFile.exists()) {
                        insuranceCardFile.delete();
                        System.out.println("Insurance Card file for " + dependent.getInsuranceCard().getCardNumber() + " deleted.");
                    }
                }
            }
        }

        // Customer(policyHolder) 정보 삭제
        customers.remove(customerToDelete);
        File customerFile = new File("customer/" + id + ".txt");
        if (customerFile.exists()) {
            customerFile.delete();
            System.out.println("Customer " + id + " file deleted.");
        }

        // Customer의 보험 카드 파일 삭제
        if (customerToDelete.getInsuranceCard() != null) {
            File insuranceCardFile = new File("insuranceCard/" + customerToDelete.getInsuranceCard().getCardNumber() + ".txt");
            if (insuranceCardFile.exists()) {
                insuranceCardFile.delete();
                System.out.println("Insurance Card file for " + customerToDelete.getInsuranceCard().getCardNumber() + " deleted.");
            }
        }

        // 변경된 고객 목록을 다시 직렬화하여 저장
        System.out.println(id + " has been successfully deleted.");
    }

    private void deleteClaimsForCustomer(String customerId) {
        File claimDirectory = new File("claim/");
        if (claimDirectory.exists() && claimDirectory.isDirectory()) {
            File[] claimFiles = claimDirectory.listFiles();
            if (claimFiles != null) {
                for (File claimFile : claimFiles) {
                    // Claim 객체 역직렬화
                    Claim claim = deserializeClaimFromFile(claimFile);
                    if (claim != null && claim.getInsuredPersonId().equals(customerId)) {
                        // 해당하는 Claim 파일 삭제
                        claimFile.delete();
                        System.out.println("Claim file " + claimFile.getName() + " deleted.");
                    }
                }
            }
        }
    }

    private static Claim deserializeClaimFromFile(File claimFile) {
        Claim claim = null; // 초기화
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(claimFile))) {
            claim = (Claim) inputStream.readObject(); // Claim 객체 역직렬화
        } catch (EOFException e) {
            // 파일 끝에 도달한 경우, 이 예외 처리는 선택적일 수 있음
            // 이 예외가 예상된 흐름이라면, 추가 처리가 필요 없을 수 있습니다.
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error occurred while reading claim data from file: " + claimFile.getName());
            e.printStackTrace();
        }
        return claim; // 역직렬화된 Claim 객체 반환
    }

    public void deleteDependent(String policyHolderId) {
        // PolicyHolder 찾기
        Customer policyHolder = findCustomerById(policyHolderId);
        if (policyHolder == null || policyHolder.getDependents() == null || policyHolder.getDependents().isEmpty()) {
            System.out.println("There is no Customer with the ID you entered or the Customer does not have dependents.");
            return;
        }
        if (policyHolder != null && policyHolder.getDependents() != null) {
            System.out.println("List of " + policyHolderId + " dependents:");
            for (Customer dependent : policyHolder.getDependents()) {
                System.out.println("Dependent ID: " + dependent.getId() + ", Dependent Full Name: " + dependent.getFullName());
            }
        }

        System.out.println("Enter the Dependent ID to delete");
        String dependentId = scan.next();

        // 종속자 목록에서 삭제할 종속자 찾기
        Customer dependentToDelete = policyHolder.getDependents().stream()
                .filter(dependent -> dependent.getId().equals(dependentId))
                .findFirst()
                .orElse(null);

        if (dependentToDelete == null) {
            System.out.println("There is no Dependent with the ID you entered.");
            return;
        }

        // 종속자가 신청한 청구 정보 삭제
        deleteClaimsForDependent(dependentId);

        // 종속자와 관련된 파일 삭제
        deleteFile("customer/" + dependentId + ".txt"); // 종속자 파일 삭제

        if (dependentToDelete.getInsuranceCard() != null) {
            String cardNumber = dependentToDelete.getInsuranceCard().getCardNumber();
            deleteFile("insuranceCard/" + cardNumber + ".txt"); // 보험 카드 파일 삭제
        }

        // 종속자 목록에서 삭제
        policyHolder.getDependents().remove(dependentToDelete);

        // 변경된 PolicyHolder 정보 직렬화하여 저장
        serializeObject(policyHolder, "customer/" + policyHolder.getId() + ".txt");
        System.out.println(dependentId + " and associated files have been successfully deleted.");
    }

    private void deleteClaimsForDependent(String dependentId) {
        File claimDirectory = new File("claim/");
        if (claimDirectory.exists() && claimDirectory.isDirectory()) {
            File[] claimFiles = claimDirectory.listFiles();
            if (claimFiles != null) {
                for (File claimFile : claimFiles) {
                    Claim claim = deserializeClaimFromFile(claimFile);
                    if (claim != null && claim.getInsuredPersonId().equals(dependentId)) {
                        if (claimFile.delete()) {
                            System.out.println("Deleted claim file: " + claimFile.getName());
                        }
                    }
                }
            }
        }
    }

    private void deleteFile(String filePath) {
        File fileToDelete = new File(filePath);
        if (fileToDelete.exists() && fileToDelete.delete()) {
            System.out.println("Deleted file: " + filePath);
        } else {
            System.out.println("Failed to delete file: " + filePath);
        }
    }

// Assume deserializeClaimFromFile method is implemented as previously described.


    @Override
    public void getCustomerById() {
        // 이름을 입력받아 해당 회원의 나이와 전화번호 출력하기

        List<Customer> customers = deserializeCustomers();
        listCustomers();
        System.out.println("Enter the Customer's ID to search.");
        String id = scan.next();

        for(int i = 0; i < customers.size(); i++) {
            Customer customer = customers.get(i);
            if(customer.getId().equals(id)) {
                System.out.println("The information of" + id);
                System.out.println(customer.toString());
                return;
            }
        }
        System.out.println(id + "is not our customer.");
    }


    public void getAllCustomers() {
        List<Customer> customers = deserializeCustomers(); // 시스템의 모든 고객을 불러옵니다.

        if (customers.isEmpty()) {
            System.out.println("There is no Customers in our system.");
            return;
        }

        for (Customer customer : customers) {
            if(customer.getIsPolicyHolder() == true) {
                System.out.println(customer.toString()); // Customer 객체의 toString 메소드 호출
                System.out.println("---------------------------------------");
            }
        }
    }



    @Override
    public void getAllDependents() {
        List<Customer> customers = deserializeCustomers(); // 시스템의 모든 고객을 불러옵니다.

        if (customers.isEmpty()) {
            System.out.println("There is no Dependents in our system.");
            return;
        }

        System.out.println("Information for all dependents stored on the system : ");

        for (Customer policyHolder : customers) {
            // 종속자가 있는 경우에만 출력
            if (policyHolder.getDependents() != null && !policyHolder.getDependents().isEmpty()) {
                for (Customer dependent : policyHolder.getDependents()) {
                    System.out.println("Dependent ID: " + dependent.getId());
                    System.out.println("Dependent Full Name: " + dependent.getFullName());
                    System.out.println("Policy Holder ID: " + policyHolder.getId());
                    System.out.println("Policy Owner: " + dependent.getPolicyOwner());
                    if (dependent.getInsuranceCard() != null) {
                        System.out.println("Expiration Date : " + dependent.getInsuranceCard().getExpirationDate());
                        System.out.println("Insurance CardNumber: " + dependent.getInsuranceCard().getCardNumber());
                    } else {
                        System.out.println("No Insurance CardNumber");
                    }
                    // dependent의 청구 목록 출력
                    List<Claim> claims = dependent.getClaims();
                    if (claims != null && !claims.isEmpty()) {
                        System.out.println("Claims for Dependent: ");
                        for (Claim claim : claims) {
                            System.out.println("\tClaim ID: " + claim.getId());
                            System.out.println("\tClaim Date: " + claim.getClaimDate());
                            System.out.println("\tInsured person: " + claim.getInsuredPersonFullName());
                            System.out.println("\tCard Number: " + dependent.getInsuranceCard().getCardNumber());
                            System.out.println("\tExam Date: " + claim.getExamDate());
                            System.out.println("\tClaim Amount: " + claim.getClaimAmount());
                            System.out.println("\tClaim Status: " + claim.getStatus());
                            System.out.println("\tReceiver Banking Info (Bank – Name – Number) : " + claim.getBankingInfo());

                            System.out.println("\t----------");
                        }
                    } else {
                        System.out.println("No Claims for Dependent.");
                    }
                    System.out.println("---------------------------------------");
                }

            }
        }
    }


    public static void serializeObject(Object obj, String filePath) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filePath))) {
            outputStream.writeObject(obj);
            System.out.println(obj.getClass().getSimpleName() + " data saved to " + filePath);
        } catch (IOException e) {
            System.out.println("Error occurred while saving data to file: " + filePath);
            e.printStackTrace();
        }
    }
    public void listCustomers() {
        List<Customer> customers = deserializeCustomers();
        System.out.println("-------------- All Customers --------------");
        for(Customer customer : customers) {
            System.out.println("Customer ID : " + customer.getId() + "\tCustomer Name : " + customer.getFullName());
        }
    }

    public void listCustomersWithoutDependents() {
        List<Customer> customers = deserializeCustomers();
        System.out.println("-------------- All Customers --------------");
        for(Customer customer : customers) {
            if(customer.getIsPolicyHolder() == true) {
                System.out.println("Customer ID : " + customer.getId() + "\tCustomer Name : " + customer.getFullName());
            }
        }
    }

}
