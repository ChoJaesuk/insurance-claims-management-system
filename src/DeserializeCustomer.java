package src;

import java.io.*;
import java.util.*;
public class DeserializeCustomer {
    public static List<Customer> deserializeCustomers(String fileName) {
        List<Customer> customers = null;
        try {
            // 파일에서 객체를 읽어옴
            FileInputStream fileIn = new FileInputStream(fileName);
            ObjectInputStream in = new ObjectInputStream(fileIn);

            // 객체 역직렬화
            customers = (List<Customer>) in.readObject();

            in.close();
            fileIn.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return customers;
    }
}
