package notusingconcurrentpackage;

import java.text.DecimalFormat;
import java.util.Arrays;

public class TesterNoConcurrentPackage {

    static final int numberOfFolks = 5;
    static final int numberOfPhilosophers = 5;
    static final int timeForDinner = 200000;
    static final int timePhilosopherEats = 10;
    static final int timePhilosopherThinks = 10;

    public static void testDinner() {

        Thread[] threads = new Thread[numberOfPhilosophers];

        Folk[] folks = new Folk[numberOfFolks];
        Philosopher[] philosophers = new Philosopher[numberOfPhilosophers];
        Waiter waiter = new Waiter(folks);

        for (int i = 0; i < folks.length; i++) {
            folks[i] = new Folk();
        }

        for (int i = 0; i < philosophers.length - 1; i++) {
            philosophers[i] = new Philosopher(i + 1, folks[i], folks[i + 1], waiter, timePhilosopherEats, timePhilosopherThinks);
        }

        int lastPh = numberOfPhilosophers;
        philosophers[lastPh - 1] = new Philosopher(lastPh, folks[lastPh - 1], folks[0], waiter, timePhilosopherThinks, timePhilosopherThinks);

        for (int i = 0; i < numberOfPhilosophers; i++) {
            threads[i] = new Thread(philosophers[i]);
            threads[i].start();
        }

        int foodWasEaten = 0;

        try {
            Thread.sleep(timeForDinner);
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

        foodWasEaten = Arrays.stream(philosophers)
                .mapToInt(Philosopher::getAmountOfEatenFood)
                .sum();


        for (Philosopher philosopher : philosophers) {
            String formattedDouble = String.format("%.2f", (double)philosopher.getAmountOfEatenFood()*100/foodWasEaten);
            System.out.println("Philosopher " + philosopher.getId() + " ate " + formattedDouble + "%.");
        }
    }

    public static void main(String[] args) {
        testDinner();
    }
}
