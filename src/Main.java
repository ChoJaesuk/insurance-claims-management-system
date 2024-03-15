package src;

import java.util.*;
public class Main {
    public static void main(String[] args) {

        ClaimProcessManagerImpl service = new ClaimProcessManagerImpl();
        CustomerManagerImpl CService = new CustomerManagerImpl();

        Scanner scan = new Scanner(System.in);

        while (true) {
            System.out.println();
            System.out.println("###### 회원 관리 프로그램 ######");
            System.out.println("## [1]고객 관리 [2]보험 카드 관리 [3]청구 관리 ##");
            System.out.println("## [4]보고서 생성 및 저장 [5]프로그램 종료 ##");
            System.out.println("##########################");

            System.out.println(" 메뉴 입력 : ");
            int choice = scan.nextInt();

            switch (choice) {

                case 1:
                    CService.getMenu();
                    break;

                case 2:
                    service.update();
                    break;

                case 3:
                    service.getMenu();
                    break;

                case 4:
                    service.getOne();
                    break;

                case 5:
                    service.getAll();
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
