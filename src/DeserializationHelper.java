package src;

import java.io.*;
import java.util.*;

public class DeserializationHelper {
    public static List<Customer> deserializeCustomers() {
        List<Customer> customers = new ArrayList<>();

        try {
            // Deserialize policyHolder and dependent files
            File customerDirectory = new File("customer/");
            if (customerDirectory.exists() && customerDirectory.isDirectory()) {
                File[] customerFiles = customerDirectory.listFiles();
                if (customerFiles != null) {
                    for (File file : customerFiles) {
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
            Object object;
            while ((object = inputStream.readObject()) != null) {
                if (object instanceof Customer) {
                    Customer customer = (Customer) object;
                    customers.add(customer);

                    // Deserialize claims for the customer
                    File claimFile = new File("claim/" + customer.getId() + ".txt");
                    if (claimFile.exists()) {
                        try (ObjectInputStream claimInputStream = new ObjectInputStream(new FileInputStream(claimFile))) {
                            while ((object = claimInputStream.readObject()) != null) {
                                if (object instanceof Claim) {
                                    customer.addClaim((Claim) object);
                                }
                            }
                        } catch (EOFException e) {
                            // Reached end of file, continue to the next file
                        }
                    }
                }
            }
        } catch (EOFException e) {
            // Reached end of file, continue to the next file
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error occurred while reading customer data from file: " + file.getName());
            e.printStackTrace();
        }
    }

}
