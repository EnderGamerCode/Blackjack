package de.waft.logic;

import de.waft.ui.ExplainationPane;
import de.waft.ui.GamePane;
import de.waft.ui.TitleScreenPane;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.input.KeyCombination;

import java.util.Objects;

public class GameHandler {

    private GamePane gamePane;
    private TitleScreenPane titleScreenPane;
    private ExplainationPane explanationPane;

    public Font standard_font = Font.font("Comic Sans MS", 20);
    public Font title_font = Font.font("Georgia", 150);

    private final StackPane root;

    public GameHandler(Stage stage) {

        root = new StackPane();

        Scene scene = new Scene(root);

        scene.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/styles.css")).toExternalForm()
        );

        stage.setScene(scene);

        // Fullscreen settings
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.setFullScreenExitHint("");
        stage.setFullScreen(true);

        showTitleScreen();
    }

    public void startGame() {
        gamePane = new GamePane(this);
        root.getChildren().setAll(gamePane);
    }

    public void showTitleScreen() {
        titleScreenPane = new TitleScreenPane(this);
        root.getChildren().setAll(titleScreenPane);
    }

    public void showExplanationScreen() {
        explanationPane = new ExplainationPane(this);
        root.getChildren().setAll(explanationPane);
    }
}
