package de.ender.core;

public enum Suit {
    CLUB("Club", '♣'),
    DIAMOND("Diamond", '♦'),
    HEART("Heart", '♥'),
    SPADE("Spade", '♠');

    private final String displayName;
    private final char symbol;

    Suit(String displayName, char symbol) {
        this.displayName = displayName;
        this.symbol = symbol;
    }

    public String getDisplayName() {
        return displayName;
    }

    public char getSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return displayName /*+ " " + symbol*/;
    }
}

