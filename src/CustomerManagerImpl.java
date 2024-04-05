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
        Customer customer = findCustomerById(customerId);
        if (customer == null) {
            System.out.println("There is no Customer with the ID you entered.");
            return;
        }

        System.out.println("Please choose a option to update");
        System.out.println("## [1] Customer's ID  [2] Customer's Full Name [3] Customer's Expiration Date ##");
        System.out.println("## [4] Add Dependent [5] Delete Dependent [6] Update Dependent(ID, FullName) [7] 취소 ##");
        int choice = scan.nextInt();
        scan.nextLine(); // 숫자 입력 후 남은 줄바꿈 문자 제거

        switch (choice) {
            case 1:
                System.out.println("Enter a NEW Customer's ID. :");
                String newId = scan.next();
                // 기존 파일 삭제 및 새로운 ID로 파일 이름 변경
                if (!customerId.equals(newId)) { // 새로운 ID가 기존 ID와 다를 경우에만 실행
                    renameCustomerFile(customerId, newId);
                    customer.setId(newId);
                }
                serializeObject(customer, "customer/" + customer.getId() + ".txt");
                break;
            case 2:
                System.out.println("Enter a NEW Customer's FullName. :");
                String newFullName = scan.next();
                customer.setFullName(newFullName);
                serializeObject(customer, "customer/" + customer.getId() + ".txt");
                break;
            case 3:
                System.out.println("Enter a NEW Customer's Expiration Date. : (ex : 2024-12-31):");
                String dateInput = scan.next();
                LocalDate newExpirationDate = LocalDate.parse(dateInput);
                customer.setExpirationDate(newExpirationDate);
                serializeObject(customer, "customer/" + customer.getId() + ".txt");
                break;

            case 4:
                addDependent(customerId);
                break;

            case 5:
                deleteDependent();
                break;

            case 6:
                updateDependentInfo(customerId);
            default:
                System.out.println("Wrong Input!");
                return;
        }

        // 업데이트된 고객 정보 직렬화
        System.out.println(customerId + "has been successfully updated.");
        updateClaimsForCustomer(customer);
    }

    private void updateDependentInfo(String customerId) {
        Customer policyHolder = findCustomerById(customerId);
        if (policyHolder != null && policyHolder.getDependents() != null) {
            System.out.println("List of" + customerId + "dependents:");

            // 모든 종속자 정보 출력
            for (Customer dependent : policyHolder.getDependents()) {
                System.out.println("Dependent ID: " + dependent.getId() + ", Dependent Full Name: " + dependent.getFullName());
            }
            System.out.println("\nEnter a Dependent ID to update");
            String dependentId = scan.next();
            for (Customer dependent : policyHolder.getDependents()) {
                if (dependent.getId().equals(dependentId)) {
                    System.out.println("Plase choose a option to update");
                    System.out.println(" [1] Dependent's ID [2] Dependent's Full Name");
                    int choice = scan.nextInt();
                    scan.nextLine(); // 숫자 입력 후 남은 줄바꿈 문자 제거

                    switch (choice) {
                        case 1:
                            System.out.println("Enter a new Dependent's ID : ");
                            String newId = scan.next();
                            dependent.setId(newId);
                            renameCustomerFile(dependentId, newId);
                            break;
                        case 2:
                            System.out.println("Enter a new Dependent's Full Name : ");
                            String newName = scan.next();
                            dependent.setFullName(newName);
                            break;
                        default:
                            System.out.println("Wrong Input!");
                            return;
                    }

                    // 변경된 정보를 반영하여 PolicyHolder 객체 직렬화
                    serializeObject(policyHolder, "customer/" + policyHolder.getId() + ".txt");
                    serializeObject(dependent, "customer/" + dependentId + ".txt");
                    System.out.println(dependentId + "has been successfully updated.");
                    return;
                }
            }
            System.out.println("There is no Dependent with the ID you entered.");
        } else {
            System.out.println("You can not update dependent's information.");
        }
    }

    private void updateClaimsForCustomer(Customer customer) {
        List<Claim> claims = deserializeClaims(); // 모든 Claim 역직렬화
        boolean changesMade = false;

        for (Claim claim : claims) {
            if (claim.getInsuredPersonId().equals(customer.getId())) {
                claim.setInsuredPersonFullName(customer.getFullName()); // 고객 이름 업데이트
                serializeObject(claim, "claim/" + claim.getId() + ".txt"); // 업데이트된 Claim 직렬화하여 저장
                changesMade = true;
            }
        }

        if (changesMade) {
            System.out.println("Related claims have been updated with the new customer information.");
        }
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
        System.out.println("Enter the Customer's ID to delete");
        String id = scan.next();

        // 역순으로 리스트를 순회하여 요소를 안전하게 제거
        for (int i = customers.size() - 1; i >= 0; i--) {
            Customer customer = customers.get(i);

            if (customer.getId().equals(id)) {
                customers.remove(i);
                System.out.println(id + "has been successfully deleted.");

                // 직렬화된 텍스트 파일 삭제
                File file = new File("customer/policyHolder/" + id + ".txt");
                if (file.exists()) {
                    if (file.delete()) {
                        System.out.println("직렬화된 텍스트 파일을 성공적으로 삭제했습니다.");
                    } else {
                        System.out.println("직렬화된 텍스트 파일 삭제에 실패했습니다.");
                    }
                } else {
                    System.out.println("삭제할 파일이 존재하지 않습니다.");
                }

                return;
            }
        }

        System.out.println(id + "is not our customer.");
    }


    public void deleteDependent() {
        System.out.println("Please enter the ID of the customer (Policy Holder) to delete the dependent :");
        String policyHolderId = scan.next();

        // PolicyHolder 찾기
        Customer policyHolder = findCustomerById(policyHolderId);
        if (policyHolder == null || policyHolder.getDependents() == null || policyHolder.getDependents().isEmpty()) {
            System.out.println("There is no Customer with the ID you entered or the Customer does not have dependents.");
            return;
        }

        System.out.println("Enter the Dependent ID to delete");
        String dependentId = scan.next();

        // 종속자 목록에서 삭제할 종속자 찾기 및 삭제
        boolean isRemoved = policyHolder.getDependents().removeIf(dependent -> dependent.getId().equals(dependentId));

        if (isRemoved) {
            // 변경된 PolicyHolder 정보 직렬화하여 저장
            serializeObject(policyHolder, "customer/" + policyHolder.getId() + ".txt");
            System.out.println(dependentId + "has been successfully deleted.");
        } else {
            System.out.println("There is no Dependent with the ID you entered");
        }
    }



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
            System.out.println(customer.toString()); // Customer 객체의 toString 메소드 호출
            System.out.println("---------------------------------------");
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
