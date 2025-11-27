package de.waft.logic;

import de.waft.core.Deck;

public class Player {

    Hand hand;
    Deck deck;
    Account account;
    String name;
    boolean dealer;

    public Player(String playerName, Deck deck, boolean dealer) {
        hand = new Hand();
        this.deck = deck;
        this.name = playerName;
        this.dealer = dealer;

        //add money from local files here:
        if(!dealer) {
            account = new Account(this);
        }
        //

        restartGame();
    }

    public void restartGame() {
        hand.deleteCards();
        hand.addCard(deck.pickRandomCard());
        if(!dealer){
            hand.addCard(deck.pickRandomCard());
        }

    }

    public Account getAccount() {
        return account;
    }

    public Hand getHand() {
        return hand;
    }

    public String getName() {
        return name;
    }
}
