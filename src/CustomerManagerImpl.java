package src;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

import static src.DeserializationHelper.deserializeCustomers;

public class CustomerManagerImpl implements CustomerManager {
    private static List<Customer> list = new ArrayList<>();
    private Scanner scan = new Scanner(System.in);



//    @Override
//    public void addCustomer() {
//
//        System.out.println("아이디를 입력해주세요");
//        String id = scan.next();
//
//        System.out.println("이름을 입력해주세요");
//        String fullName = scan.next();
//
//        System.out.println("보험 만료일을 입력해주세요 (예: 2024-12-31): ");
//        String dateInput = scan.next();
//        LocalDate expirationDate = LocalDate.parse(dateInput);
//
//        System.out.println("당신은 Policy Holder인가요?");
//        boolean answer = scan.nextBoolean();
//
//        System.out.println("Policy owner");
//        String policyOwner = scan.next();
//
//        // 보험 카드 생성
//        String cardNumber = generateRandomCardNumber();
//        InsuranceCard insuranceCard = new InsuranceCard(cardNumber);
//        insuranceCard.setCardInfo(fullName, policyOwner, expirationDate);
//
//        // 고객 객체 생성
//        Customer cus = new Customer(id, fullName, answer, policyOwner, expirationDate, insuranceCard);
//
//        // 고객 리스트에 추가
//        list.add(cus);
//
//        // 직렬화
//        serializeObject(cus, "customer/policyHolder/" + cus.getId() + ".txt");
//        System.out.println(fullName + "회원이 등록되었습니다.");
//
//
//    }


    @Override
    public void addCustomer() {
        System.out.println("아이디를 입력해주세요");
        String id = scan.next();

        System.out.println("이름을 입력해주세요");
        String fullName = scan.next();

        System.out.println("보험 만료일을 입력해주세요 (예: 2024-12-31): ");
        String dateInput = scan.next();
        LocalDate expirationDate = LocalDate.parse(dateInput);

        System.out.println("당신은 Policy Holder인가요?");
        boolean answer = scan.nextBoolean();

        System.out.println("Policy owner");
        String policyOwner = scan.next();

        // 보험 카드 생성
        String cardNumber = generateRandomCardNumber();
        InsuranceCard insuranceCard = new InsuranceCard(cardNumber);
        insuranceCard.setCardInfo(fullName, policyOwner, expirationDate);

        // 고객 객체 생성
        Customer cus = new Customer(id, fullName, answer, policyOwner, expirationDate, insuranceCard);

        // 고객 리스트에 추가
        list.add(cus);

        // 직렬화
        serializeObject(cus, "customer/" + cus.getId() + ".txt");
        System.out.println(fullName + "회원이 등록되었습니다.");


    }

    public void addDependent() {
        System.out.println("Policy Holder의 ID를 입력해주세요:");
        String policyHolderId = scan.next();

        // PolicyHolder 찾기
        Customer policyHolder = findCustomerById(policyHolderId);
        if (policyHolder == null) {
            System.out.println("해당 ID를 가진 Policy Holder가 존재하지 않습니다.");
            return;
        }

        // Dependent 정보 입력 받기
        System.out.println("Dependent의 ID를 입력해주세요:");
        String dependentId = scan.next();
        System.out.println("Dependent의 이름을 입력해주세요:");
        String dependentFullName = scan.next();

        String cardNumber = generateRandomCardNumber(); // 보험 카드 번호 생성
        InsuranceCard dependentInsuranceCard = new InsuranceCard(cardNumber);
        dependentInsuranceCard.setCardInfo(dependentFullName, policyHolder.getPolicyOwner(), policyHolder.getInsuranceCard().getExpirationDate());

        // Dependent 객체 생성
        Customer dependent = new Customer(dependentId, dependentFullName, false, policyHolder.getPolicyOwner(), policyHolder.getExpirationDate(), dependentInsuranceCard); // InsuranceCard는 예제에서 제외

        // PolicyHolder의 dependents 리스트에 추가
        if (policyHolder.getDependents() == null) {
            policyHolder.setDependents(new ArrayList<>());
        }
        policyHolder.getDependents().add(dependent);

        // 변경된 PolicyHolder 정보 직렬화
        serializeObject(policyHolder, "customer/" + policyHolder.getId() + ".txt");

        System.out.println("Dependent가 성공적으로 추가되었습니다.");
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

//    @Override
//// policyHolder의 dependent 추가 메소드
//    public void addDependent(Customer policyHolder) {
//        System.out.println("Dependent의 ID를 입력해주세요: ");
//        String dependentId = scan.next();
//
//        System.out.println("Dependent의 이름을 입력해주세요: ");
//        String dependentName = scan.next();
//
//
//        // 보험 카드 생성
//        String cardNumber = generateRandomCardNumber();
//        InsuranceCard insuranceCard = new InsuranceCard(cardNumber);
//        insuranceCard.setCardInfo(dependentName, policyHolder.getPolicyOwner(), policyHolder.getExpirationDate());
//        // Dependent 정보를 생성합니다.
//        Customer dependent = new Customer(dependentId, dependentName, false, policyHolder.getId(), policyHolder.getExpirationDate(), policyHolder.getPolicyOwner(), insuranceCard);
//
//        // policyHolder의 dependents 리스트에 추가합니다.
//        policyHolder.getDependents().add(dependent);
//
//        // 직렬화하여 policyHolder 정보 업데이트
//        serializeObject(dependent, "customer/dependent/" + dependentId + ".txt");
//
//
//        System.out.println("Dependent가 등록되었습니다.");
//    }

    // 랜덤한 10자리 숫자를 생성하는 메서드
    private String generateRandomCardNumber() {
        Random random = new Random();
        StringBuilder cardNumberBuilder = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            int digit = random.nextInt(10);
            cardNumberBuilder.append(digit);
        }
        return cardNumberBuilder.toString();
    }

