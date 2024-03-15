package src;

import java.io.*;
import java.util.*;

public class CustomerManagerImpl implements CustomerManager {
    private List<Customer> list = new ArrayList<>();
    private Scanner scan = new Scanner(System.in);
    private Customer customer;


    @Override
    public void addCustomer() {
        System.out.println("아이디를 입력해주세요");
        String id = scan.next();

        System.out.println("이름을 입력해주세요");
        String fullName = scan.next();

        System.out.println("보험카드 번호 입력해주세요");
        String insuranceCard = scan.next();

        System.out.println("dependents");
        String dependents = scan.next();

        System.out.println("claims");
        String claims = scan.next();


        list.add(new Customer(id, fullName, insuranceCard, dependents, claims));
        Customer customer = new Customer(id, fullName, insuranceCard, dependents, claims);
        System.out.println(fullName + "회원이 등록되었습니다.");
        saveCustomerToFile(customer);
        
    }

    // 고객 데이터를 텍스트 파일에 저장하는 메소드
    private void saveCustomerToFile(Customer customer) {
        if (customer == null) {
            System.out.println("고객 데이터가 없습니다.");
            return;
        }

        String fileName = "customer/" + "customer-" + customer.getId() + ".txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            // 고객 데이터를 텍스트 파일에 저장
            writer.write(customer.toString());
        } catch (IOException e) {
            System.out.println("고객 데이터를 텍스트 파일에 저장하는 데 실패했습니다: " + e.getMessage());
        }
    }

    @Override
    public void updateCustomer() {
        // 이름을 입력받아 해당 회원의 나이와 전화번호 수정하기
        System.out.println("수정할 회원의 이름을 입력하세요.");
        String id = scan.next();

        for(int i = 0; i < list.size(); i++) {
            Customer customer = list.get(i);

            if(customer.getId().equals(id)) {
                while(true) {
                    Scanner scan = new Scanner(System.in);
                    System.out.println("무엇을 수정하시겠어요?");
                    System.out.println("## [1]아이디 [2]이름 [3]카드번호");
                    System.out.println("## [4]dependents [5]claims [6]취소");
                    int number = scan.nextInt();

                    switch (number) {
                        case 1:
                            System.out.println("수정할 아이디 : ");
                            String newId = scan.next();
                            customer.setId(newId);
                            break;

                        case 2:
                            System.out.println("수정할 이름 : ");
                            String newFullName = scan.next();
                            customer.setFullName(newFullName);
                            break;

                        case 3:
                            System.out.println("수정할 카드번호 : ");
                            String newInsuranceNumber = scan.next();
                            customer.setInsuranceCard(newInsuranceNumber);
                            break;

                        case 4:
                            System.out.println("수정할 dependents : ");
                            String newDependents = scan.next();
                            customer.setInsuranceCard(newDependents);
                            break;

                        case 5:
                            System.out.println("수정할 claims : ");
                            String newClaims = scan.next();
                            customer.setInsuranceCard(newClaims);
                            break;

                        case 6:
                            System.out.println("취소");
                            break;



                    }
                    System.out.println(id + "님의 개인정보를 수정하였습니다.");
                    return;
                }


            }
        }
        System.out.println(id + "님은 저희 회원이 아닙니다.");
    }

    @Override
    public void deleteCustomer() {
        // 이름을 입력받아 해당 회원을 삭제하기

        System.out.println("삭제할 회원의 이름을 입력하세요.");
        String id = scan.next();

        for(int i = 0; i < list.size(); i++) {
            Customer customer = list.get(i);

            if(customer.getId().equals(id)) {
                list.remove(i);
                System.out.println(id + "회원님을 삭제하였습니다");
                return;
            }
        }
        System.out.println(id + "님은 저희 회원이 아닙니다.");
    }

    @Override
    public void getCustomerById() {
        // 이름을 입력받아 해당 회원의 나이와 전화번호 출력하기

        System.out.println("조회할 이름을 입력하세요.");
        String id = scan.next();

        for(int i = 0; i < list.size(); i++) {
            Customer customer = list.get(i);
            if(customer.getId().equals(id)) {
                System.out.println(id + "회원님의 나이 : " + customer.getFullName());
                System.out.println(id + "회원님의 보험카드 번호 : " + customer.getInsuranceCard());
                System.out.println(id + "회원님의 청구 : " + customer.getClaims());
                System.out.println(id + "회원님의  : " + customer.getDependents());
                return;
            }
        }
        System.out.println(id + "님은 저희 회원이 아닙니다.");
    }


    @Override
    public void getAllCustomers() {
        for (int i = 0; i < list.size(); i++) {
            Customer customer = list.get(i);
            String customerId = customer.getId();
            String fullName = customer.getFullName();
            String insuranceCard = customer.getInsuranceCard();

            System.out.println("아이디 : " + customerId + "이름 : " + fullName + "보험카드번호 : " + insuranceCard);
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
