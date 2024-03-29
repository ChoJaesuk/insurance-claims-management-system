package src;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class CustomerManagerImpl implements CustomerManager {
    private static List<Customer> list = new ArrayList<>();
    private static List<Dependent> dependentsList = new ArrayList<>();
    private Scanner scan = new Scanner(System.in);



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
    // 고객 데이터를 텍스트 파일에 저장하는 메소드
//    public void createInsuranceCard(String cardNumber, String cardHolder, String policyOwner, LocalDate expirationDate) {
//        InsuranceCard insuranceCard = new InsuranceCard(cardNumber);
//        insuranceCard.setCardInfo(cardHolder, policyOwner, expirationDate);
//        this.insuranceCard = insuranceCard; // 보험 카드를 고객 객체에 설정
//
//    }

    @Override
    public void updateCustomer() {
        List<Customer> customers = deserializeCustomers();
        System.out.println("수정할 회원의 이름을 입력하세요.");
        String id = scan.next();

        for(int i = 0; i < customers.size(); i++) {
            Customer cus = customers.get(i);

            if(cus.getId().equals(id)) {
                while(true) {
                    Scanner scan = new Scanner(System.in);
                    System.out.println("무엇을 수정하시겠어요?");
                    System.out.println("## [1]아이디 [2]이름 [3]policyHolder");
                    System.out.println("## [4]유효기간 수정 [5]취소");
                    int number = scan.nextInt();

                    switch (number) {
                        case 1:
                            System.out.println("수정할 아이디 : ");
                            String newId = scan.next();
                            cus.setId(newId);

                            String directoryPath = "customer";
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
                            addDependent();
                            break;

                        case 6:
                            System.out.println("취소");
                            break;

                    }
                    serializeObject(cus, "customer/policyHolder/" + cus.getId() + ".txt");
                    System.out.println(id + "님의 개인정보가 성공적으로 수정되었습니다.");
                    System.out.println(id + "님의 개인정보를 수정하였습니다.");
                    return;
                }
            }

        }

        System.out.println(id + "님은 저희 회원이 아닙니다.");
    }


    @Override
    public void deleteCustomer() {
        List<Customer> customers = deserializeCustomers();
        System.out.println("삭제할 회원의 ID를 입력하세요.");
        String id = scan.next();

        // 역순으로 리스트를 순회하여 요소를 안전하게 제거
        for(int i = customers.size() - 1; i >= 0; i--) {
            Customer customer = customers.get(i);

            if(customer.getId().equals(id)) {
                customers.remove(i);
                System.out.println(id + "회원님을 삭제하였습니다");

                // 직렬화된 텍스트 파일 삭제
                File file = new File("customer/" + id + ".txt");
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
//    public void addDependent(Dependent dependent) {
//        dependents.add(dependent);
//    }
//
//    public void removeDependent(Dependent dependent) {
//        dependents.remove(dependent);
//    }
    @Override
    public void addDependent() {
        List<Customer> customers = deserializeCustomers();
        System.out.println("수정할 회원의 이름을 입력하세요.");
        String id = scan.next();

        for (int i = 0; i < customers.size(); i++) {
            Customer cus = customers.get(i);

            if (cus.getId().equals(id)) {

                System.out.println("dependent의 아이디를 입력해주세요");
                String dependentId = scan.next();

                System.out.println("dependent의 이름을 입력해주세요");
                String dependentFullName = scan.next();

                System.out.println("당신은 Policy Holder인가요?");
                boolean answer = scan.nextBoolean();

                LocalDate expirationDate = cus.getExpirationDate();
                String policyOwner = cus.getPolicyOwner();
                String cardNumber = generateRandomCardNumber();
                InsuranceCard insuranceCard = new InsuranceCard(cardNumber);

                cus.setDependentsInfo(id, policyOwner, expirationDate);

                // 고객 객체 생성
                Dependent dependent2 = new Dependent(dependentId, dependentFullName, id, insuranceCard, answer, policyOwner, expirationDate);

                // 고객 리스트에 추가
                dependentsList.add(dependent2);

                // 직렬화
                serializeObject(dependent2, "customer/dependent/" + dependentId + ".txt");
                System.out.println(dependentFullName + "회원이 등록되었습니다.");

            }
            else {
                System.out.println("저희 시스템에 등록되지 않은 id입니다.");
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

    public static List<Customer> deserializeCustomers() {
        List<Customer> customers = new ArrayList<>();

        try {
            // PolicyHolder 디렉토리
            File policyHolderDirectory = new File("customer/PolicyHolder");
            if (policyHolderDirectory.exists() && policyHolderDirectory.isDirectory()) {
                File[] policyHolderFiles = policyHolderDirectory.listFiles();
                if (policyHolderFiles != null) {
                    for (File file : policyHolderFiles) {
                        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file))) {
                            Customer policyHolder = (Customer) inputStream.readObject();
                            customers.add(policyHolder);
                        } catch (IOException | ClassNotFoundException e) {
                            System.out.println("Error occurred while reading PolicyHolder data from file: " + file.getName());
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                System.out.println("No PolicyHolder data found.");
            }

            // Dependent 디렉토리
            File dependentDirectory = new File("customer/Dependent");
            if (dependentDirectory.exists() && dependentDirectory.isDirectory()) {
                File[] dependentFiles = dependentDirectory.listFiles();
                if (dependentFiles != null) {
                    for (File file : dependentFiles) {
                        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file))) {
                            Dependent dependent = (Dependent) inputStream.readObject();
                            customers.add(dependent);
                        } catch (IOException | ClassNotFoundException e) {
                            System.out.println("Error occurred while reading Dependent data from file: " + file.getName());
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                System.out.println("No Dependent data found.");
            }
        } catch (Exception e) {
            System.out.println("Error occurred during deserialization.");
            e.printStackTrace();
        }

        return customers;
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
