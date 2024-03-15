package src;

import java.util.*;

public class ClaimProcessManagerImpl implements ClaimProcessManager {

//    @Override
//    public void add(Claim claim) {
//
//    }
//
//    @Override
//    public void update(Claim claim) {
//
//    }
//
//    @Override
//    public void delete(String claimId) {
//
//    }
//
//    @Override
//    public Claim getOne(String claimId) {
//        return null;
//    }

    @Override
    public void add() {

    }

    @Override
    public void update() {

    }

    @Override
    public void delete() {

    }

    @Override
    public Claim getOne() {
        return null;
    }

    @Override
    public List<Claim> getAll() {
        return null;
    }

    @Override
    public void getMenu() {
        ClaimProcessManager service = new ClaimProcessManagerImpl();
        Scanner scan = new Scanner(System.in);

        while (true) {
            System.out.println();
            System.out.println("###### 회원 관리 프로그램 ######");
            System.out.println("## [1]청구 추가 [2]청구 수정 [3]청구 삭제 ##");
            System.out.println("## [4]청구 하나 가져오기 [5]청구 다 가져오기 [6]프로그램종료 ##");
            System.out.println("##########################");

            System.out.println(" 메뉴 입력 : ");
            int choice = scan.nextInt();

            switch (choice) {

                case 1:
                    service.add();
                    break;

                case 2:
                    service.update();
                    break;

                case 3:
                    service.delete();
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
