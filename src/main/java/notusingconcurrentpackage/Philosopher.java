package notusingconcurrentpackage;

public class Philosopher implements Runnable {

    private int id;
    private Folk leftFolk;
    private Folk rightFolk;
    private Waiter waiter;
    private int amountOfEatenFood = 0;
    private int timePhilosopherEats;
    private int timePhilosopherThinks;

    public Philosopher(int id, Folk leftFolk, Folk rightFolk, Waiter waiter, int timePhilosopherEats, int timePhilosopherThinks) {
        this.id = id;
        this.leftFolk = leftFolk;
        this.rightFolk = rightFolk;
        this.waiter = waiter;
        this.timePhilosopherEats = timePhilosopherEats;
        this.timePhilosopherThinks = timePhilosopherThinks;
    }

    public int getAmountOfEatenFood() {
        return amountOfEatenFood;
    }

    public void run() {
        for (; ; ) {
            try {
                think();
                if (AskAboutFolkAndTake(leftFolk)) {
                    synchronized (leftFolk) {
                        if (!AskAboutFolkAndTake(rightFolk)) {
                            putFolk(leftFolk);
                            continue;
                        }
                        synchronized (rightFolk) {
                            eat();
                        }
                    }
                    putFolk(leftFolk);
                    putFolk(rightFolk);
                }

            }catch (InterruptedException e){
                return;
            }
        }
    }


    void putFolk(Folk folk) {
        waiter.putFolkOnTable(folk);
    }


    public int getId() {
        return id;
    }

    public void eat() throws InterruptedException {
        System.out.println("Philosopher " + id + " is eating!");
        Thread.sleep(timePhilosopherEats);
        amountOfEatenFood++;
    }

    public void think() throws InterruptedException {
        System.out.println("Philosopher " + id + " is thinking!");
        Thread.sleep(timePhilosopherThinks);
        amountOfEatenFood++;
    }

    public boolean AskAboutFolkAndTake(Folk folk) {
        return waiter.checkFolkAndGiveToPhilosopher(folk);
    }

}
