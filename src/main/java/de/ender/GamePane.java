package de.ender;

import de.ender.logic.GameState;
import de.ender.logic.Player;
import de.ender.logic.Hand;
import de.ender.core.Card;
import de.ender.core.Deck;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Timer;
import java.util.TimerTask;

public class GamePane extends StackPane {

    private GameState gameState = GameState.START;

    private Deck deck;
    private Player player;
    private Player dealer;

    private Font standard_font = Font.font("Comic Sans MS", 20);

    private StackPane playerHandPane;
    private StackPane dealerHandPane;

    private Text playerValueText;
    private Text dealerValueText;

    private StackPane gameOverOverlay;
    private Text gameOverMessage;
    private Button restartButton;

    private Timer actionTimer = new Timer();

    public GamePane() {

        this.setPadding(new Insets(20));

        startNewGame();

        // Build layout sections
        VBox dealerBox = createPlayerBox(dealer);
        VBox playerBox = createPlayerBox(player);

        HBox buttons = new HBox(20);
        buttons.setAlignment(Pos.CENTER);

        Button hitButton = new Button("Hit");
        hitButton.getStyleClass().add("standard-button");
        Button standButton = new Button("Stand");
        standButton.getStyleClass().add("standard-button");

        buttons.getChildren().addAll(hitButton, standButton);

        // Game logic
        hitButton.setOnAction(e -> {
            if (gameState != GameState.PLAYER_TURN) return;

            if (!player.getHand().isBust()) {
                player.getHand().addCard(deck.pickRandomCard());
                updateHandPane(playerHandPane, player.getHand());
            }

            if (player.getHand().isBust()) {
                gameState = GameState.DEALER_TURN;
                runDealerTurn();
            }
        });

        standButton.setOnAction(e -> {
            if (gameState != GameState.PLAYER_TURN) return;

            gameState = GameState.DEALER_TURN;
            runDealerTurn();
        });

        // Arrange everything vertically
        VBox layout = new VBox(45);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(dealerBox, buttons, playerBox);

        this.getChildren().add(layout);

        // Build Game Over overlay
        buildGameOverOverlay();
    }

    private void startNewGame() {
        this.deck = new Deck();
        this.player = new Player("Player", deck,false);
        this.dealer = new Player("Dealer", deck, true);

        gameState = GameState.PLAYER_TURN;
    }

    private VBox createPlayerBox(Player p) {
        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);

        Text name = new Text(p.getName());
        name.setFont(standard_font);

        StackPane handPane = createHandPane(p.getHand());

        Text valueText = new Text("Total: " + p.getHand().getDisplayValue());
        valueText.setFont(standard_font);

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
        pane.getChildren().clear();

        for (int i = 0; i < hand.getCards().size(); i++) {
            Text t = new Text(hand.getCards().get(i).toString());
            t.setFont(Font.font(20));
            t.setTranslateX(i * 60);
            pane.getChildren().add(t);
        }

        String total = "Total: " + hand.getDisplayValue();

        if (pane == playerHandPane) {
            playerValueText.setText(total);
            playerValueText.setFont(standard_font);
        } else if (pane == dealerHandPane) {
            dealerValueText.setText(total);
            dealerValueText.setFont(standard_font);
        }
    }


    // ============================
    // DEALER TURN + GAME OVER
    // ============================

    private String checkWinner() {
        int playerTotal = player.getHand().getBestTotal();
        int dealerTotal = dealer.getHand().getBestTotal();

        if (playerTotal > 21) return "Spieler über 21! Der Dealer gewinnt!";
        if (dealerTotal > 21) return "Dealer über 21! "+player.getName()+" gewinnt!";
        if (playerTotal > dealerTotal) return "Spieler gewinnt!";
        if (dealerTotal > playerTotal) return "Dealer gewinnt!";
        return "Niemand gewinnt: Unentschieden!";
    }


    // ============================
    // GAME OVER OVERLAY
    // ============================

    private void buildGameOverOverlay() {
        gameOverOverlay = new StackPane();
        gameOverOverlay.setVisible(false);
        gameOverOverlay.setPickOnBounds(true);

        Rectangle dim = new Rectangle();
        dim.widthProperty().bind(widthProperty());
        dim.heightProperty().bind(heightProperty());
        dim.setFill(Color.rgb(0, 0, 0, 0.6));

        VBox content = new VBox(20);
        content.setAlignment(Pos.CENTER);

        gameOverMessage = new Text("Game Over");
        gameOverMessage.setFill(Color.WHITE);
        gameOverMessage.setFont(Font.font("Comic Sans MS", 34));

        restartButton = new Button("Restart");
        restartButton.getStyleClass().add("standard-button");
        restartButton.setOnAction(e -> restartGame());

        content.getChildren().addAll(gameOverMessage, restartButton);

        gameOverOverlay.getChildren().addAll(dim, content);

        this.getChildren().add(gameOverOverlay);
    }

    private void showGameOver(String message) {
        gameOverMessage.setText(message);
        gameOverOverlay.setVisible(true);
    }

    private void runDealerTurn() {
        actionTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {

                    if (dealer.getHand().getBestTotal() < 17 && !player.getHand().isBust() && !player.getHand().is21()) {

                        dealer.getHand().addCard(deck.pickRandomCard());
                        updateHandPane(dealerHandPane, dealer.getHand());

                        runDealerTurn();
                    }
                    else {
                        updateHandPane(dealerHandPane, dealer.getHand());

                        gameState = GameState.GAME_OVER;

                        showGameOver(checkWinner());
                    }
                });
            }
        }, 800);
    }



    private void restartGame() {
        // Reset everything
        startNewGame();

        updateHandPane(playerHandPane, player.getHand());
        updateHandPane(dealerHandPane, dealer.getHand());

        playerValueText.setText("Total: " + player.getHand().getDisplayValue());
        dealerValueText.setText("Total: " + dealer.getHand().getDisplayValue());

        gameOverOverlay.setVisible(false);
    }
}
