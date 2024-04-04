package src;

import java.util.*;

public class InsuranceClaimManager {
    private CustomerManager customerManager;
    private ClaimProcessManager claimProcessManager;

    public InsuranceClaimManager(CustomerManager customerManager, ClaimProcessManager claimProcessManager) {

        this.customerManager = customerManager;
        this.claimProcessManager = claimProcessManager;

    }

    void start() {

        Scanner scan = new Scanner(System.in);

        while (true) {
            System.out.println();
            System.out.println("###### 회원 관리 프로그램 ######");
            System.out.println("## [1]고객 관리 [2]청구 관리 ##");
            System.out.println("## [3]보고서 생성 및 저장 [4]프로그램 종료 ##");
            System.out.println("##########################");

            System.out.println(" 메뉴 입력 : ");
            int choice = scan.nextInt();

            switch (choice) {

                case 1:
                    menuForManagingCustomer();
                    break;

                case 2:
                    menuForManagingClaim();
                    break;

                case 3:
//                    service.getMenu();
//                    break;

                case 4:
                    System.out.println("프로그램 종료합니다.");
                    System.exit(0);    // 프로그램 강제종료

                default:
                    System.out.println("잘못입력 하셨습니다.");


        }
    }
    }

    public void menuForManagingCustomer() {

            Scanner scan = new Scanner(System.in);

            while (true) {
                System.out.println();
                System.out.println("###### 회원 관리 프로그램 ######");
                System.out.println("## [1]고객추가 [2]고객수정 [3]고객삭제 ##");
                System.out.println("## [4]고객 아이디로 검색 [5]모든 고객 불러오기 [6]dependent 불러오기 [7]종료 ##");
                System.out.println("## [7]dependent 삭제 [2]고객수정 [3]고객삭제 ##");
                System.out.println("##########################");

                System.out.println(" 메뉴 입력 : ");
                int choice = scan.nextInt();

                switch (choice) {

                    case 1:
                        customerManager.addCustomer();
                        break;

                    case 2:
                        customerManager.updateCustomer();
                        break;

                    case 3:
                        customerManager.deleteCustomer();
                        break;

                    case 4:
                        customerManager.getCustomerById();
                        break;

                    case 5:
                        customerManager.getAllCustomers();
                        break;

                    case 6:
                        customerManager.getAllDependents();
                        break;

                    case 7:



                    case 9:
                        System.out.println("프로그램 종료합니다.");
                        System.exit(0);    // 프로그램 강제종료

                    default:
                        System.out.println("잘못입력 하셨습니다.");

            }
        }
    }

    public void menuForManagingClaim() {
        Scanner scan = new Scanner(System.in);
        InsurancePaymentManager insurancePaymentManager = new InsurancePaymentManager();

        while (true) {
            System.out.println();
            System.out.println("###### 회원 관리 프로그램 ######");
            System.out.println("## [1]청구 추가 [2]청구 수정 [3]청구 삭제 ##");
            System.out.println("## [4]청구 검색 [5]청구 다 가져오기 [6]보험금 지급 ##");
            System.out.println("## [7]보험금 지급 내역서 리스트 출력 [8]종료 ##");
            System.out.println("##########################");

            System.out.println(" 메뉴 입력 : ");
            int choice = scan.nextInt();

            switch (choice) {

                case 1:
                    claimProcessManager.addClaim();
                    break;

                case 2:
                    claimProcessManager.update();
                    break;

                case 3:
                    claimProcessManager.delete();
                    break;

                case 4:
                    claimProcessManager.getOne();
                    break;

                case 5:
                    claimProcessManager.getAllClaim();
                    break;

                case 6:
                    insurancePaymentManager.processInsurancePayment();
                    break;

                case 8:
                    System.out.println("프로그램 종료합니다.");
                    System.exit(0);    // 프로그램 강제종료

                default:
                    System.out.println("잘못입력 하셨습니다.");

            }
        }
    }


}
