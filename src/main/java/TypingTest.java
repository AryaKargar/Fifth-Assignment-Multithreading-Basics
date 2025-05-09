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
    private static Scanner scanner = new Scanner(System.in);

    public static class InputRunnable implements Runnable {
        @Override
        public void run() {
            try {
                lastInput = scanner.nextLine().trim();
            } catch (Exception ignored) {
            }
        }
    }


    public static void testWord(String wordToTest) {
        try {
            System.out.println(wordToTest);
            lastInput = "";

            Thread inputThread = new Thread(new InputRunnable());
            long startTime = System.currentTimeMillis();
            inputThread.start();

            int timeout = wordToTest.length() * 700;
            try {
                inputThread.join(timeout);  // Wait for the user input or timeout
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            inputThread.interrupt();

            long endTime = System.currentTimeMillis();
            System.out.println();
            if (lastInput.equals(wordToTest)) {
                System.out.println("Correct");
                totalTime += endTime - startTime;
                correctWords++;
            } else {
                System.out.println("Incorrect");
                totalTime += timeout;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

//        System.out.println("press enter to continue");
//        scanner.nextLine();
    }

    public static void typingTest(List<String> inputList) throws InterruptedException {

        for (int i = 0; i < inputList.size(); i++) {
            String wordToTest = inputList.get(i);
            testWord(wordToTest);
            Thread.sleep(2000);
        }


        System.out.println("\n✅ Test Complete!");
        System.out.println("Correct: " + correctWords);
        System.out.println("Total Time: " + (totalTime / 1000.0) + " seconds");

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

        typingTest(testWords);

        System.out.println("\nPress enter to exit.");
//        scanner.nextLine();
    }
}