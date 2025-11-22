package de.waft.ui;

import de.waft.logic.GameHandler;
import de.waft.ui.components.MenuButton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class TitleScreenPane extends StackPane {

    public TitleScreenPane(GameHandler gameHandler) {

        // Background gradient
        Rectangle bg = new Rectangle();
        bg.widthProperty().bind(widthProperty());
        bg.heightProperty().bind(heightProperty());
        bg.setFill(new LinearGradient(
                0, 0, 1, 1,
                true,
                CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#14a1e2ff")),
                new Stop(1, Color.web("#9007cfff"))));


        // Main layout
        VBox centerBox = new VBox(10);
        centerBox.setAlignment(Pos.TOP_CENTER);
        centerBox.setPadding(new Insets(100,0,0,0));

        VBox closeBox = new VBox();
        closeBox.setAlignment(Pos.TOP_RIGHT);
        closeBox.setPadding(new Insets(10,10,0,0)); // top, right, bottom, left

        Button closeButton = new Button("❌");
        closeButton.getStyleClass().add("close-button");
        closeButton.setOnAction(_ -> System.exit(0));
        closeButton.setAlignment(Pos.TOP_RIGHT);

        closeBox.getChildren().add(closeButton);


        HBox title = new HBox(10); // spacing = 10px
        title.setAlignment(Pos.CENTER);

        Text aceText = new Text("\uD83C\uDCA1");
        aceText.setFont(gameHandler.title_font);
        aceText.setFill(Color.web("#110f02"));

        Text blackjackTitle = new Text(" BLACKJACK ");
        blackjackTitle.setFont(gameHandler.title_font);
        blackjackTitle.setFill(Color.WHITE);
        blackjackTitle.setLineSpacing(blackjackTitle.getLineSpacing()*1.2);

        DropShadow glow = new DropShadow();
        glow.setColor(Color.web("#ff1315"));
        glow.setRadius(25);
        glow.setSpread(0.3);
        blackjackTitle.setEffect(glow);

        Text kingText = new Text("\uD83C\uDCAE");
        kingText.setFont(gameHandler.title_font);
        kingText.setFill(Color.web("#110f02"));

        title.getChildren().addAll(aceText, blackjackTitle, kingText);


        VBox buttonSelection = new VBox(10);
        buttonSelection.setAlignment(Pos.CENTER);
        buttonSelection.setPadding(new Insets(100,0,0,0));

        // Start button
        MenuButton startButton = new MenuButton("Play", gameHandler::startGame);

        // Explanation Button
        MenuButton explanationButton = new MenuButton("Explanation", gameHandler::showExplanationScreen);

        buttonSelection.getChildren().addAll(startButton, explanationButton);

        centerBox.getChildren().addAll(title, buttonSelection);

        VBox box = new VBox();
        box.getChildren().addAll(closeBox, centerBox);


        this.getChildren().addAll(bg, box);
    }
}
