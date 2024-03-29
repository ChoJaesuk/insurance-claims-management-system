package src;

import java.time.LocalDate;
import java.util.*;
import java.io.*;

import static src.CustomerManagerImpl.deserializeCustomers;
import static src.CustomerManagerImpl.serializeObject;

public class ClaimProcessManagerImpl implements ClaimProcessManager {
    private Scanner scan = new Scanner(System.in);
    private static List<Claim> claimList = new ArrayList<>();

    @Override
    public void addClaim() {

        System.out.println("청구 아이디를 입력해주세요");
        String claimId = scan.next();

        System.out.println("보험자 아이디를 입력해주세요");
        String customerId = scan.next();

        // 고객이 존재하는지 확인
        Customer customer = findCustomerById(customerId);


        // 고객 정보에서 필요한 정보 가져오기
        String customerFullName = customer.getFullName();
        InsuranceCard cardNumber = customer.getInsuranceCard();

        // Claim date 설정
        LocalDate claimDate = LocalDate.now();


        System.out.println("진료일을 입력해주세요 (예: 2024-12-31): ");
        String dateInput2 = scan.next();
        LocalDate examDate = LocalDate.parse(dateInput2);

        System.out.println("청구 금액을 입력해주세요");
        double amount = scan.nextDouble();


        // 고객 객체 생성
        Claim claim = new Claim(claimId, customerId, customerFullName, cardNumber, claimDate, examDate, amount);

        // 고객 리스트에 추가
        claimList.add(claim);

        // 직렬화
        serializeObject(claim, "claim/" + claim.getId() + ".txt");

        System.out.println(customerId + "회원의 청구가 등록되었습니다.");


    }

    public Customer findCustomerById(String customerId) {
        List<Customer> customers = deserializeCustomers();

        for (Customer customer : customers) {
            if (customer.getId().equals(customerId)) {
                return customer;
            }
        }

        return null; // 해당 customerId에 해당하는 고객을 찾지 못한 경우
    }

    @Override
    public void update() {
        // 이름을 입력받아 해당 회원의 나이와 전화번호 수정하기
        List<Claim> claims = deserializeClaims();
        System.out.println("수정할 회원의 이름을 입력하세요.");
        String id = scan.next();

        for(int i = 0; i < claims.size(); i++) {
            Claim claim = claims.get(i);

            if(claim.getId().equals(id)) {
                while(true) {
                    Scanner scan = new Scanner(System.in);
                    System.out.println("무엇을 수정하시겠어요?");
                    System.out.println("## [1]아이디 [2]진료일 변경 [3]청구 금액 변경");
                    System.out.println("## [5]policyHolder [6]유효기간 수정 [7]취소");
                    int number = scan.nextInt();

                    switch (number) {
                        case 1:
                            System.out.println("수정할 청구 아이디 : ");
                            String newClaimId = scan.next();
                            claim.setId(newClaimId);

                            String directoryPath = "claim";
                            String oldFileName = directoryPath + "/" + id + ".txt";
                            String newFileName = directoryPath + "/" + newClaimId + ".txt";

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
                            System.out.println("변경할 진료일 : ");
                            String dateInput = scan.next();
                            LocalDate newExamDate = LocalDate.parse(dateInput);
                            claim.setExamDate(newExamDate);
                            break;
                        case 3:
                            System.out.println("변경할 청구 금액 : ");
                            double newAmount = scan.nextDouble();
                            claim.setClaimAmount(newAmount);
                        case 6:
                            System.out.println("취소");
                            break;

                    }
                    serializeObject(claim, "claim/" + claim.getId() + ".txt");
                    System.out.println(id + "님의 개인정보가 성공적으로 수정되었습니다.");
                    System.out.println(id + "님의 개인정보를 수정하였습니다.");
                    return;
                }
            }

        }

        System.out.println(id + "님은 저희 회원이 아닙니다.");
    }

    @Override
    public void delete() {
        List<Claim> claims = deserializeClaims();
        System.out.println("삭제할 청구 ID를 입력하세요.");
        String id = scan.next();

        // 역순으로 리스트를 순회하여 요소를 안전하게 제거
        for (int i = claims.size() - 1; i >= 0; i--) {
            Claim claim = claims.get(i);

            if (claim.getId().equals(id)) {
                claims.remove(i);
                System.out.println(id + "회원님을 삭제하였습니다");

                // 직렬화된 텍스트 파일 삭제
                File file = new File("claim/" + id + ".txt");
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
    }

    @Override
    public void getOne() {
        // 이름을 입력받아 해당 회원의 나이와 전화번호 출력하기
        List<Claim> claims = deserializeClaims();
        System.out.println("조회할 청구 아이디를 입력하세요.");
        String id = scan.next();

        for(int i = 0; i < claims.size(); i++) {
            Claim claim = claims.get(i);
            if(claim.getId().equals(id)) {
                System.out.println("해당 청구 id를 가진 청구 정보입니다.");
                System.out.println(claim.toString());
                return;
            }
        }
        System.out.println("잘못된 청구id입니다.");
    }

    @Override
    public void getAllClaim() {
        List<Claim> claims = deserializeClaims();
        if (claims.isEmpty()) {
            System.out.println("No customer data found.");
        } else {
            System.out.println("All Customers:");
            for (Claim claim : claims) {
                // Customer 클래스의 getCustomerInfoString() 메서드를 사용하여 고객 정보 출력
                System.out.println(claim.toString());
                System.out.println(); // 고객 정보 간의 간격
            }
        }
    }
    public static List<Claim> deserializeClaims() {
        List<Claim> claims = new ArrayList<>();

        try {
            File directory = new File("claim");
            if (!directory.exists() || !directory.isDirectory()) {
                System.out.println("No claim data found.");
                return claims;
            }

            File[] files = directory.listFiles();
            if (files == null || files.length == 0) {
                System.out.println("No claim data found.");
                return claims;
            }

            for (File file : files) {
                try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file))) {
                    Claim claim = (Claim) inputStream.readObject();
                    claims.add(claim);
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println("Error occurred while reading claim data from file: " + file.getName());
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            System.out.println("Error occurred during deserialization.");
            e.printStackTrace();
        }

        return claims;
    }

}
