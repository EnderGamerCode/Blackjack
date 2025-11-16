package de.ender.core;

import de.ender.core.Card;
import de.ender.core.Deck;

public class BlackjackCards {

    static void main() {
        Deck deck = new Deck();

        System.out.println("\n\nDECK: \n");
        System.out.println(deck.toString());
        System.out.println("\n\nRANDOM CARD: \n");
        Card randomCard = deck.pickRandomCard();
        System.out.println(randomCard.toString()+"\n\n");

    }

}
