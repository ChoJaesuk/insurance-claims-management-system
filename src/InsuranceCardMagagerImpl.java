package src;

import java.util.*;
import java.io.*;
public class InsuranceCardMagagerImpl implements InsuranceCardMagager{
    @Override
    public void addInsuranceCard() {

        Scanner scan = new Scanner(System.in);

        System.out.println();
        System.out.println("###### 보험 카드 번호를 입력하세요 ######");
        System.out.println("카드 번호 입력 : ");

        String cardNumber = scan.next();

        InsuranceCard insuranceCard = new InsuranceCard(cardNumber);



    }

    @Override
    public void getAllCard() {
        System.out.println();
    }

    @Override
    public void getMenu() {

        Scanner scan = new Scanner(System.in);

        while(true) {
            System.out.println();
            System.out.println("###### 회원 관리 프로그램 ######");
            System.out.println("## [1]보험 카드 생성 [2] 모든 보험 카드 불러오기 [3] 프로그램 종료");
            System.out.println("##########################");

            System.out.println(" 메뉴 입력 : ");

            int choice = scan.nextInt();

            switch (choice) {
                case 1:
                    addInsuranceCard();
                    break;

                case 2:
                    getAllCard();
                    break;

                case 3:
                    System.out.println("프로그램 종료합니다.");
                    System.exit(0);    // 프로그램 강제종료
            }
        }

    }
}
