package src;

import java.io.*;
import java.util.*;

public class DeserializationHelper {
    public static List<Customer> deserializeCustomers() {
        List<Customer> customers = new ArrayList<>();

        try {
            // Deserialize policyHolder files
            File policyHolderDirectory = new File("customer/policyHolder");
            if (policyHolderDirectory.exists() && policyHolderDirectory.isDirectory()) {
                File[] policyHolderFiles = policyHolderDirectory.listFiles();
                if (policyHolderFiles != null) {
                    for (File file : policyHolderFiles) {
                        deserializeCustomerFromFile(file, customers);
                    }
                }
            }

            // Deserialize dependent files
            File dependentDirectory = new File("customer/dependent");
            if (dependentDirectory.exists() && dependentDirectory.isDirectory()) {
                File[] dependentFiles = dependentDirectory.listFiles();
                if (dependentFiles != null) {
                    for (File file : dependentFiles) {
                        deserializeCustomerFromFile(file, customers);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error occurred during deserialization.");
            e.printStackTrace();
        }

        return customers;
    }


    private static void deserializeCustomerFromFile(File file, List<Customer> customers) {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file))) {
            Customer customer = (Customer) inputStream.readObject();
            customers.add(customer);

            // Deserialize claims for the customer
            File claimFile = new File("claim/" + customer.getId() + ".txt");
            if (claimFile.exists()) {
                try (ObjectInputStream claimInputStream = new ObjectInputStream(new FileInputStream(claimFile))) {
                    Object object;
                    while ((object = claimInputStream.readObject()) != null) {
                        if (object instanceof Claim) {
                            customer.addClaim((Claim) object);
                        }
                    }
                } catch (EOFException e) {
                    // Reached end of file, continue to the next file
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error occurred while reading customer data from file: " + file.getName());
            e.printStackTrace();
        }
    }
}
