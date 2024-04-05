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
            System.out.println("###### insurance claims management system ######");
            System.out.println("## [1] managing Customers [2] Managing Claims [3] End the program ##");
            System.out.println("#################################################");

            System.out.println(" Input Menu : ");
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
                    System.out.println("Wrong Input.");


        }
    }
    }

    public void menuForManagingCustomer() {

            Scanner scan = new Scanner(System.in);

            while (true) {
                System.out.println();
                System.out.println("###### Managing Customers ######");
                System.out.println("## [1] Add Customer [2] Update Customer [3] Delete Customer ##");
                System.out.println("## [4] Search  A Customer By ID [5] Get All Customers  ##");
                System.out.println("## [6] Get All Dependent [7] Back to the Main Menu [8] End the program ##");

                System.out.println(" Input Menu : ");
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
                        start();
                        break;

                    case 8:
                        System.out.println("End the program.");
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
            System.out.println("###### Managing Claims ######");
            System.out.println("## [1] Add Claim [2] Update Claim [3] Delete Claim ##");
            System.out.println("## [4] Search A Claim By ID [5] Get All Claims [6] Insurance Payment ##");
            System.out.println("## [7] List All Documents [8] Back to the Main Menu [9] End the program##");
            System.out.println("##########################");

            System.out.println(" Input Menu : ");
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

                case 7:
                    claimProcessManager.getDocumentsList();
                    break;

                case 8:
                    start();
                    break;

                case 9:
                    System.out.println("End the program");
                    System.exit(0);    // 프로그램 강제종료

                default:
                    System.out.println("Wrong Input!");

            }
        }
    }


}
