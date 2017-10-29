package com.mycompany.folks_and_philosophers;

public class PhilosopherJavaSync implements Runnable {

    private int id;
    private final Object leftFork;
    private final Object rightFork;
    private int amountOfEatenFood = 0;
    private int timePhilosopherEats;
    private int timePhilosopherThinks;

    static private volatile int numberOfFreeForks = 5;

    public PhilosopherJavaSync(int id, Object leftFork, Object rightFork, int timePhilosopherEats, int timePhilosopherThinks) {
        this.id = id;
        this.leftFork = leftFork;
        this.rightFork = rightFork;
        this.timePhilosopherEats = timePhilosopherEats;
        this.timePhilosopherThinks = timePhilosopherThinks;
    }

    public int getAmountOfEatenFood() {
        return amountOfEatenFood;
    }

    public void run() {
        while (true) {
            try {
                think();
                if (canTakeFolk()) {
                    synchronized (leftFork) {
                        decrementFreeFolks();
                        synchronized (rightFork) {
                            decrementFreeFolks();
                            eat();
                        }
                        incrementFreeFolks();
                    }
                    incrementFreeFolks();
                }
            } catch (InterruptedException e) {
                return;
            }
        }
    }

    public int getId() {
        return id;
    }

    private void eat() throws InterruptedException {
        System.out.println("Philosopher " + id + " is eating!");
        Thread.sleep(timePhilosopherEats);
        amountOfEatenFood++;
    }

    private void think() throws InterruptedException {
        System.out.println("Philosopher " + id + " is thinking!");
        Thread.sleep(timePhilosopherThinks);
    }

    synchronized static private boolean canTakeFolk() {
        return numberOfFreeForks > 1;
    }

    synchronized static private void decrementFreeFolks() {
        numberOfFreeForks--;
    }

    synchronized static private void incrementFreeFolks() {
        numberOfFreeForks++;
    }

    static public void setMaxNumberOfFolks(int maxFolks) {
        numberOfFreeForks = maxFolks;
    }
}
