package de.ender.logic;

import de.ender.core.Deck;

public class Player {

    Hand hand;
    Deck deck;
    String name;

    public Player(String playerName, Deck deck) {
        hand = new Hand();
        this.deck = deck;
        this.name = playerName;
        restartGame();
    }

    public void restartGame() {
        hand.deleteCards();
        hand.addCard(deck.pickRandomCard());
        hand.addCard(deck.pickRandomCard());
    }

    public Hand getHand() {
        return hand;
    }

    public String getName() {
        return name;
    }
}
