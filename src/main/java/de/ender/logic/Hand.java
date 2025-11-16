package de.ender.logic;

import de.ender.core.Card;

import java.util.ArrayList;
import java.util.List;

public class Hand {

    private final List<Card> cards;
    private boolean canHit;

    public Hand() {
        cards = new ArrayList<>();
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

    public void addCard(Card card) {
        cards.add(card);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(Card card : cards) {
            sb.append(card.toString()+"\n");
        }
        return sb.toString();
    }

    public int getRawTotal() {
        int total = 0;
        for (Card c : cards) {
            total += c.getRank().getValue();  // Ace = 1
        }
        return total;
    }

    public int getAceCount() {
        int aces = 0;
        for (Card c : cards) {
            if (c.getRank().getDisplayName().equals("Ace")) {
                aces++;
            }
        }
        return aces;
    }

    public int getBestTotal() {
        int raw = getRawTotal();
        int aces = getAceCount();

        if (aces > 0 && raw + 10 <= 21) {
            return raw + 10;
        }

        return raw;
    }

    public String getDisplayValue() {
        int raw = getRawTotal();
        int best = getBestTotal();

        if (best != raw) {
            return raw + "/" + best;
        }

        return String.valueOf(raw);
    }

    public boolean canPlayerHit() {
        return getBestTotal() < 21;
    }

    public boolean isBust() {
        return getBestTotal() > 21;
    }

    public boolean isBlackjack() {
        return cards.size() == 2 && getBestTotal() == 21;
    }

}
