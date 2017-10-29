package com.mycompany;

//Критическая проблема.
//Можно сделать решение и без этого класса (и класса Folk).
//Сейчас оно пререусложнено в несколько раз.
public class Waiter {

    private int numberOfFreeFolks;
    //поле должно быть private final
    Folk[] folks;

    public Waiter(Folk[] folks) {
        this.numberOfFreeFolks = folks.length;
        this.folks = folks;
    }

    synchronized boolean checkFolkAndGiveToPhilosopher(Folk folk) {
        if (folk.isFree() && numberOfFreeFolks != 1) {
            giveFolkToPhilosopher(folk);
            return true;
        } else return false; //нарушение JCC - много операторов в одной строке + добавить {}
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
