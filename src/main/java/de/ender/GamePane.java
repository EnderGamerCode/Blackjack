package de.ender;

import de.ender.logic.Player;
import de.ender.logic.Hand;
import de.ender.core.Card;
import de.ender.core.Deck;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class GamePane extends StackPane {

    private Deck deck;
    private Player player;
    private Player dealer;

    private StackPane playerHandPane;
    private StackPane dealerHandPane;

    private Text playerValueText;
    private Text dealerValueText;

    public GamePane() {

        this.setPadding(new Insets(20));

        this.deck = new Deck();
        this.player = new Player("Player", deck);
        this.dealer = new Player("Dealer", deck);


        // Build layout sections
        VBox dealerBox = createPlayerBox(dealer);
        VBox playerBox = createPlayerBox(player);

        HBox buttons = new HBox(20);
        buttons.setAlignment(Pos.CENTER);

        Button hitButton = new Button("Hit");
        Button standButton = new Button("Stand");

        buttons.getChildren().addAll(hitButton, standButton);

        // Game logic
        hitButton.setOnAction(e -> {
            if(player.getHand().canPlayerHit()) {
                player.getHand().addCard(deck.pickRandomCard());
                updateHandPane(playerHandPane, player.getHand());
            }
        });

        standButton.setOnAction(e -> {
            player.getHand().addCard(deck.pickRandomCard());
            while(dealer.getHand().getBestTotal()<17) {
                dealer.getHand().addCard(deck.pickRandomCard());
            }
            updateHandPane(dealerHandPane, dealer.getHand());
        });

        // Arrange everything vertically
        VBox layout = new VBox(45);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(dealerBox, buttons, playerBox);

        this.getChildren().add(layout);
    }

    private VBox createPlayerBox(Player p) {
        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);

        Text name = new Text(p.getName());
        name.setFont(Font.font(22));

        StackPane handPane = createHandPane(p.getHand());

        Text valueText = new Text("Total: " + p.getHand().getDisplayValue());
        valueText.setFont(Font.font(18));

        if (p.getName().equals("Player")) {
            playerHandPane = handPane;
            playerValueText = valueText;
        } else {
            dealerHandPane = handPane;
            dealerValueText = valueText;
        }

        box.getChildren().addAll(name, handPane, valueText);
        return box;
    }



    private StackPane createHandPane(Hand hand) {
        StackPane pane = new StackPane();
        pane.setPrefHeight(120);
        pane.setPrefWidth(600);

        updateHandPane(pane, hand);
        return pane;
    }

    private void updateHandPane(StackPane pane, Hand hand) {

        // Clear card text
        pane.getChildren().clear();

        for (int i = 0; i < hand.getCards().size(); i++) {
            Text t = new Text(hand.getCards().get(i).toString());
            t.setFont(Font.font(20));
            t.setTranslateX(i * 60);
            pane.getChildren().add(t);
        }

        // Update total text
        String total = "Total: " + hand.getDisplayValue();

        if (pane == playerHandPane) {
            playerValueText.setText(total);
        } else if (pane == dealerHandPane) {
            dealerValueText.setText(total);
        }
    }


}
