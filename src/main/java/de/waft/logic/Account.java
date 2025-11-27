package de.waft.logic;

public class Account {

    int amount;

    public Account(Player player) {
        this.amount = 500;
    }


    public void add(int amount) {
        this.amount += amount;
    }

    public void subtract(int amount) {
        this.amount -= amount;
    }

    @Override
    public String toString() {
        return amount + "$";
    }

    public int getAmount() {
        return amount;
    }
}
