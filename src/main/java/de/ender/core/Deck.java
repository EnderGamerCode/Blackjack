package de.ender.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Deck {

    private final List<Card> cards;

    public Deck() {
        cards = new ArrayList<>();
        createDeck();
    }

    void createDeck() {
        cards.clear();
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                cards.add(new Card(rank, suit));
            }
        }
    }

    public List<Card> getCards() {
        return cards;
    }

    public void deleteCards() {
        this.cards.clear();
    }

    public void removeCard(Card card) {
        cards.remove(card);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(Card card : cards) {
            sb.append(card.toString()+"\n");
        }
        return sb.toString();
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card pickRandomCard() {
        Random random = new Random();
        shuffle();
        removeCard(cards.getFirst());
        return cards.getFirst();
    }

}
