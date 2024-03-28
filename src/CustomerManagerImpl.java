package src;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class CustomerManagerImpl implements CustomerManager {
    private static List<Customer> list = new ArrayList<>();
    private Scanner scan = new Scanner(System.in);
    private Customer customer;
    private InsuranceCard insuranceCard;


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
        // 이름을 입력받아 해당 회원의 나이와 전화번호 수정하기
        List<Customer> customers = deserializeCustomers();
        System.out.println("수정할 회원의 이름을 입력하세요.");
        String id = scan.next();

        for(int i = 0; i < customers.size(); i++) {
            Customer cus = customers.get(i);

            if(cus.getId().equals(id)) {
                while(true) {
                    Scanner scan = new Scanner(System.in);
                    System.out.println("무엇을 수정하시겠어요?");
                    System.out.println("## [1]아이디 [2]이름 [4]dependents 추가");
                    System.out.println("## [5]policyHolder [6]유효기간 수정 [7]취소");
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

//                        case 3:
//                            System.out.println("수정할 카드번호 : ");
//                            String newInsuranceNumber = scan.next();
//                            customer.setInsuranceCard(newInsuranceNumber);
//                            break;
//
//                        case 4:
//                            System.out.println("수정할 dependents : ");
//                            String newDependents = scan.next();
//                            customer.setInsuranceCard(newDependents);
//                            break;
//
//                        case 5:
//                            System.out.println("수정할 claims : ");
//                            String newClaims = scan.next();
//                            customer.setInsuranceCard(newClaims);
//                            break;

                        case 6:
                            System.out.println("취소");
                            break;

                    }
                    serializeObject(cus, "customer/" + cus.getId() + ".txt");
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

    @Override
    public void addDependents() {

    }

    public static List<Customer> deserializeCustomers() {
        List<Customer> customers = new ArrayList<>();

        try {
            File directory = new File("customer");
            if (!directory.exists() || !directory.isDirectory()) {
                System.out.println("No customer data found.");
                return customers;
            }

            File[] files = directory.listFiles();
            if (files == null || files.length == 0) {
                System.out.println("No customer data found.");
                return customers;
            }

            for (File file : files) {
                try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file))) {
                    Customer customer = (Customer) inputStream.readObject();
                    customers.add(customer);
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println("Error occurred while reading customer data from file: " + file.getName());
                    e.printStackTrace();
                }
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
    @Override
    public void getMenu() {
        CustomerManager Cservice = new CustomerManagerImpl();

        Scanner scan = new Scanner(System.in);

        while (true) {
            System.out.println();
            System.out.println("###### 회원 관리 프로그램 ######");
            System.out.println("## [1]고객추가 [2]고객수정 [3]고객삭제 ##");
            System.out.println("## [4]고객 아이디로 검색 [5]모든 고객 불러오기 [6]프로그램종료 ##");
            System.out.println("##########################");

            System.out.println(" 메뉴 입력 : ");
            int choice = scan.nextInt();

            switch (choice) {

                case 1:
                    Cservice.addCustomer();
                    break;

                case 2:
                    Cservice.updateCustomer();
                    break;

                case 3:
                    Cservice.deleteCustomer();
                    break;

                case 4:
                    Cservice.getCustomerById();
                    break;

                case 5:
                    Cservice.getAllCustomers();
                    break;

                case 6:
                    System.out.println("프로그램 종료합니다.");
                    System.exit(0);    // 프로그램 강제종료

                default:
                    System.out.println("잘못입력 하셨습니다.");

            }
        }
    }


}
