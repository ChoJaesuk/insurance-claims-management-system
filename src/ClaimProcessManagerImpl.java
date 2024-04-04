package src;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static src.CustomerManagerImpl.serializeObject;
import static src.DeserializationHelper.deserializeCustomers;

public class ClaimProcessManagerImpl implements ClaimProcessManager {
    private Scanner scan = new Scanner(System.in);
    private static List<Claim> claims = new ArrayList<>();

    @Override
    public void addClaim() {
        IdGenerator idGenerator = new IdGenerator();

        System.out.println("보험자 아이디를 입력해주세요");
        String customerId = scan.next();

        // 고객이 존재하는지 확인
        Customer customer = findCustomerById(customerId);
        if (customer != null) {
            // 고객 정보에서 필요한 정보 가져오기
            String policyHolderFullName = customer.getFullName();

            InsuranceCard cardNumber = customer.getInsuranceCard();

            String claimId = idGenerator.generateClaimId();

            // Claim date 설정
            LocalDate claimDate = LocalDate.now();

            System.out.println("진료일을 입력해주세요 (예: 2024-12-31): ");
            String dateInput2 = scan.next();
            LocalDate examDate = LocalDate.parse(dateInput2);

            System.out.println("청구 금액을 입력해주세요.");
            double amount = scan.nextDouble();

            System.out.println("은행 이름을 입력하세요");
            String bankName = scan.next();

            System.out.println("계좌번호를 입력하세요.");
            String accountNumber = scan.next();

            System.out.println("보험금을 청구할 고객의 이름을 입력하세요.");
            String insuredPersonFullName = scan.next();

            ReceiverBankingInfo bankingInfo = new ReceiverBankingInfo(bankName, insuredPersonFullName, accountNumber);

            // 고객 객체 생성
            Claim claim = new Claim(claimId, customerId, policyHolderFullName, insuredPersonFullName, cardNumber, claimDate, examDate, amount, bankingInfo);

            // 고객 객체에 클레임 추가
            if (customer.getClaims() == null) {
                customer.setClaims(new ArrayList<>());
            }
            customer.getClaims().add(claim);

            // 변경된 고객 정보 직렬화하여 저장
            serializeObject(customer, "customer/" + customerId + ".txt");

            // 클레임 정보 직렬화하여 저장
            serializeObject(claim, "claim/" + claimId + ".txt");

            System.out.println("새 클레임이 성공적으로 추가되었습니다.");

        } else {
            System.out.println("고객이 존재하지 않습니다.");
        }
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
        Scanner scanner = new Scanner(System.in);
        List<Claim> claims = deserializeClaims();
        System.out.println("수정할 claim 아이디를 입력하세요.");
        String oldClaimId = scan.next();

        Claim claimToBeUpdated = null;
        Customer customerToUpdate = null;

        // 청구에 해당하는 고객을 먼저 찾습니다.
        customerToUpdate = findCustomerByClaimId(oldClaimId);
        if (customerToUpdate != null) {
            for (Claim claim : customerToUpdate.getClaims()) {
                if (claim.getId().equals(oldClaimId)) {
                    claimToBeUpdated = claim;
                    break;
                }
            }
        }

        if (claimToBeUpdated == null || customerToUpdate == null) {
            System.out.println("해당 아이디를 가진 Claim 또는 Customer을 찾을 수 없습니다.");
            return;
        }

        boolean changesMade = false;

        System.out.println("무엇을 수정하시겠어요?");
        System.out.println("## [1]아이디 [2]진료일 변경 [3]청구 금액 변경");
        System.out.println("## [4]컨펌하기 [6]유효기간 수정 [7]취소");
        int number = scan.nextInt();

        switch (number) {
            case 1:
                // Claim 아이디 변경 로직 등을 수행
                System.out.println("새로운 Claim 아이디를 입력하세요:");
                String newClaimId = scanner.next();
                claimToBeUpdated.setId(newClaimId);
                updateClaimIdAndRenameFile(oldClaimId, newClaimId);
                changesMade = true;
                break;
            case 2:
                System.out.println("변경할 진료일 : ");
                String dateInput = scan.next();
                LocalDate newExamDate = LocalDate.parse(dateInput);
                claimToBeUpdated.setExamDate(newExamDate);
                changesMade = true;
                break;
            case 3:
                System.out.println("변경할 청구 금액 : ");
                double newAmount = scan.nextDouble();
                claimToBeUpdated.setClaimAmount(newAmount);
                changesMade = true;
                break;
            case 4:
                System.out.println("해당 청구를 컨펌하시겠습니까?");
                boolean confirm = scan.nextBoolean();
                if (confirm) {
                    claimToBeUpdated.setStatus("Processing");
                    System.out.println("청구가 컨펌되었습니다.");
                } else {
                    System.out.println("청구 컨펌이 취소되었습니다.");
                }
                changesMade = true;
                break;
            default:
                System.out.println("잘못된 선택입니다.");
                return;


        }
        if (changesMade) {
            // 변경된 Claim 정보 다시 직렬화하여 저장
            serializeObject(claimToBeUpdated, "claim/" + claimToBeUpdated.getId() + ".txt");

            // 변경된 고객 정보 다시 직렬화하여 저장
            serializeObject(customerToUpdate, "customer/" + customerToUpdate.getId() + ".txt");

            System.out.println("클레임 및 고객 정보가 성공적으로 업데이트되었습니다.");
        }

        }

