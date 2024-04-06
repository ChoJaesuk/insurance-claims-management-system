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

        listCustomers();

        System.out.println("Enter the Customer's ID : ");
        String customerId = scan.next();

        // Ensure that the customer exists
        Customer customer = findCustomerById(customerId);
        if (customer != null) {
            // Get the information you need from customer information

            InsuranceCard cardNumber = customer.getInsuranceCard();

            String claimId = idGenerator.generateClaimId();

            // Set Claim date
            LocalDate claimDate = LocalDate.now();

            String insuredPersonFullName = customer.getFullName();

            System.out.println("Enter the Exam Date (ex : 2024-12-31) : ");
            String dateInput2 = scan.next();
            LocalDate examDate = LocalDate.parse(dateInput2);

            System.out.println("Etner the Claim Amount : ");
            double amount = scan.nextDouble();

            System.out.println("Enter the customer's Bank Name : ");
            String bankName = scan.next();

            System.out.println("Enter the customer's Bank account : ");
            String accountNumber = scan.next();

            ReceiverBankingInfo bankingInfo = new ReceiverBankingInfo(bankName, insuredPersonFullName, accountNumber);

            // Create claim object
            Claim claim = new Claim(claimId, customerId, insuredPersonFullName, cardNumber, claimDate, examDate, amount, bankingInfo);

            // Add Claims to Customer Objects
            if (customer.getClaims() == null) {
                customer.setClaims(new ArrayList<>());
            }
            customer.getClaims().add(claim);

            // Serialize and store changed customer information
            serializeObject(customer, "customer/" + customerId + ".txt");

            // Serialize and store claim information
            serializeObject(claim, "claim/" + claimId + ".txt");

            System.out.println(customerId + "'s new claim has been successfully added.");

        } else {
            System.out.println("No customer with that customer's ID");
        }
    }

    public Customer findCustomerById(String customerId) {
        List<Customer> customers = deserializeCustomers();

        for (Customer customer : customers) {
            if (customer.getId().equals(customerId)) {
                return customer;
            }
        }

        return null; // If you have not found a customer that corresponds to that customerId
    }

    @Override
    public void update() {
        List<Claim> claims = deserializeClaims();
        listClaims();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the Claim's ID to update");
        String oldClaimId = scan.next();

        Claim claimToBeUpdated = null;
        Customer customerToUpdate = null;

        // Find the customer who is eligible for the claim first.
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
            System.out.println("No Claim or Customer with that ID was found.");
            return;
        }

        boolean changesMade = false;

        System.out.println("Plase choose an option to update");
        System.out.println("## [1] Claim's ID [2] Exam Date [3] Claim Amount");
        System.out.println("## [4] Confirm(NEW -> PROCESSING) [6] 취소 ##");
        int number = scan.nextInt();

        switch (number) {
            case 1:
                // Perform Claim ID change logic, etc
                System.out.println("Enter a NEW Claim's iD : ");
                String newClaimId = scanner.next();
                claimToBeUpdated.setId(newClaimId);
                updateClaimIdAndRenameFile(oldClaimId, newClaimId);
                changesMade = true;
                break;
            case 2:
                System.out.println("Enter a NEW Exam Date (ex : 2024-12-31) : ");
                String dateInput = scan.next();
                LocalDate newExamDate = LocalDate.parse(dateInput);
                claimToBeUpdated.setExamDate(newExamDate);
                changesMade = true;
                break;
            case 3:
                System.out.println("Enter NEW Claim Amount : ");
                double newAmount = scan.nextDouble();
                claimToBeUpdated.setClaimAmount(newAmount);
                changesMade = true;
                break;
            case 4:
                System.out.println("Are you sure to Confirm the Claim? (true / false)");
                boolean confirm = scan.nextBoolean();
                if (confirm) {
                    claimToBeUpdated.setStatus("Processing");
                    System.out.println(oldClaimId + "has been successfully confirmed.");
                } else {
                    System.out.println("Your claim confirmation has been cancelled.");
                }
                changesMade = true;
                break;
            default:
                System.out.println("Wrong Input!");
                return;


        }
        if (changesMade) {
            // Reserial and save changed Claim information
            serializeObject(claimToBeUpdated, "claim/" + claimToBeUpdated.getId() + ".txt");

            // Reserial and store changed customer information
            serializeObject(customerToUpdate, "customer/" + customerToUpdate.getId() + ".txt");

            System.out.println("Claims and customer information have been successfully updated.");
        }

        }

    // Change Claim ID and change file name logic
    public void updateClaimIdAndRenameFile(String oldClaimId, String newClaimId) {
        String oldFilePath = "claim/" + oldClaimId + ".txt";
        String newFilePath = "claim/" + newClaimId + ".txt";

        // Create File Object
        File oldFile = new File(oldFilePath);
        File newFile = new File(newFilePath);

        // Change file name
        if (oldFile.renameTo(newFile)) {
            System.out.println("File name changed successfully : " + newFilePath);
        } else {
            System.out.println("Failed to rename file.");
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
        listClaims();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the Claim's ID to delete. : ");
        String claimId = scanner.next();

        // Delete a Claim
        Claim claimToDelete = null;
        Customer customerToUpdate = findCustomerByClaimId(claimId);
        if (customerToUpdate != null) {
            List<Claim> claims = customerToUpdate.getClaims();
            for (int i = 0; i < claims.size(); i++) {
                if (claims.get(i).getId().equals(claimId)) {
                    claimToDelete = claims.remove(i); // Delete and return the Claim
                    break;
                }
            }

            if (claimToDelete != null) {
                // Claim has been successfully found and deleted
                // Reserialize and save changed Customer objects
                serializeObject(customerToUpdate, "customer/" + customerToUpdate.getId() + ".txt");

                // Delete Clime's text file
                File claimFile = new File("claim/" + claimId + ".txt");
                if (claimFile.exists()) {
                    claimFile.delete();
                    System.out.println("The file associated with Claim has been successfully deleted.");
                }
            } else {
                System.out.println("No Claims with that ID were found.");
            }
        } else {
            System.out.println("No customer could be found containing that Claim.");
        }
    }
    @Override
    public void getOne() {
        List<Claim> claims = deserializeClaims();
        if (claims.isEmpty()) {
            System.out.println("There are no registered claims.");
            return;
        }

        for (Claim claim : claims) {
            System.out.println("Claim ID :" + claim.getId() + "\tInsured Person ID :" + claim.getInsuredPerson()); // Customer 객체의 toString 메소드 호출
        }
        System.out.println("Please enter the id of the claim you want to search about.");
        String id = scan.next();

        for(int i = 0; i < claims.size(); i++) {
            Claim claim = claims.get(i);
            if(claim.getId().equals(id)) {
                System.out.println("Claim information with that claim ID.");
                System.out.println(claim.toString());
                return;
            }
        }
        System.out.println("Wrong Claim ID");
    }

    @Override
    public void getDocumentsList() {
        List<Claim> claims = deserializeClaims();

        for (Claim claim : claims) {
            if (claim.getDocuments() != null && !claim.getDocuments().isEmpty()) { // 문서 목록이 null이 아니고, 비어 있지 않은 경우에만 출력
                System.out.println(claim.getDocuments());
            }

        }
    }

    @Override
    public void getAllClaim() {
        List<Claim> claims = deserializeClaims();
        Scanner scan = new Scanner(System.in);
        System.out.println("[1] All Claims [2] New Claims [3] Processing Claims [4] Done Claims [5] 취소");
        System.out.println("Input Option : ");
        int choice = scan.nextInt();

        switch (choice) {
            case 1:
                if (claims.isEmpty()) {
                    System.out.println("No claim data found.");
                } else {
                    System.out.println("All Claims:");
                    for (Claim claim : claims) {
                        // Output customer information using the getCustomerInfoString() method in the Customer class
                        System.out.println(claim.toString());
                        System.out.println();
                    }
                }
                break;

            case 2:

                findClaimWithStatus(claims, "New");
                break;

            case 3:

                findClaimWithStatus(claims, "Processing");
                break;

            case 4:

                findClaimWithStatus(claims, "Done");
                break;

            default:
                break;
        }
    }

    private void findClaimWithStatus(List<Claim> claims, String status) {

        for(Claim claim : claims) {
            if(status == null || status.equals(claim.getStatus())) {
                System.out.println(claim.toString());
                System.out.println();
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

    public void listCustomers() {
        List<Customer> customers = deserializeCustomers();
        System.out.println("-------------- All Customers --------------");
        for(Customer customer : customers) {
            System.out.println("Customer ID : " + customer.getId() + "\tCustomer Name : " + customer.getFullName());
        }
    }

    public void listClaims() {
        List<Claim> claims = deserializeClaims();
        int maxNameLength = 0;

        // Find the length of the longest customer name
        for (Claim claim : claims) {
            if (claim.getInsuredPersonFullName().length() > maxNameLength) {
                maxNameLength = claim.getInsuredPersonFullName().length();
            }
        }

        System.out.println("-------------- All Claims --------------");
        for (Claim claim : claims) {
            // Set the output format using String.format()
            System.out.printf("Claim ID : %s\tClaim Name : %-" + maxNameLength + "s Claim Status : %s%n",
                    claim.getId(), claim.getInsuredPersonFullName(), claim.getStatus());
        }
    }

}
