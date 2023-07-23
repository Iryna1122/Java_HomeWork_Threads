import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;

//import org.apache.commons.io.FileUtils;
public class Task3 {

    public static void main(String[] args) throws IOException {

        String firstPath = getUserInput("Enter first directory: ");
        String destinationPath = getUserInput("Enter second directory");

        File sourceDirectory = new File(firstPath);
        File destinationDirectory = new File(destinationPath);

        //COPY

        Thread copyThread = new Thread(()->{
           try{
               FileUtils.copyDirectory(sourceDirectory,destinationDirectory);
               System.out.println("Successfully copied directory from " + firstPath + " to " + destinationPath);


           }catch (IOException e)
           {
               e.printStackTrace();
           }
        });
        FileUtils.copyDirectory(sourceDirectory,destinationDirectory);
        System.out.println("Successfully copied directory from " + firstPath + " to " + destinationPath);


    }

    private static String getUserInput(String message) {
        //Scanner scanner = new Scanner(System.in);
        System.out.println(message);
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
}
