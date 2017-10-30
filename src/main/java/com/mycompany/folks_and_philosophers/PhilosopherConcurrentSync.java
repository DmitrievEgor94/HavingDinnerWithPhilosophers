package com.mycompany.folks_and_philosophers;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;

public class PhilosopherConcurrentSync implements Runnable {

    private int id;

    private Lock leftFork;
    private Lock rightFork;
    private int amountOfEatenFood = 0;
    private int timePhilosopherEats;
    private int timePhilosopherThinks;

    private static Semaphore semFreeForks;

    public PhilosopherConcurrentSync(int id, Lock leftFork, Lock rightFork, int timePhilosopherEats, int timePhilosopherThinks) {
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
                semFreeForks.acquire();
                think();
                leftFork.lock();
                try {
                    rightFork.lock();
                    try {
                        eat();
                    } finally {
                        rightFork.unlock();
                    }
                } finally {
                    leftFork.unlock();
                }
                semFreeForks.release();
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

    static public void setMaxNumberOfFreeForks(int maxNumberOfFreeForks) {
        semFreeForks = new Semaphore(maxNumberOfFreeForks-1, true);
    }
}
