import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

public class Task2 {

  private static  List<Integer>primeNum = new ArrayList<>();
    private static  List<Long>factorialNum = new ArrayList<>();
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Path:");

        String filePath = scanner.next();

        CountDownLatch fillLatch = new CountDownLatch(1);
        CountDownLatch calculatelatch = new CountDownLatch(2);


        Thread fillThread = new Thread(()->{
            try {
                fillFileWithRandomNumber(filePath);
                fillLatch.countDown();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        Thread primeThread = new Thread(()->{
           try{
               fillLatch.await();
               primeNum = findPrimeNumber(filePath);
               calculatelatch.countDown();
           }catch (InterruptedException e)
           {
               e.printStackTrace();
           } catch (IOException e) {
               throw new RuntimeException(e);
           }
        });


        Thread factorialThread = new Thread(()->{

            try{
                fillLatch.await();
                factorialNum = calculateFactorial(filePath);
                calculatelatch.countDown();
            }catch (InterruptedException e)
            {
                e.printStackTrace();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        //Start Thread
        fillThread.start();
        primeThread.start();
        factorialThread.start();

        //Wait while all thread ended
        try{
            calculatelatch.await();
        }catch (InterruptedException e)
        {
            e.printStackTrace();
        }


        System.out.println("Prime numbers: " + primeNum);
        System.out.println("Factorial numbers: " + factorialNum);

    }

    private static void fillFileWithRandomNumber(String pathFile)throws IOException
    {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(pathFile))) {
            Random random = new Random();
            for (int i = 0; i<100; i++)
            {
                int randomNumber = random.nextInt(100-1)+1;
                writer.write(randomNumber+"\n");
            }
        }
    }

    private static List<Integer>findPrimeNumber(String path)throws IOException
    {
        List<Integer> primeNumbers = new ArrayList<>();

        try(BufferedReader reader = new BufferedReader(new FileReader(path)))
        {
            String line;
            while((line = reader.readLine())!=null)
            {
                int number = Integer.parseInt(line);
                if(isPrime(number))
                {
                    primeNumbers.add(number);
                }
            }
        }

        return primeNumbers;
    }

    private static List<Long>calculateFactorial(String path)throws IOException
    {
        List<Long>factorials = new ArrayList<>();

        try(BufferedReader reader = new BufferedReader(new FileReader(path)))
        {
            String Line;
            while((Line=reader.readLine())!=null)
            {
                int number = Integer.parseInt(Line);
                long factorial = calculateFactorial(number);
                factorials.add(factorial);
            }
        }
        return factorials;
    }


    private static boolean isPrime(int number)
    {
        if(number <=1)return false;
//        for(int i = 0; i<=Math.sqrt(number); i++)
//        {
//            if(number%i==0)return false;
//
//        }

        return true;
    }


    private static long calculateFactorial(int number)
    {
        if(number == 0 || number==1)return 1;

        long result = 1;
        for(int i=2; i<=number; i++)
        {
            result*=i;
        }
        return result;
    }


}
//D:\SomeDir\notes.txt