    // Claim 아이디 변경 및 파일 이름 변경 로직
    public void updateClaimIdAndRenameFile(String oldClaimId, String newClaimId) {
        // 파일 경로 지정 (실제 경로에 따라 조정 필요)
        String oldFilePath = "claim/" + oldClaimId + ".txt";
        String newFilePath = "claim/" + newClaimId + ".txt";

        // 파일 객체 생성
        File oldFile = new File(oldFilePath);
        File newFile = new File(newFilePath);

        // 파일 이름 변경
        if (oldFile.renameTo(newFile)) {
            System.out.println("파일 이름이 성공적으로 변경되었습니다: " + newFilePath);
        } else {
            System.out.println("파일 이름 변경에 실패했습니다.");
        }
    }




    private Customer findCustomerByClaimId(String claimId) {
        File customerDir = new File("customer/");
        File[] customerFiles = customerDir.listFiles();
        if (customerFiles != null) {
            for (File file : customerFiles) {
                Customer customer = (Customer) deserializeObject(file.getPath());
                for (Claim claim : customer.getClaims()) {
                    if (claim.getId().equals(claimId)) {
                        return customer;
                    }
                }
            }
        }
        return null;
    }


    public void delete() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("삭제할 claim 아이디를 입력하세요:");
        String claimId = scanner.next();

        // Claim 삭제
        Claim claimToDelete = null;
        Customer customerToUpdate = findCustomerByClaimId(claimId);
        if (customerToUpdate != null) {
            List<Claim> claims = customerToUpdate.getClaims();
            for (int i = 0; i < claims.size(); i++) {
                if (claims.get(i).getId().equals(claimId)) {
                    claimToDelete = claims.remove(i); // 해당 Claim을 삭제하고 반환받음
                    break;
                }
            }

            if (claimToDelete != null) {
                // Claim이 성공적으로 찾아지고 삭제된 경우
                // 변경된 Customer 객체를 다시 직렬화하여 저장
                serializeObject(customerToUpdate, "customer/" + customerToUpdate.getId() + ".txt");

                // Claim의 텍스트 파일 삭제
                File claimFile = new File("claim/" + claimId + ".txt");
                if (claimFile.exists()) {
                    claimFile.delete();
                    System.out.println("Claim과 관련된 파일이 성공적으로 삭제되었습니다.");
                }
            } else {
                System.out.println("해당 아이디를 가진 Claim을 찾을 수 없습니다.");
            }
        } else {
            System.out.println("해당 Claim을 포함하는 고객을 찾을 수 없습니다.");
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

    private Object deserializeObject(String filePath) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error occurred during deserialization.");
            e.printStackTrace();
        }
        return null;
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

    private void serializeObject(Object obj, String filePath) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(obj);
            System.out.println(obj.getClass().getSimpleName() + " has been saved to " + filePath);
        } catch (IOException e) {
            System.out.println("Error occurred during serialization.");
            e.printStackTrace();
        }
    }

}
