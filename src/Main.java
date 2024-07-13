import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static AtomicInteger atomicInt3 = new AtomicInteger(0);
    public static AtomicInteger atomicInt4 = new AtomicInteger(0);
    public static AtomicInteger atomicInt5 = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {

        List<Thread> threads = new ArrayList<>();

        Random random = new Random(); //Для генерации 100 000 коротких слов
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
//            System.out.println(texts[i]);
        }

        long startTs = System.currentTimeMillis(); // start time

            Thread thread = new Thread(() -> { //сгенерированное слово является палиндромом
                for (String text : texts) {
                    String textPalindrome = text;
                    int textLengthPalindrome = textPalindrome.length();
                    int countPalindrome = 0;
                    int length2 = textLengthPalindrome / 2;
                    for (int i = 0; i < length2; i++) {
                        if (textPalindrome.charAt(i) == textPalindrome.charAt(textLengthPalindrome - i - 1)) {
                            countPalindrome++;
                        }
                    }
                    if (countPalindrome == length2) {
                        if (textLengthPalindrome == 3) {
                            atomicInt3.incrementAndGet();
                        }
                        if (textLengthPalindrome == 4) {
                            atomicInt4.incrementAndGet();
                        }
                        if (textLengthPalindrome == 5) {
                            atomicInt5.incrementAndGet();
                        }
                    }
                }
            });
            threads.add(thread);
            thread.start();

            thread = new Thread(() -> { //сгенерированное слово состоит из одной и той же буквы
                for (String text : texts) {
                    String textSameLetter = text;
                    int textLengthSameLetter = textSameLetter.length();
                    int countSameLetter = 0;
                    for (int i = 0; i < textLengthSameLetter - 1; i++) {
                        if (textSameLetter.charAt(i) == textSameLetter.charAt(i + 1)) {
                            countSameLetter++;
                        }
                    }
                    if (countSameLetter == textLengthSameLetter - 1) {
                        if (textLengthSameLetter == 3) {
                            atomicInt3.incrementAndGet();
                        }
                        if (textLengthSameLetter == 4) {
                            atomicInt4.incrementAndGet();
                        }
                        if (textLengthSameLetter == 5) {
                            atomicInt5.incrementAndGet();
                        }
                    }
                }
            });
            threads.add(thread);
            thread.start();

            thread = new Thread(() -> { //буквы в слове идут по возрастанию
                for (String text : texts) {
                    String textLettersAscending = text;
                    int textLengthLettersAscending = textLettersAscending.length();
                    int countLettersAscending = 0;
                    for (int i = 0; i < textLengthLettersAscending - 1; i++) {
                        if (textLettersAscending.codePointAt(i) <= textLettersAscending.codePointAt(i + 1)) {
                            countLettersAscending++;
                        }
                    }
                    if (countLettersAscending == (textLengthLettersAscending - 1)) {
                        if (textLengthLettersAscending == 3) {
                            atomicInt3.incrementAndGet();
                        }
                        if (textLengthLettersAscending == 4) {
                            atomicInt4.incrementAndGet();
                        }
                        if (textLengthLettersAscending == 5) {
                            atomicInt5.incrementAndGet();
                        }
                    }
                }
            });
            threads.add(thread);
            thread.start();

        for (Thread t : threads) {
            t.join();
        }

        System.out.println("Красивых слов с длиной 3: " + atomicInt3.get() + " шт");
        System.out.println("Красивых слов с длиной 4: " + atomicInt4.get() + " шт");
        System.out.println("Красивых слов с длиной 5: " + atomicInt5.get() + " шт");

        long endTs = System.currentTimeMillis(); // end time
        System.out.println("Time: " + (endTs - startTs) + "ms");

    }//конец main

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}

// Потоки в одном цикле 13 809 ms
// У каждого потока свой цикл 27 ms