package de.waft.ui;

import de.waft.logic.GameHandler;
import de.waft.logic.GameState;
import de.waft.logic.Player;
import de.waft.logic.Hand;
import de.waft.core.Deck;

import de.waft.ui.components.ActionButton;
import de.waft.ui.components.CustomButton;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class GamePane extends StackPane {


    private final GameHandler gameHandler;
    private GameState gameState = GameState.START;

    private Deck deck;
    private Player player;
    private Player dealer;

    private StackPane playerHandPane;
    private StackPane dealerHandPane;

    private Text playerValueText;
    private Text dealerValueText;

    private StackPane gameOverOverlay;
    private StackPane betOverlay;

    private Text gameOverMessage;

    private final Timer actionTimer = new Timer();

    private HBox buttons = new HBox(20);

    private ActionButton doubleButton;
    private ActionButton splitButton;

    VBox dealerBox;
    VBox playerBox;

    Font standard_font;

    public GamePane(GameHandler gameHandler) {

        this.gameHandler = gameHandler;
        standard_font = gameHandler.standard_font;



        //background
        StackPane bg = new StackPane();
        bg.prefWidthProperty().bind(this.widthProperty());
        bg.prefHeightProperty().bind(this.heightProperty());

        Image image = new Image(
                Objects.requireNonNull(getClass().getResourceAsStream("/images/Spieltisch.png"))
        );

        bg.setBackground(new Background(
                new BackgroundImage(
                        image,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.CENTER,
                        new BackgroundSize(100, 100, true, true, false, true)
                )
        ));

        startNewGame();
        buildBetOverlay();

        // Build layout sections
        dealerBox = createPlayerBox(dealer);
        playerBox = createPlayerBox(player);

        buttons.setAlignment(Pos.CENTER);

        // Vertical alignment
        VBox layout = new VBox(45);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(dealerBox, playerBox ,buttons );

        this.getChildren().addAll(bg, layout);

        showBetScreen();

        // Build Game Over overlay
        buildGameOverOverlay();
    }

    private void showBetScreen() {
        buttons.getChildren().clear();
        playerBox.setVisible(false);
        dealerBox.setVisible(false);
        betOverlay.setVisible(true);
        betOverlay.toFront();
    }

    private void buildBetOverlay() {
        betOverlay = new StackPane();
        betOverlay.setVisible(true);
        betOverlay.setPickOnBounds(true);

        VBox box = new VBox(20);
        box.setAlignment(Pos.CENTER);

        HBox centerBox = new HBox(10);
        centerBox.setAlignment(Pos.CENTER);

        int[] totalBet = { player.getAccount().getAmount() / 5 };


        //adjust text
        Text betTotal = new Text(totalBet[0] + "$");

        CustomButton addButton = new CustomButton("+25", "adjustAdd-button", () -> {
            if(totalBet[0] + 25 <= player.getAccount().getAmount()) {
                totalBet[0] += 25;
                betTotal.setText(totalBet[0] + "$");
            }
        });

        CustomButton subtractButton = new CustomButton("-25","adjustSubtract-button", () -> {
            if(totalBet[0] - 25 >= 0) {
                totalBet[0] -= 25;
                betTotal.setText(totalBet[0] + "$");
            }
        });


        centerBox.getChildren().addAll(addButton, betTotal, subtractButton);

        CustomButton doneButton = new CustomButton("Done","action-button", () -> {
            betOverlay.setVisible(false);
            playerBox.setVisible(true);
            dealerBox.setVisible(true);
            showActionButtons();
        });

        box.getChildren().addAll(centerBox, doneButton);

        betOverlay.getChildren().add(box);

        this.getChildren().add(betOverlay);
        betOverlay.toFront();
    }




    private void showActionButtons() {
        buttons.getChildren().clear();

        //show Cards / Titles
        playerBox.setVisible(true);
        dealerBox.setVisible(true);

        //Create Action Buttons
        ActionButton hitButton = new ActionButton("Hit", () -> {
            if (gameState != GameState.PLAYER_TURN) return;

            if (!player.getHand().isBust()) {
                player.getHand().addCard(deck.pickRandomCard());
                updateHandUI(playerHandPane, player.getHand());
            }

            if (player.getHand().isBust()) {
                gameState = GameState.DEALER_TURN;
                runDealerTurn(0);
            }

            buttons.getChildren().remove(doubleButton);
            buttons.getChildren().remove(splitButton);
        });

        ActionButton standButton = new ActionButton("Stand", () -> {
            if (gameState != GameState.PLAYER_TURN) return;

            runDealerTurn(800);
        });

        doubleButton = new ActionButton("Double", () -> {
            if (gameState != GameState.PLAYER_TURN) return;
            if (player.getHand().getCards().size() != 2) return;

            // Double
            player.getHand().addCard(deck.pickRandomCard());
            updateHandUI(playerHandPane, player.getHand());

            // Bust?
            if (player.getHand().isBust()) {
                runDealerTurn(0);
                return;
            }

            // Stand
            runDealerTurn(800);
        });

        splitButton = new ActionButton("Split", () -> {
            if (gameState != GameState.PLAYER_TURN) return;
        });

        buttons.getChildren().addAll(hitButton, standButton, doubleButton, splitButton);
    }

    private void startNewGame() {
        this.deck = new Deck();
        this.player = new Player("Player", deck,false);
        this.dealer = new Player("Dealer", deck, true);

        gameState = GameState.PLAYER_TURN;

        // boxes will be hidden by the constructor's showBetScreen()
    }



    //player ui
    private VBox createPlayerBox(Player p) {
        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);
        box.setMaxWidth(300);
        box.setStyle("-fx-background-color: rgba(255,255,255,0.25);" + "-fx-background-radius: 10;" + "-fx-padding: 20;" + "-fx-border-color: black;" + "-fx-border-radius: 10;" + "-fx-border-width: 4;");

        Text name = new Text(p.getName());
        name.setFont(standard_font);

        StackPane handPane = new StackPane();
        handPane.setPrefHeight(120);
        handPane.setMaxWidth(Region.USE_COMPUTED_SIZE);
        updateHandUI(handPane, player.getHand());

        Text valueText = new Text("Total: " + p.getHand().getDisplayValue());
        valueText.setFont(standard_font);

        if (!p.getName().equals("Dealer")) {
            playerHandPane = handPane;
            playerValueText = valueText;
        } else {
            dealerHandPane = handPane;
            dealerValueText = valueText;
        }

        box.getChildren().addAll(name, handPane, valueText);
        return box;
    }

    //update Cards from Hands in UI
    private void updateHandUI(StackPane pane, Hand hand) {
        pane.getChildren().clear();

        HBox cards = new HBox(10);
        cards.setAlignment(Pos.CENTER);

        for (int i = 0; i < hand.getCards().size(); i++) {
            Text t = new Text(hand.getCards().get(i).toString());
            t.setFont(Font.font(20));
            cards.getChildren().add(t);
        }

        pane.getChildren().add(cards);

        String total = "Total: " + hand.getDisplayValue();

        if (pane == playerHandPane) {
            playerValueText.setText(total);
        } else if (pane == dealerHandPane) {
            dealerValueText.setText(total);
        }
    }


    //winning message
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
        if (!buttons.getChildren().contains(doubleButton)) {
            buttons.getChildren().add(doubleButton);
        } if (!buttons.getChildren().contains(splitButton)) {
            buttons.getChildren().add(splitButton);
        }

        startNewGame();

        showBetScreen();

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


    //game over overlay
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

        ActionButton backToTitleScreenButton = new ActionButton("Main Menu", gameHandler::showTitleScreen);

        ActionButton restartButton = new ActionButton("Restart", this::restartGame);

        content.getChildren().addAll(gameOverMessage, backToTitleScreenButton, restartButton);

        gameOverOverlay.getChildren().addAll(dim, content);

        this.getChildren().add(gameOverOverlay);
    }

}
