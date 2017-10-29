package com.mycompany;

import com.mycompany.folks_and_philosophers.PhilosopherJavaSync;

import java.util.Arrays;

public class DinnerJavaSync {
    private static final int NUMBER_OF_FORKS = 5;
    private static final int NUMBER_OF_PHILOSOPHERS = 5;
    private static final int TIME_FOR_DINNER = 2000;
    private static final int TIME_PHILOSOPHER_EATS = 10;
    private static final int TIME_PHILOSOPHER_THINKS = 10;

    private static void testDinner() {
        PhilosopherJavaSync.setMaxNumberOfFolks(NUMBER_OF_FORKS);

        Thread[] threads = new Thread[NUMBER_OF_PHILOSOPHERS];

        Object[] forks = new Object[NUMBER_OF_FORKS];
        PhilosopherJavaSync[] philosophers = new PhilosopherJavaSync[NUMBER_OF_PHILOSOPHERS];

        for (int i = 0; i < forks.length; i++) {
            forks[i] = new Object();
        }

        for (int i = 0; i < philosophers.length - 1; i++) {
            philosophers[i] = new PhilosopherJavaSync(i + 1, forks[i], forks[i + 1], TIME_PHILOSOPHER_EATS, TIME_PHILOSOPHER_THINKS);
        }

        int lastPh = NUMBER_OF_PHILOSOPHERS;
        philosophers[lastPh - 1] = new PhilosopherJavaSync(lastPh, forks[lastPh - 1], forks[0], TIME_PHILOSOPHER_THINKS, TIME_PHILOSOPHER_THINKS);

        for (int i = 0; i < NUMBER_OF_PHILOSOPHERS; i++) {
            threads[i] = new Thread(philosophers[i]);
            threads[i].start();
        }

        try {
            Thread.sleep(TIME_FOR_DINNER);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (Thread thread : threads) {
            thread.interrupt();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        int foodWasEaten = Arrays.stream(philosophers)
                .mapToInt(PhilosopherJavaSync::getAmountOfEatenFood)
                .sum();


        for (PhilosopherJavaSync philosopher : philosophers) {
            String formattedDouble = String.format("%.2f", (double) philosopher.getAmountOfEatenFood() * 100 / foodWasEaten);
            System.out.println("PhilosopherJavaSync " + philosopher.getId() + " ate " + formattedDouble + "%.");
        }
    }

    public static void main(String[] args) {
        testDinner();
    }
}
