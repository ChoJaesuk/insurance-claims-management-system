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
        serializeObject(cus, "customer/policyHolder/" + cus.getId() + ".txt");
        System.out.println(fullName + "회원이 등록되었습니다.");


    }

    @Override
// policyHolder의 dependent 추가 메소드
    public void addDependent(Customer policyHolder) {
        System.out.println("Dependent의 ID를 입력해주세요: ");
        String dependentId = scan.next();

        System.out.println("Dependent의 이름을 입력해주세요: ");
        String dependentName = scan.next();


        // 보험 카드 생성
        String cardNumber = generateRandomCardNumber();
        InsuranceCard insuranceCard = new InsuranceCard(cardNumber);
        insuranceCard.setCardInfo(dependentName, policyHolder.getPolicyOwner(), policyHolder.getExpirationDate());
        // Dependent 정보를 생성합니다.
        Customer dependent = new Customer(dependentId, dependentName, false, policyHolder.getId(), policyHolder.getExpirationDate(), policyHolder.getPolicyOwner(), insuranceCard);

        // policyHolder의 dependents 리스트에 추가합니다.
        policyHolder.getDependents().add(dependent);

        // 직렬화하여 policyHolder 정보 업데이트
        serializeObject(policyHolder, "customer/policyHolder/" + policyHolder.getId() + ".txt");
        serializeObject(dependent, "customer/dependent/" + dependentId + ".txt");


        System.out.println("Dependent가 등록되었습니다.");
    }

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
        List<Customer> customers = deserializeCustomers();

            System.out.println("수정할 회원의 이름을 입력하세요.");
            String id = scan.next();

            for (int i = 0; i < customers.size(); i++) {
                Customer cus = customers.get(i);

                if (cus.getId().equals(id)) {
                    while (true) {
                        Scanner scan = new Scanner(System.in);
                        System.out.println("무엇을 수정하시겠어요?");
                        System.out.println("## [1]아이디 [2]이름 [3]유효기간 수정");
                        System.out.println("## [4]dependent 추가 [5]dependent 삭제");
                        System.out.println("## [6]dependent 수정 [5]취소");
                        int number = scan.nextInt();

                        switch (number) {
                            case 1:
                                System.out.println("수정할 아이디 : ");
                                String newId = scan.next();
                                cus.setId(newId);

                                String directoryPath = "customer/policyHolder";
                                String oldFileName = directoryPath + "/" + id + ".txt";
                                String newFileName = directoryPath + "/" + newId + ".txt";

                                File oldFile = new File(oldFileName);
                                File newFile = new File(newFileName);

                                if (oldFile.exists()) {
                                    if (oldFile.renameTo(newFile)) {
                                        System.out.println("파일 이름을 변경하였습니다.");
                                    } else {
                                        System.out.println("파일 이름 변경에 실패하였습니다.");
                                    }
                                } else {
                                    System.out.println("수정할 고객의 파일이 존재하지 않습니다.");
                                }
                                break;

                            case 2:
                                System.out.println("수정할 이름 : ");
                                String newFullName = scan.next();
                                cus.setFullName(newFullName);
                                break;

                            case 3:
                                System.out.println("수정할 유효기간");
                                String dateInput = scan.next();
                                LocalDate expirationDate = LocalDate.parse(dateInput);
                                cus.setExpirationDate(expirationDate);

                            case 4:
                                addDependent(cus);
                                break;

                            case 5:
                                System.out.println("삭제할 종속자의 ID를 입력하세요:");
                                String dependentIdToDelete = scan.next();
                                deleteDependentAndUpdateCustomer(dependentIdToDelete);

                            case 6:
                                System.out.println("수정할 종속자의 ID를 입력하세요:");
                                String editDependentId = scan.next();
                                updateDependentInfoAndUpdateCustomer(editDependentId, cus);
                            case 7:
                                System.out.println("취소");
                                break;

                        }
                        serializeObject(cus, "customer/policyHolder/" + cus.getId() + ".txt");
                        System.out.println(id + "님의 개인정보가 성공적으로 수정되었습니다.");
                        return;
                    }
                }

            }

            System.out.println(id + "님은 저희 회원이 아닙니다.");

    }

    @Override
    public void updateDependentInfoAndUpdateCustomer(String dependentId, Customer parentCustomer) {
        // 기존 종속자 리스트 가져오기
        List<Customer> customers = deserializeCustomers();

        // 수정된 종속자 리스트 초기화
        List<Customer> dependentList = new ArrayList<>();

        // 기존 종속자 리스트를 순회하며 수정된 정보 업데이트
        for (Customer cus : customers) {
            if (cus.getDependentId() != null && cus.getDependentId().equals(dependentId)) {
                // 종속자 정보 수정 로직
                while (true) {
                    Scanner scan = new Scanner(System.in);
                    System.out.println("무엇을 수정하시겠어요?");
                    System.out.println("## [1]아이디 [2]이름 [3]유효기간 수정");
                    int number = scan.nextInt();

                    switch (number) {
                        case 1:
                            System.out.println("수정할 아이디 : ");
                            String newId = scan.next();
                            cus.setDependentId(newId);

                            String directoryPath = "customer/dependent";
                            String oldFileName = directoryPath + "/" + dependentId + ".txt";
                            String newFileName = directoryPath + "/" + newId + ".txt";

                            File oldFile = new File(oldFileName);
                            File newFile = new File(newFileName);

                            if (oldFile.exists()) {
                                if (oldFile.renameTo(newFile)) {
                                    System.out.println("파일 이름을 변경하였습니다.");
                                } else {
                                    System.out.println("파일 이름 변경에 실패하였습니다.");
                                }
                            } else {
                                System.out.println("수정할 고객의 파일이 존재하지 않습니다.");
                            }
                            break;

                        case 2:
                            System.out.println("수정할 이름 : ");
                            String newFullName = scan.next();
                            cus.setDependentFullName(newFullName);
                            break;
                        case 3:
                            System.out.println("수정할 유효기간");
                            String dateInput = scan.next();
                            LocalDate expirationDate = LocalDate.parse(dateInput);
                            cus.setExpirationDate(expirationDate);
                            break;
                    }

                    // 수정된 종속자 정보를 리스트에 추가
                    dependentList.add(cus);
                    System.out.println(dependentId + "님의 개인정보가 성공적으로 수정되었습니다.");
                    break;
                }
            } else {
                // 수정되지 않은 종속자는 그대로 유지
                dependentList.add(cus);
            }
        }

        parentCustomer.setDependents(dependentList);

        // 종속자 정보만을 직렬화하여 해당 종속자의 파일에 저장
        serializeObject(dependentList, "customer/dependent/" + dependentId + ".txt");

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




    private void deleteDependentAndUpdateCustomer(String dependentId) {
        List<Customer> customers = deserializeCustomers();

        for (Customer customer : customers) {
            List<Customer> dependents = customer.getDependents();
            Iterator<Customer> iterator = dependents.iterator();

            while (iterator.hasNext()) {
                Customer dependent = iterator.next();
                if (dependent.getDependentId().equals(dependentId)) {
                    System.out.println("삭제 전 - 종속자 리스트: " + dependents);
                    iterator.remove(); // 종속자 리스트에서 해당 종속자 제거
                    System.out.println("삭제 후 - 종속자 리스트: " + dependents);

                    // 종속자 정보를 삭제하고 수정된 종속자 리스트로 고객 객체를 다시 직렬화하여 업데이트
                    serializeObject(customer, "customer/policyHolder/" + customer.getId() + ".txt");
                    System.out.println("직렬화된 고객 파일 업데이트 완료");

                    // 종속자 텍스트 파일 삭제
                    String filePath = "customer/dependent/" + dependentId + ".txt";
                    File file = new File(filePath);
                    if (file.exists()) {
                        if (file.delete()) {
                            System.out.println(dependentId + "종속자를 삭제하였습니다.");
                        } else {
                            System.out.println("파일 처리 중 오류가 발생했습니다.");
                        }
                    } else {
                        System.out.println("삭제할 파일이 존재하지 않습니다.");
                    }

                    System.out.println("종속자 정보가 성공적으로 업데이트되었습니다.");
                    return;
                }
            }
        }
        System.out.println(dependentId + "종속자를 찾을 수 없습니다.");
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


    @Override
    public void getAllCustomers() {
        List<Customer> customers = deserializeCustomers();
        if (customers.isEmpty()) {
            System.out.println("No customer data found.");
        } else {
            System.out.println("All Customers:");
            for (Customer customer : customers) {
                // Customer 클래스의 getCustomerInfoString() 메서드를 사용하여 고객 정보 출력
                System.out.println(customer.toString());
                System.out.println(); // 고객 정보 간의 간격
            }
        }
    }

    @Override
    public void getAllDependents() {
        List<Customer> customers = deserializeCustomers();
        if (customers.isEmpty()) {
            System.out.println("No dependent data found.");
        } else {
            System.out.println("All Dependents:");
            for (Customer customer : customers) {
                List<Customer> dependents = customer.getDependents();
                if (dependents.isEmpty()) {
                    System.out.println("No dependents found for customer with ID: " + customer.getId());
                } else {
                    System.out.println("Dependents for customer with ID: " + customer.getId());
                    for (Customer dependent : dependents) {
                        // Customer 클래스의 toString() 메서드를 사용하여 종속자 정보 출력
                        System.out.println(dependent.getDependentInfo());
                    }
                }
                System.out.println(); // 종속자 정보 간의 간격
            }
        }
    }



    // 고객을 ID로 찾는 메소드
    private Customer findCustomerById(String customerId) {
        List<Customer> customers = deserializeCustomers(); // deserializeCustomers()는 해당 메소드에 맞게 구현되어야 함
        for (Customer customer : customers) {
            if (customer.getId().equals(customerId)) {
                return customer;
            }
        }
        return null;
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
