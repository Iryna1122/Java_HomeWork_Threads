import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Task4 {
    public static void main(String[] args) throws InterruptedException, IOException {

//        Користувач з клавіатури вводить шлях до директорії і слово для пошуку. Після чого запускаються два
//        потоки. Перший повинен знайти файли, що містять задане слово і злити їх вміст в один файл. Другий потік
//        чекає закінчення роботи першого потоку. Після чого проводить вирізання всіх заборонених слів (список
//                цих слів потрібно зчитати із файлу з забороненими словами) із отриманого файлу. В методі main необхідно
//        відобразити статистику виконаних операцій


        String directoryPath = getUserInput("Enter the directory path: ");
        String searchWord = getUserInput("Enter the word to search: ");


        File directory = new File(directoryPath);
        List<String> forbiddenWords = readForbiddenWordsFromFile("forbidden_words.txt");

        Thread searchandMergeThread = new Thread(()->{
            try {
                File mergedFile = searchAndMergeFiles(directory, searchWord);
                System.out.println("Merged file created: " + mergedFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

        Thread cutForbiddenWordsThread = new Thread(() -> {
            try {
                searchandMergeThread.join(); // Чекаємо закінчення першого потоку
                File mergedFile = new File(directory, "merged_file.txt");
                cutForbiddenWords(mergedFile, forbiddenWords);
                System.out.println("Forbidden words cut from the merged file.");
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        });

        // Запускаємо потоки
        searchandMergeThread.start();
        cutForbiddenWordsThread.start();

        // Чекаємо, поки всі потоки завершаться
        searchandMergeThread.join();
        cutForbiddenWordsThread.join();

        System.out.println("All operations completed.");

    }

    private static String getUserInput(String message)
    {
        System.out.println(message);
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    private static File searchAndMergeFiles(File directory, String searchWord) throws IOException {
        File mergedFile = new File(directory, "merged_file.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(mergedFile));

        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".txt")) {
                    Scanner scanner = new Scanner(file);
                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine();
                        if (line.contains(searchWord)) {
                            writer.write(line);
                            writer.newLine();
                        }
                    }
                    scanner.close();
                }
            }
        }

        writer.close();
        return mergedFile;
    }

    private static void cutForbiddenWords(File file, List<String> forbiddenWords) throws IOException {
        File tempFile = new File(file.getAbsolutePath() + ".tmp");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

        String line;
        while ((line = reader.readLine()) != null) {
            for (String forbiddenWord : forbiddenWords) {
                line = line.replace(forbiddenWord, "");
            }
            writer.write(line);
            writer.newLine();
        }

        reader.close();
        writer.close();

        // Замінюємо вихідний файл на тимчасовий
        if (file.delete()) {
            if (!tempFile.renameTo(file)) {
                throw new IOException("Could not rename the temporary file.");
            }
        } else {
            throw new IOException("Could not delete the original file.");
        }
    }

    private static List<String> readForbiddenWordsFromFile(String filePath) throws IOException {
        List<String> forbiddenWords = new ArrayList<>();
        File file = new File(filePath);
        if (file.exists()) {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                forbiddenWords.add(line.trim());
            }
            reader.close();
        }
        return forbiddenWords;
    }


}
