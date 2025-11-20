package de.waft.ui;

import de.waft.logic.GameHandler;
import de.waft.logic.GameState;
import de.waft.logic.Player;
import de.waft.logic.Hand;
import de.waft.core.Deck;

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


    private GameHandler gameHandler;
    private GameState gameState = GameState.START;

    private Deck deck;
    private Player player;
    private Player dealer;

    private StackPane playerHandPane;
    private StackPane dealerHandPane;

    private Text playerValueText;
    private Text dealerValueText;

    private StackPane gameOverOverlay;
    private Text gameOverMessage;
    private Button restartButton;
    private Button backToTitleScreenButton;

    private Timer actionTimer = new Timer();

    Font standard_font;

    public GamePane(GameHandler gameHandler) {
        this.gameHandler = gameHandler;
        standard_font = gameHandler.standard_font;

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
                updateHandUI(playerHandPane, player.getHand());
            }

            if (player.getHand().isBust()) {
                gameState = GameState.DEALER_TURN;
                runDealerTurn(0);
            }
        });

        standButton.setOnAction(e -> {
            if (gameState != GameState.PLAYER_TURN) return;

            runDealerTurn(800);
        });

        //vertical alignment
        VBox layout = new VBox(45);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(dealerBox, buttons, playerBox);

        this.getChildren().add(layout);

        // Build Game Over overlay
        buildGameOverOverlay();
    }

    private void startNewGame() {
        //Kontrovers:
        this.deck = new Deck();
        //
        this.player = new Player("Player", deck,false);
        this.dealer = new Player("Dealer", deck, true);

        gameState = GameState.PLAYER_TURN;

        if(player.getHand().is21()) {
            runDealerTurn(400);
        }

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

        updateHandUI(pane, hand);
        return pane;
    }

    private void updateHandUI(StackPane pane, Hand hand) {
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


    //get winning message

    private String checkWinningMessage() {
        int playerTotal = player.getHand().getBestTotal();
        int dealerTotal = dealer.getHand().getBestTotal();

        if(player.getHand().isBlackjack()){
            //blackjack player
            return player.getName()+" gewinnt! BLACKJACK!";
        }else if(dealer.getHand().isBlackjack()){
            //blackjack dealer
            return "Dealer gewinnt! BLACKJACK!";
        }

            if (playerTotal > 21) return "Spieler über 21! Der Dealer gewinnt!";
            if (dealerTotal > 21) return "Dealer über 21! "+player.getName()+" gewinnt!";
            if (playerTotal > dealerTotal) return "Spieler gewinnt!";
            if (dealerTotal > playerTotal) return "Dealer gewinnt!";
            return "Niemand gewinnt: Unentschieden!";

    }

    //Restart Game

    private void restartGame() {
        // Reset everything
        startNewGame();

        updateHandUI(playerHandPane, player.getHand());
        updateHandUI(dealerHandPane, dealer.getHand());

        playerValueText.setText("Total: " + player.getHand().getDisplayValue());
        dealerValueText.setText("Total: " + dealer.getHand().getDisplayValue());

        gameOverOverlay.setVisible(false);
    }

    //Game over

    private void showGameOver(String message) {
        gameState = GameState.GAME_OVER;
        gameOverMessage.setText(message);
        gameOverOverlay.setVisible(true);
    }

    //Dealer Turn

    private void runDealerTurn(int delay) {
        gameState = GameState.DEALER_TURN;
        actionTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {

                    if (dealer.getHand().getBestTotal() < 17 && !player.getHand().isBust() && !player.getHand().isBlackjack()) {

                        dealer.getHand().addCard(deck.pickRandomCard());
                        updateHandUI(dealerHandPane, dealer.getHand());

                        runDealerTurn(800);
                    }
                    else {
                        updateHandUI(dealerHandPane, dealer.getHand());

                        showGameOver(checkWinningMessage());
                    }
                });
            }
        }, delay);
    }


    //build game over overlay (save resources)

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

        backToTitleScreenButton = new Button("Main Menu");
        backToTitleScreenButton.getStyleClass().add("standard-button");
        backToTitleScreenButton.setOnAction(e -> gameHandler.showTitleScreen());

        restartButton = new Button("Restart");
        restartButton.getStyleClass().add("standard-button");
        restartButton.setOnAction(e -> restartGame());

        content.getChildren().addAll(gameOverMessage, backToTitleScreenButton,restartButton);

        gameOverOverlay.getChildren().addAll(dim, content);

        this.getChildren().add(gameOverOverlay);
    }

}
