package src;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

import static src.ClaimProcessManagerImpl.deserializeClaims;
import static src.DeserializationHelper.deserializeCustomers;

public class CustomerManagerImpl implements CustomerManager {
    private static List<Customer> list = new ArrayList<>();
    private Scanner scan = new Scanner(System.in);


    @Override
    public void addCustomer() {

        InsuranceCard insuranceCard2 = new InsuranceCard();
        IdGenerator idGenerator = new IdGenerator();

        String id = idGenerator.generateCustomerId();

        System.out.println("Enter the customer's Full Name (Don't use spaces.)");
        String fullName = scan.next();

        System.out.println("Enter the Expiration Date (ex : 2024-12-31): ");
        String dateInput = scan.next();
        LocalDate expirationDate = LocalDate.parse(dateInput);

        System.out.println("is the customer Policy Holder? (Please input 'true'");
        boolean answer = scan.nextBoolean();

        System.out.println("Enter customer's Policy owner (Don't use spaces.)");
        String policyOwner = scan.next();

        // Create an Insurance Card
        String cardNumber = insuranceCard2.generateRandomCardNumber(); // 중복 검사를 포함한 카드 번호 생성
        InsuranceCard insuranceCard = new InsuranceCard(cardNumber);
        insuranceCard.setCardInfo(fullName, policyOwner, expirationDate);

        // Create Customer Object
        Customer cus = new Customer(id, fullName, answer, policyOwner, expirationDate, insuranceCard);

        // Add the customer list
        list.add(cus);

        // serialization
        SerializationUtils.serialize(cus, "customer/" + cus.getId() + ".txt");
        SerializationUtils.serialize(insuranceCard, "insuranceCard/" + insuranceCard.getCardNumber() + ".txt");
        System.out.println(fullName + "has been successfully registered.");


    }

    public void addDependent(String policyHolderId) {

        InsuranceCard insuranceCard2 = new InsuranceCard();
        IdGenerator idGenerator = new IdGenerator();

        // to find PolicyHolder
        Customer policyHolder = findCustomerById(policyHolderId);
        if (policyHolder == null) {
            System.out.println("There is no PolicyHolder with the ID you entered.");
            return;
        }

        // Get Dependent information input
        String dependentId = idGenerator.generateCustomerId();

        System.out.println("Enter the dependent's Full Name (Don't use spaces.)");
        String dependentFullName = scan.next();

        String cardNumber = insuranceCard2.generateRandomCardNumber(); // 보험 카드 번호 생성
        InsuranceCard dependentInsuranceCard = new InsuranceCard(cardNumber);
        dependentInsuranceCard.setCardInfo(dependentFullName, policyHolder.getPolicyOwner(), policyHolder.getInsuranceCard().getExpirationDate());

        // Create a Dependent Object
        Customer dependent = new Customer(dependentId, dependentFullName, false, policyHolder.getPolicyOwner(), policyHolder.getExpirationDate(), dependentInsuranceCard);

        // Add to policyholder's dependents list
        if (policyHolder.getDependents() == null) {
            policyHolder.setDependents(new ArrayList<>());
        }
        policyHolder.getDependents().add(dependent);

        // Serialize changed PolicyHolder information
        SerializationUtils.serialize(policyHolder, "customer/" + policyHolder.getId() + ".txt");
        SerializationUtils.serialize(dependent, "customer/" + dependentId + ".txt");
        SerializationUtils.serialize(dependentInsuranceCard, "insuranceCard/" + dependentInsuranceCard.getCardNumber() + ".txt");

        System.out.println(dependentFullName + "has been successfully registered.");
    }