    @Override
    public void updateCustomer() {
        System.out.println("업데이트할 고객의 ID를 입력해주세요:");
        String customerId = scan.next();

        // 고객 찾기
        Customer customer = findCustomerById(customerId);
        if (customer == null) {
            System.out.println("해당 ID를 가진 고객이 존재하지 않습니다.");
            return;
        }

        System.out.println("업데이트할 정보를 선택해주세요:");
        System.out.println("1. 고객 ID");
        System.out.println("2. 고객 이름");
        System.out.println("3. 보험 만료일");
        System.out.println("## [4]dependent 추가 [5]dependent 삭제");
        System.out.println("## [6]dependent 수정 [5]취소");
        int choice = scan.nextInt();
        scan.nextLine(); // 숫자 입력 후 남은 줄바꿈 문자 제거

        switch (choice) {
            case 1:
                System.out.println("새로운 고객 ID를 입력해주세요:");
                String newId = scan.next();
                // 기존 파일 삭제 및 새로운 ID로 파일 이름 변경
                if (!customerId.equals(newId)) { // 새로운 ID가 기존 ID와 다를 경우에만 실행
                    renameCustomerFile(customerId, newId);
                    customer.setId(newId);
                }
                serializeObject(customer, "customer/" + customer.getId() + ".txt");
                break;
            case 2:
                System.out.println("새로운 고객 이름을 입력해주세요:");
                String newFullName = scan.next();
                customer.setFullName(newFullName);
                serializeObject(customer, "customer/" + customer.getId() + ".txt");
                break;
            case 3:
                System.out.println("새로운 보험 만료일을 입력해주세요 (예: 2024-12-31):");
                String dateInput = scan.next();
                LocalDate newExpirationDate = LocalDate.parse(dateInput);
                customer.setExpirationDate(newExpirationDate);
                serializeObject(customer, "customer/" + customer.getId() + ".txt");
                break;

            case 4:
                addDependent();
                break;

            case 5:
                deleteDependent();
                break;

            case 6:
                System.out.println("수정할 종속자의 ID를 입력하세요:");
                String editDependentId = scan.next();
                updateDependentInfo(customerId, editDependentId);
            default:
                System.out.println("잘못된 선택입니다.");
                return;
        }

        // 업데이트된 고객 정보 직렬화
        System.out.println("고객 정보가 성공적으로 업데이트되었습니다.");
    }

