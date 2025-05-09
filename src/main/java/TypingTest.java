import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class TypingTest {

    private static String lastInput = "";
    private static int correctWords = 0;
    private static long totalTime = 0;
    public  static int difficulty = 0;
    private static final Scanner scanner = new Scanner(System.in);

    public static class InputRunnable implements Runnable {
        @Override
        public void run() {
            try {
                lastInput = scanner.nextLine();
            } catch (Exception ignored) {
            }
        }
    }


    public static void testWord(String wordToTest) {
        try {
            System.out.println("\u001B[33m" + wordToTest + "\u001B[0m");
            lastInput = "";

            Thread inputThread = new Thread(new InputRunnable());
            long startTime = System.currentTimeMillis();
            inputThread.start();

            int timeout = wordToTest.length() * 400 * (4 - difficulty);
            try {
                inputThread.join(timeout);  // Wait for the user input or timeout
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            long endTime = System.currentTimeMillis();
            System.out.println();
            if (lastInput.equals(wordToTest)) {
                System.out.println("\u001B[32mCorrect\u001B[0m");
                totalTime += endTime - startTime;
                correctWords++;
            } else {
                System.out.println("\u001B[31mIncorrect\u001B[0m");
                totalTime += timeout;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void typingTest(List<String> inputList) throws InterruptedException {

        for (int i = 0; i < inputList.size(); i++) {
            String wordToTest = inputList.get(i);
            testWord(wordToTest);
            Thread.sleep(2000);
        }

        System.out.println("\n \u001B[34mTest Complete!\u001B[0m");
        System.out.println("\u001B[32mCorrect: " + correctWords + "\u001B[0m");
        System.out.println("\u001B[31mIncorrect: " + (10 - correctWords) + "\u001B[0m");
        System.out.println("\u001B[34mTotal Time: " + (totalTime / 1000.0) + " seconds\u001B[0m");

    }

    public static List<String> loadWords(String path) throws FileNotFoundException {
        List<String> words = new ArrayList<>();
        try (Scanner fileScanner = new Scanner(new File(path))) {
            while (fileScanner.hasNextLine()) {
                words.add(fileScanner.nextLine().trim());
            }
        }
        Collections.shuffle(words);
        return words;
    }


    public static void main(String[] args) throws InterruptedException, FileNotFoundException {
        List<String> allWords = loadWords("src/main/resources/Words.txt");
        List<String> testWords = allWords.subList(0, 10);
        System.out.println("what level of difficulty do you want to test?");
        System.out.println("(1) hard , (2) medium  (3) easy");
        difficulty = scanner.nextInt();

        typingTest(testWords);

        System.out.println("\nPress enter to exit.");
    }
}