    @Override
    public void updateCustomer() {
        listCustomersWithoutDependents();

        System.out.println("Enter the Customer ID to update (NOT DEPENDENT)");
        String customerId = scan.next();

        // Find a customer
        Customer customerToUpdate = findCustomerById(customerId);
        if (customerToUpdate == null) {
            System.out.println("There is no Customer with the ID you entered.");
            return;
        }

        System.out.println("Please choose an option to update");
        System.out.println("## [1] Customer's ID [2] Customer's Full Name [3] Customer's Expiration Date ##");
        System.out.println("## [4] Add Dependent [5] Delete Dependent [6] Update Dependent(ID, FullName) [7] End the program ##");
        int choice = scan.nextInt();
        scan.nextLine(); // Remove remaining line change characters after entering numbers

        boolean customerUpdated = false;

        switch (choice) {
            case 1:
                System.out.println("Enter a NEW Customer's ID. :");
                String newId = scan.next();
                // Delete existing files and rename files with new IDs
                if (!customerId.equals(newId)) {
                    renameCustomerFile(customerId, newId);
                    customerToUpdate.setId(newId);
                    customerUpdated = true;
                }
                break;
            case 2:
                System.out.println("Enter a NEW Customer's Full Name. (Don't use spaces.) :");
                String newFullName = scan.next();
                customerToUpdate.setFullName(newFullName);
                customerUpdated = true;
                break;
            case 3:
                System.out.println("Enter a NEW Customer's Expiration Date. : (ex : 2024-12-31):");
                String dateInput = scan.next();
                LocalDate newExpirationDate = LocalDate.parse(dateInput);
                customerToUpdate.getInsuranceCard().setExpirationDate(newExpirationDate); // Update customer's insurance card expiration date
                if (customerToUpdate.getDependents() != null) {
                    for (Customer dependent : customerToUpdate.getDependents()) {
                        if(dependent.getInsuranceCard() != null) {
                            dependent.getInsuranceCard().setExpirationDate(newExpirationDate); // 종속자의 보험 카드 만료 날짜 업데이트
                            SerializationUtils.serialize(dependent, "customer/" + dependent.getId() + ".txt"); // 변경된 종속자 정보 직렬화
                        }
                    }
                }
                customerUpdated = true;
                break;
            case 4:
                addDependent(customerId);
                break;

            case 5:
                deleteDependent(customerId);
                break;

            case 6:
                updateDependentInfo(customerId);
                break;

            case 7:
                System.out.println("End the program.");
                System.exit(0);

            default:
                System.out.println("Wrong Input!");

        }

        if (customerUpdated) {
            SerializationUtils.serialize(customerToUpdate, "customer/" + customerToUpdate.getId() + ".txt");
            System.out.println("Customer information has been successfully updated.");
            updateRelatedClaims(customerToUpdate);
        }
    }

    private void updateRelatedClaims(Customer updatedCustomer) {
        List<Claim> updatedClaims = new ArrayList<>(); // List to store updated claims

        for (Claim claim : updatedCustomer.getClaims()) {
            // Update the relevant information within the claim according to the customer information update.
            claim.setInsuredPersonFullName(updatedCustomer.getFullName());

            if (claim.getBankingInfo() != null) {
                claim.getBankingInfo().setReceiverName(updatedCustomer.getFullName());
            }
            updatedClaims.add(claim); // Add to updated claims list
            SerializationUtils.serialize(claim, "claim/" + claim.getId() + ".txt"); // Serialize and store updated claim information
        }

        // Update claims list of customer objects to updated claims list
        updatedCustomer.setClaims(updatedClaims);
        // Reserial and store changed customer information
        SerializationUtils.serialize(updatedCustomer, "customer/" + updatedCustomer.getId() + ".txt");

        System.out.println("All related claims have been updated with the customer's new information.");
    }

