package com.mycompany;

public class Waiter {

    private int numberOfFreeFolks;
    Folk[] folks;

    public Waiter(Folk[] folks) {
        this.numberOfFreeFolks = folks.length;
        this.folks = folks;
    }

    synchronized boolean checkFolkAndGiveToPhilosopher(Folk folk) {
        if (folk.isFree() && numberOfFreeFolks != 1) {
            giveFolkToPhilosopher(folk);
            return true;
        } else return false;
    }

    private void giveFolkToPhilosopher(Folk folk) {
        folk.setFree(false);
        numberOfFreeFolks--;
    }

    synchronized void putFolkOnTable(Folk folk) {
        folk.setFree(true);
        numberOfFreeFolks++;
    }
}
