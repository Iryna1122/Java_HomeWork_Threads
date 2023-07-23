import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {

    private static int[] arr;
    private static int sum;
    private static double average;

    public static void main(String[] args) {
//        При старті додатку запускаються три потоки. Перший потік заповнює масив випадковими числами. Два
//        інших очікують заповнення. Коли масив заповнений, два інших потоки запускаються. Перший потік
//        знаходить суму елементів масиву, другий потік середнє арифметичне значення в масиві. Отриманий
//        масив, сума і середнє арифметичне значення повертаються в метод main, де повинні бути відображені
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter len of array: ");
        int len = scanner.nextInt();

        CountDownLatch fillLatch = new CountDownLatch(1);// це механізм синхронізації, який дозволяє одному або декільком потокам очікувати, доки інші потоки завершать свою роботу.
        CountDownLatch calculateLatch = new CountDownLatch(2);

        int[]array = new int[len];

        Thread fillThread = new Thread(()->{
            fillArrayWithRandomValue(array);
            fillLatch.countDown();
        });

        Thread sumThread = new Thread(()->{
            try{
                fillLatch.await();
                sum = getSum(array);
                calculateLatch.countDown();

            }catch (InterruptedException e){
                e.printStackTrace();
            }
        });


        Thread averageThread = new Thread(()->{

            try{
                fillLatch.await();
                average = getAverage(array);
                calculateLatch.countDown();
            }catch (InterruptedException e)
            {
                e.printStackTrace();
            }

        });


        fillThread.start();
        sumThread.start();
        averageThread.start();

        try{
            calculateLatch.await();
        }catch (InterruptedException e)
        {
            e.printStackTrace();
        }


        System.out.println("Array: ");
        for (int value:array)
        {
            System.out.print(value + ", ");
        }

        System.out.println("\n Sum: "+ sum );
        System.out.println("\n Average: " + average);


        }




private static void fillArrayWithRandomValue(int[]arr)
{
    Random random = new Random();
    for (int i = 0; i < arr.length; i++)
    {
        arr[i] = random.nextInt(100 - 1)+1;
    }
}

private static int getSum(int[]arr)
{
    int sum = 0;
    for (int i = 0; i <arr.length; i++)
    {
        sum+=arr[i];
    }
    return sum;
}

private static double getAverage(int[]arr)
{
    int sum = getSum(arr);
    return (double)sum/arr.length;
}



//        static class FillArray extends Thread
//        {
//            @Override
//            public void run()
//            {
//                synchronized (arr){
//                    Scanner scanner = new Scanner(System.in);
//                    Random random = new Random();
//                    for(int i = 0; i < arr.length; i++ )
//                    {
////                        System.out.println("Element " + (i+1) + ": ");
////                        arr[i] = scanner.nextInt();
//                        arr[i] = random.nextInt(100-1)+100;
//
////                        Random random = new Random();
////                        return random.nextInt(max - min) + min;
//                    }
//                }
//            }
//        }
    }
