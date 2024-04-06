package src;


import java.io.*;

public class SerializationUtils {

    public static void serialize(Object obj, String filePath) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filePath))) {
            outputStream.writeObject(obj);
            System.out.println(obj.getClass().getSimpleName() + " data saved to " + filePath);
        } catch (IOException e) {
            System.out.println("Error occurred while saving data to file: " + filePath);
            e.printStackTrace();
        }
    }

}