    private void updateDependentInfo(String customerId) {
        Customer policyHolder = findCustomerById(customerId);
        if (policyHolder != null && policyHolder.getDependents() != null) {
            System.out.println("List of " + customerId + " dependents:");
            for (Customer dependent : policyHolder.getDependents()) {
                System.out.println("Dependent ID: " + dependent.getId() + ", Dependent Full Name: " + dependent.getFullName());
            }
            System.out.println("\nEnter a Dependent ID to update");
            String dependentId = scan.next();
            Customer dependentToUpdate = null;

            for (Customer dependent : policyHolder.getDependents()) {
                if (dependent.getId().equals(dependentId)) {
                    dependentToUpdate = dependent;
                    break;
                }
            }

            if (dependentToUpdate == null) {
                System.out.println("Dependent not found.");
                return;
            }

            System.out.println("Please choose an option to update:");
            System.out.println(" [1] Dependent's Full Name (Don't use spaces.)");
            int choice = scan.nextInt();
            scan.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("Enter a new Dependent's Full Name (Don't use spaces.) : ");
                    String newName = scan.nextLine();
                    dependentToUpdate.setFullName(newName);

                    // Update all claims related to dependents here
                    updateRelatedDependentClaims(dependentToUpdate);

                    // Serialize PolicyHolder objects reflecting changed information
                    SerializationUtils.serialize(policyHolder, "customer/" + policyHolder.getId() + ".txt");

                    updatePolicyHolderDependents(policyHolder, dependentToUpdate);

                    System.out.println("Dependent's information has been successfully updated.");
                    break;
                default:
                    System.out.println("Wrong Input!");
            }

        } else {
            System.out.println("You can not update dependent's information.");
        }
    }
    private void updatePolicyHolderDependents(Customer policyHolder, Customer updatedDependent) {
        // Update the list of dependents in the PolicyHolder.
        List<Customer> updatedDependents = new ArrayList<>();
        for (Customer dependent : policyHolder.getDependents()) {
            if (dependent.getId().equals(updatedDependent.getId())) {
                updatedDependents.add(updatedDependent); // Add changed dependents
            } else {
                updatedDependents.add(dependent);
            }
        }
        policyHolder.setDependents(updatedDependents); // Set changed list to policyHolder

        // Reserial and save changed PolicyHolder objects
        SerializationUtils.serialize(policyHolder, "customer/" + policyHolder.getId() + ".txt");
    }
    private void updateRelatedDependentClaims(Customer dependent) {
        List<Claim> updatedClaims = new ArrayList<>();
        List<Claim> allClaims = deserializeClaims(); // Recall all claim information in a de-serialized manner.

        for (Claim claim : allClaims) {
            if (claim.getInsuredPersonId().equals(dependent.getId())) {
                claim.setInsuredPersonFullName(dependent.getFullName());
                // BankingInfo의 Receiver Name도 업데이트
                if (claim.getBankingInfo() != null) {
                    claim.getBankingInfo().setReceiverName(dependent.getFullName());
                }
                updatedClaims.add(claim); // Add updated claims to list
                // Serialize and store updated claim information
                SerializationUtils.serialize(claim, "claim/" + claim.getId() + ".txt");
            }
        }
        dependent.setClaims(updatedClaims); // Update claims list within dependent object
        SerializationUtils.serialize(dependent, "customer/" + dependent.getId() + ".txt"); // Reserialize and save changed dependent objects
        System.out.println("All related claims for the dependent have been updated.");
    }


    private void renameCustomerFile(String oldId, String newId) {
        File oldFile = new File("customer/" + oldId + ".txt");
        File newFile = new File("customer/" + newId + ".txt");
        if (oldFile.exists()) {
            if (oldFile.renameTo(newFile)) {
                System.out.println("The file name has been changed successfully.");
            } else {
                System.out.println("Failed to rename file.");
            }
        } else {
            System.out.println("No file exists to change.");
        }
    }

    //In this method, when you delete a customer by deleting a customer,
    // all of the Depent, InsuranceCard, and Claim associated with that customer are deleted.
    @Override
    public void deleteCustomer() {
        List<Customer> customers = deserializeCustomers();

        listCustomersWithoutDependents();

        System.out.println("Enter the Customer's ID to delete");
        String id = scan.next();

        Customer customerToDelete = null;
        for (Customer customer : customers) {
            if (customer.getId().equals(id)) {
                customerToDelete = customer;
                break;
            }
        }

        if (customerToDelete == null) {
            System.out.println(id + " is not our customer.");
            return;
        }

        // Delete Claim files for customers and dependents
        deleteClaimsForCustomer(id);
        if (customerToDelete.getDependents() != null) {
            for (Customer dependent : customerToDelete.getDependents()) {
                deleteClaimsForCustomer(dependent.getId());
            }
        }

        // Delete Customer's Dependents information
        if (customerToDelete.getDependents() != null && !customerToDelete.getDependents().isEmpty()) {
            for (Customer dependent : customerToDelete.getDependents()) {
                File dependentFile = new File("customer/" + dependent.getId() + ".txt");
                if (dependentFile.exists()) {
                    dependentFile.delete();
                    System.out.println("Dependent " + dependent.getId() + " file deleted.");
                }

                if (dependent.getInsuranceCard() != null) {
                    File insuranceCardFile = new File("insuranceCard/" + dependent.getInsuranceCard().getCardNumber() + ".txt");
                    if (insuranceCardFile.exists()) {
                        insuranceCardFile.delete();
                        System.out.println("Insurance Card file for " + dependent.getInsuranceCard().getCardNumber() + " deleted.");
                    }
                }
            }
        }

        // Delete Customer(policyHolder) information
        customers.remove(customerToDelete);
        File customerFile = new File("customer/" + id + ".txt");
        if (customerFile.exists()) {
            customerFile.delete();
            System.out.println("Customer " + id + " file deleted.");
        }

        // Delete Customer's Insurance Card File
        if (customerToDelete.getInsuranceCard() != null) {
            File insuranceCardFile = new File("insuranceCard/" + customerToDelete.getInsuranceCard().getCardNumber() + ".txt");
            if (insuranceCardFile.exists()) {
                insuranceCardFile.delete();
                System.out.println("Insurance Card file for " + customerToDelete.getInsuranceCard().getCardNumber() + " deleted.");
            }
        }

        // Reserial and save changed customer lists
        System.out.println(id + " has been successfully deleted.");
    }

    // Method for deleting claims of customers that you want to delete.
    private void deleteClaimsForCustomer(String customerId) {
        File claimDirectory = new File("claim/");
        if (claimDirectory.exists() && claimDirectory.isDirectory()) {
            File[] claimFiles = claimDirectory.listFiles();
            if (claimFiles != null) {
                for (File claimFile : claimFiles) {
                    // Re-serialize Clim objects
                    Claim claim = deserializeClaimFromFile(claimFile);
                    if (claim != null && claim.getInsuredPersonId().equals(customerId)) {
                        // Delete the corresponding Claim file
                        claimFile.delete();
                        System.out.println("Claim file " + claimFile.getName() + " deleted.");
                    }
                }
            }
        }
    }

    private static Claim deserializeClaimFromFile(File claimFile) {
        Claim claim = null; // 초기화
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(claimFile))) {
            claim = (Claim) inputStream.readObject(); // Re-serialize Claim objects
        } catch (EOFException e) {

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error occurred while reading claim data from file: " + claimFile.getName());
            e.printStackTrace();
        }
        return claim; // Returning a Re-Serialized Claim Object
    }

    public void deleteDependent(String policyHolderId) {
        // to find a PolicyHolder
        Customer policyHolder = findCustomerById(policyHolderId);
        if (policyHolder == null || policyHolder.getDependents() == null || policyHolder.getDependents().isEmpty()) {
            System.out.println("There is no Customer with the ID you entered or the Customer does not have dependents.");
            return;
        }
        if (policyHolder != null && policyHolder.getDependents() != null) {
            System.out.println("List of " + policyHolderId + " dependents:");
            for (Customer dependent : policyHolder.getDependents()) {
                System.out.println("Dependent ID: " + dependent.getId() + ", Dependent Full Name: " + dependent.getFullName());
            }
        }

        System.out.println("Enter the Dependent ID to delete");
        String dependentId = scan.next();

        // Find dependents to delete from the dependents list
        Customer dependentToDelete = policyHolder.getDependents().stream()
                .filter(dependent -> dependent.getId().equals(dependentId))
                .findFirst()
                .orElse(null);

        if (dependentToDelete == null) {
            System.out.println("There is no Dependent with the ID you entered.");
            return;
        }

        // Delete the claim information requested by the subordinate
        deleteClaimsForDependent(dependentId);

        // Delete files related to dependents
        deleteFile("customer/" + dependentId + ".txt");

        if (dependentToDelete.getInsuranceCard() != null) {
            String cardNumber = dependentToDelete.getInsuranceCard().getCardNumber();
            deleteFile("insuranceCard/" + cardNumber + ".txt"); // Delete Insurance Card Files
        }

        // Delete from Dependents list
        policyHolder.getDependents().remove(dependentToDelete);

        // Serialize and store changed PolicyHolder information
        SerializationUtils.serialize(policyHolder, "customer/" + policyHolder.getId() + ".txt");
        System.out.println(dependentId + " and associated files have been successfully deleted.");
    }


    // This method is to delete the claim of the dependent that you delete.
    private void deleteClaimsForDependent(String dependentId) {
        File claimDirectory = new File("claim/");
        if (claimDirectory.exists() && claimDirectory.isDirectory()) {
            File[] claimFiles = claimDirectory.listFiles();
            if (claimFiles != null) {
                for (File claimFile : claimFiles) {
                    Claim claim = deserializeClaimFromFile(claimFile);
                    if (claim != null && claim.getInsuredPersonId().equals(dependentId)) {
                        if (claimFile.delete()) {
                            System.out.println("Deleted claim file: " + claimFile.getName());
                        }
                    }
                }
            }
        }
    }

    // A method of deleting a file according to the path of the file.
    private void deleteFile(String filePath) {
        File fileToDelete = new File(filePath);
        if (fileToDelete.exists() && fileToDelete.delete()) {
            System.out.println("Deleted file: " + filePath);
        } else {
            System.out.println("Failed to delete file: " + filePath);
        }
    }


    @Override
    public void getCustomerById() {

        List<Customer> customers = deserializeCustomers();
        listCustomers();
        System.out.println("Enter the Customer's ID to search.");
        String id = scan.next();

        for(int i = 0; i < customers.size(); i++) {
            Customer customer = customers.get(i);
            if(customer.getId().equals(id)) {
                System.out.println("The information of\t" + id);
                System.out.println(customer.toString());
                return;
            }
        }
        System.out.println(id + "is not our customer.");
    }


    public void getAllCustomers() {
        List<Customer> customers = deserializeCustomers(); // Bring up all customers in the system.

        if (customers.isEmpty()) {
            System.out.println("There is no Customers in our system.");
            return;
        }

        for (Customer customer : customers) {
                System.out.println(customer.toString());
                System.out.println("---------------------------------------");
        }
    }



    @Override
    public void getAllDependents() {
        List<Customer> customers = deserializeCustomers();

        if (customers.isEmpty()) {
            System.out.println("There is no Dependents in our system.");
            return;
        }

        System.out.println("Information for all dependents stored on the system : ");

        for (Customer policyHolder : customers) {
            // Output only if there is a dependent
            if (policyHolder.getDependents() != null && !policyHolder.getDependents().isEmpty()) {
                for (Customer dependent : policyHolder.getDependents()) {
                    System.out.println("Dependent ID: " + dependent.getId());
                    System.out.println("Dependent Full Name: " + dependent.getFullName());
                    System.out.println("Policy Holder ID: " + policyHolder.getId());
                    System.out.println("Policy Owner: " + dependent.getPolicyOwner());
                    if (dependent.getInsuranceCard() != null) {
                        System.out.println("Expiration Date : " + dependent.getInsuranceCard().getExpirationDate());
                        System.out.println("Insurance CardNumber: " + dependent.getInsuranceCard().getCardNumber());
                        System.out.println("-------------------------------------------------------");
                    } else {
                        System.out.println("No Insurance CardNumber");
                    }
                }

            }
        }
    }


    // This method outputs the customer's ID and name for a comfortable UI.
    public void listCustomers() {
        List<Customer> customers = deserializeCustomers();
        System.out.println("-------------- All Customers --------------");
        for(Customer customer : customers) {
            System.out.println("Customer ID : " + customer.getId() + "\tCustomer Name : " + customer.getFullName());
        }
    }

    // This method outputs the customer's ID and name for a comfortable UI.
    public void listCustomersWithoutDependents() {
        List<Customer> customers = deserializeCustomers();
        System.out.println("-------------- All Customers --------------");
        for(Customer customer : customers) {
            if(customer.getIsPolicyHolder() == true) {
                System.out.println("Customer ID : " + customer.getId() + "\tCustomer Name : " + customer.getFullName());
            }
        }
    }

    private Customer findCustomerById(String customerId) {
        // This method uses deserializeCustomers to bring all customers,
        // Finds and returns a Customer object that matches the given CustomerId.
        List<Customer> customers = deserializeCustomers();
        for (Customer customer : customers) {
            if (customer.getId().equals(customerId)) {
                return customer;
            }
        }
        return null; // return null if not found
    }

}
