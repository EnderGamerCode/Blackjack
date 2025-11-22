package de.waft.ui;

import de.waft.logic.GameHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class ExplainationPane extends StackPane {

    GameHandler gameHandler;

    public ExplainationPane(GameHandler gameHandler) {
        this.gameHandler = gameHandler;

        renderExplanations();

    }

    private void renderExplanations() {

        Rectangle bg = new Rectangle();
        bg.widthProperty().bind(widthProperty());
        bg.heightProperty().bind(heightProperty());
        bg.setFill(Color.web("#303761"));

        VBox menuBox = new VBox(35);
        menuBox.setAlignment(Pos.TOP_CENTER);

        VBox backBox = new VBox();
        backBox.setAlignment(Pos.TOP_LEFT);
        backBox.setPadding(new Insets(10, 0, 0, 10)); // top, right, bottom, left


        Button backButton = new Button("\uD83D\uDD19");
        backButton.getStyleClass().add("close-button");
        backButton.setOnAction(_ -> gameHandler.showTitleScreen());
        backBox.getChildren().add(backButton);

        VBox explanationPane = new VBox();
        explanationPane.setSpacing(10);
        explanationPane.setAlignment(Pos.TOP_CENTER);
        Text explanationPaneText = new Text("Explainations for this game");
        explanationPane.getChildren().addAll(explanationPaneText);

        menuBox.getChildren().addAll(backBox, explanationPane);

        this.getChildren().addAll(bg, menuBox);
    }


}
