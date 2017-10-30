package com.mycompany;

import com.mycompany.folks_and_philosophers.PhilosopherConcurrentSync;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DinnerConcurrentSync {
    private static final int NUMBER_OF_FORKS = 5;
    private static final int NUMBER_OF_PHILOSOPHERS = 5;
    private static final int TIME_FOR_DINNER = 200000;
    private static final int TIME_PHILOSOPHER_EATS = 10;
    private static final int TIME_PHILOSOPHER_THINKS = 10;

    private static void testDinner() {
        PhilosopherConcurrentSync.setMaxNumberOfFreeForks(NUMBER_OF_FORKS );

        ExecutorService executor = Executors.newFixedThreadPool(NUMBER_OF_PHILOSOPHERS);

        Lock[] locks = new Lock[NUMBER_OF_FORKS];
        PhilosopherConcurrentSync[] philosophers = new PhilosopherConcurrentSync[NUMBER_OF_PHILOSOPHERS];

        for (int i = 0; i < locks.length; i++) {
            locks[i] = new ReentrantLock();
        }

        for (int i = 0; i < philosophers.length - 1; i++) {
            philosophers[i] = new PhilosopherConcurrentSync(i + 1, locks[i], locks[i + 1], TIME_PHILOSOPHER_EATS, TIME_PHILOSOPHER_THINKS);
        }

        int lastPh = NUMBER_OF_PHILOSOPHERS;
        philosophers[lastPh - 1] = new PhilosopherConcurrentSync(lastPh, locks[lastPh - 1], locks[0], TIME_PHILOSOPHER_THINKS, TIME_PHILOSOPHER_THINKS);

        for (int i = 0; i < NUMBER_OF_PHILOSOPHERS; i++) {
            executor.submit(philosophers[i]);
        }

        try {
            Thread.sleep(TIME_FOR_DINNER);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        executor.shutdownNow();

        int foodWasEaten = Arrays.stream(philosophers)
                .mapToInt(PhilosopherConcurrentSync::getAmountOfEatenFood)
                .sum();

        for (PhilosopherConcurrentSync philosopher : philosophers) {
            String formattedDouble = String.format("%.2f", (double) philosopher.getAmountOfEatenFood() * 100 / foodWasEaten);
            System.out.println("Philosopher " + philosopher.getId() + " ate " + formattedDouble + "%.");
        }
    }

    public static void main(String[] args) {
        testDinner();
    }
}
