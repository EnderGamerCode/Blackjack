package de.ender.core;

import java.awt.image.BufferedImage;

public class Card {

    Rank rank;
    Suit suit;
    BufferedImage image;

    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
        image = getImage();
    }

    @Override
    public String toString() {
        return suit.getSymbol() + " " + rank;
    }

    BufferedImage getImage() {
        //layer1 = card outline+rank
        //layer2 = card suit
        return null;
    }


    public Rank getRank() {
        return rank;
    }

    public Suit getSuit() {
        return suit;
    }

}