    private void updateDependentInfo(String customerId, String dependentId) {
        Customer policyHolder = findCustomerById(customerId);
        if (policyHolder != null && policyHolder.getDependents() != null) {
            System.out.println("해당 고객의 종속자 목록:");

            // 모든 종속자 정보 출력
            for (Customer dependent : policyHolder.getDependents()) {
                System.out.println("종속자 ID: " + dependent.getId() + ", 이름: " + dependent.getFullName());
            }
            System.out.println("\n수정할 종속자의 ID를 입력해주세요:");
            dependentId = scan.next(); // 사용자 입력을 기반으로 수정할 종속자 ID 업데이트
            for (Customer dependent : policyHolder.getDependents()) {
                if (dependent.getId().equals(dependentId)) {
                    System.out.println("수정할 정보를 선택해주세요:");
                    System.out.println("1. 종속자 아이디");
                    System.out.println("2. 종속자 이름");
                    int choice = scan.nextInt();
                    scan.nextLine(); // 숫자 입력 후 남은 줄바꿈 문자 제거

                    switch (choice) {
                        case 1:
                            System.out.println("새로운 종속자 아이디를 입력해주세요:");
                            String newId = scan.next();
                            dependent.setId(newId);
                            break;
                        case 2:
                            System.out.println("새로운 종속자 이름을 입력해주세요:");
                            String newName = scan.next();
                            dependent.setFullName(newName);
                            break;
                        default:
                            System.out.println("잘못된 선택입니다.");
                            return;
                    }

                    // 변경된 정보를 반영하여 PolicyHolder 객체 직렬화
                    serializeObject(policyHolder, "customer/" + policyHolder.getId() + ".txt");
                    System.out.println("종속자 정보가 성공적으로 업데이트되었습니다.");
                    return;
                }
            }
            System.out.println("해당 ID를 가진 종속자가 존재하지 않습니다.");
        } else {
            System.out.println("종속자 정보를 수정할 수 없습니다.");
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


//    @Override
//    public void updateCustomer() {
//        List<Customer> customers = deserializeCustomers();
//
//            System.out.println("수정할 회원의 이름을 입력하세요.");
//            String id = scan.next();
//
//            for (int i = 0; i < customers.size(); i++) {
//                Customer cus = customers.get(i);
//
//                if (cus.getId().equals(id)) {
//                    while (true) {
//                        Scanner scan = new Scanner(System.in);
//                        System.out.println("무엇을 수정하시겠어요?");
//                        System.out.println("## [1]아이디 [2]이름 [3]유효기간 수정");
//                        System.out.println("## [4]dependent 추가 [5]dependent 삭제");
//                        System.out.println("## [6]dependent 수정 [5]취소");
//                        int number = scan.nextInt();
//
//                        switch (number) {
//                            case 1:
//                                System.out.println("수정할 아이디 : ");
//                                String newId = scan.next();
//                                cus.setId(newId);
//
//                                String directoryPath = "customer";
//                                String oldFileName = directoryPath + "/" + id + ".txt";
//                                String newFileName = directoryPath + "/" + newId + ".txt";
//
//                                File oldFile = new File(oldFileName);
//                                File newFile = new File(newFileName);
//
//                                if (oldFile.exists()) {
//                                    if (oldFile.renameTo(newFile)) {
//                                        System.out.println("파일 이름을 변경하였습니다.");
//                                    } else {
//                                        System.out.println("파일 이름 변경에 실패하였습니다.");
//                                    }
//                                } else {
//                                    System.out.println("수정할 고객의 파일이 존재하지 않습니다.");
//                                }
//                                break;
//
//                            case 2:
//                                System.out.println("수정할 이름 : ");
//                                String newFullName = scan.next();
//                                cus.setFullName(newFullName);
//                                break;
//
//                            case 3:
//                                System.out.println("수정할 유효기간");
//                                String dateInput = scan.next();
//                                LocalDate expirationDate = LocalDate.parse(dateInput);
//                                cus.setExpirationDate(expirationDate);
//
//                            case 4:
//                                addDependent();
//                                break;
//
//                            case 5:
//                                System.out.println("삭제할 종속자의 ID를 입력하세요:");
//                                String dependentIdToDelete = scan.next();
//                                deleteDependentAndUpdateCustomer(dependentIdToDelete);
//
//                            case 6:
//                                System.out.println("수정할 종속자의 ID를 입력하세요:");
//                                String editDependentId = scan.next();
//                                updateDependentInfoAndUpdateCustomer(editDependentId, cus);
//
//                            case 7:
//                                System.out.println("취소");
//                                break;
//
//                        }
//                        System.out.println(id + "님의 개인정보가 성공적으로 수정되었습니다.222");
//                        serializeObject(cus, "customer/" + cus.getId() + ".txt");
//                        return;
//                    }
//                }
//
//            }
//
//            System.out.println(id + "님은 저희 회원이 아닙니다.");
//
//    }

    @Override
    public void updateDependentInfoAndUpdateCustomer(String dependentId, Customer parentCustomer) {
        // 기존 종속자 리스트 가져오기
        List<Customer> customers = deserializeCustomers();

        // 수정된 종속자 리스트 초기화
        List<Customer> dependentList = new ArrayList<>();

        // 종속자 정보 수정 로직
        for (Customer cus : customers) {
            if (cus.getDependentId() != null && cus.getDependentId().equals(dependentId)) {
                System.out.println("수정할 종속자 정보를 입력하세요:");
                Scanner scan = new Scanner(System.in);
                System.out.println("무엇을 수정하시겠어요?");
                System.out.println("## [1] 아이디 [2] 이름 [3] 유효기간 수정");
                int number = scan.nextInt();

                switch (number) {
                    case 1:
                        System.out.println("수정할 아이디 : ");
                        String newId = scan.next();
                        cus.setDependentId(newId);
                        break;
                    case 2:
                        System.out.println("수정할 이름 : ");
                        String newFullName = scan.next();
                        cus.setDependentFullName(newFullName);
                        break;
                    case 3:
                        System.out.println("수정할 유효기간 : ");
                        String dateInput = scan.next();
                        LocalDate expirationDate = LocalDate.parse(dateInput);
                        cus.setExpirationDate(expirationDate);
                        break;
                    default:
                        System.out.println("잘못된 입력입니다. 다시 입력하세요.");
                }
            }
            dependentList.add(cus);
        }

        parentCustomer.setDependents(dependentList);
        // 종속자 정보만을 직렬화하여 해당 종속자의 파일에 저장
        serializeObject(dependentList, "customer/dependent/" + parentCustomer.getId() + ".txt");

        // 부모 고객 정보만을 직렬화하여 부모 고객의 파일에 저장
        serializeObject(parentCustomer, "customer/policyHolder/" + parentCustomer.getId() + ".txt");
    }


    @Override
    public void deleteCustomer() {
        List<Customer> customers = deserializeCustomers();
        System.out.println("삭제할 회원의 ID를 입력하세요.");
        String id = scan.next();

        // 역순으로 리스트를 순회하여 요소를 안전하게 제거
        for (int i = customers.size() - 1; i >= 0; i--) {
            Customer customer = customers.get(i);

            if (customer.getId().equals(id)) {
                customers.remove(i);
                System.out.println(id + "회원님을 삭제하였습니다");

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

        System.out.println(id + "님은 저희 회원이 아닙니다.");
    }




    public void deleteDependent() {
        System.out.println("종속자를 삭제할 고객(Policy Holder)의 ID를 입력해주세요:");
        String policyHolderId = scan.next();

        // PolicyHolder 찾기
        Customer policyHolder = findCustomerById(policyHolderId);
        if (policyHolder == null || policyHolder.getDependents() == null || policyHolder.getDependents().isEmpty()) {
            System.out.println("해당 ID를 가진 고객이 존재하지 않거나, 종속자가 없습니다.");
            return;
        }

        System.out.println("삭제할 종속자의 ID를 입력해주세요:");
        String dependentId = scan.next();

        // 종속자 목록에서 삭제할 종속자 찾기 및 삭제
        boolean isRemoved = policyHolder.getDependents().removeIf(dependent -> dependent.getId().equals(dependentId));

        if (isRemoved) {
            // 변경된 PolicyHolder 정보 직렬화하여 저장
            serializeObject(policyHolder, "customer/" + policyHolder.getId() + ".txt");
            System.out.println("종속자가 성공적으로 삭제되었습니다.");
        } else {
            System.out.println("해당 ID를 가진 종속자가 존재하지 않습니다.");
        }
    }



    @Override
    public void getCustomerById() {
        // 이름을 입력받아 해당 회원의 나이와 전화번호 출력하기
        List<Customer> customers = deserializeCustomers();
        System.out.println("조회할 아이디를 입력하세요.");
        String id = scan.next();

        for(int i = 0; i < customers.size(); i++) {
            Customer customer = customers.get(i);
            if(customer.getId().equals(id)) {
                System.out.println("해당 id를 가진 고객의 정보입니다.");
                System.out.println(customer.toString());
                return;
            }
        }
        System.out.println(id + "님은 저희 회원이 아닙니다.");
    }


    public void getAllCustomers() {
        List<Customer> customers = deserializeCustomers(); // 시스템의 모든 고객을 불러옵니다.

        if (customers.isEmpty()) {
            System.out.println("등록된 고객이 없습니다.");
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
            System.out.println("등록된 고객이 없습니다.");
            return;
        }

        System.out.println("시스템에 저장된 모든 종속자의 정보:");

        for (Customer policyHolder : customers) {
            // 종속자가 있는 경우에만 출력
            if (policyHolder.getDependents() != null && !policyHolder.getDependents().isEmpty()) {
                for (Customer dependent : policyHolder.getDependents()) {
                    System.out.println("종속자 ID: " + dependent.getId());
                    System.out.println("종속자 이름: " + dependent.getFullName());
                    System.out.println("Policy Holder ID: " + policyHolder.getId());
                    System.out.println("Policy Owner: " + dependent.getPolicyOwner());
                    if (dependent.getInsuranceCard() != null) {
                        System.out.println("보험 유효기간: " + dependent.getInsuranceCard().getExpirationDate());
                        System.out.println("보험 카드 번호: " + dependent.getInsuranceCard().getCardNumber());
                    } else {
                        System.out.println("보험 카드 정보가 없습니다.");
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


}
