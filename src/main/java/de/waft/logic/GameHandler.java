package de.waft.logic;

import de.waft.ui.ExplainationPane;
import de.waft.ui.GamePane;
import de.waft.ui.TitleScreenPane;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Objects;

public class GameHandler {

    GamePane gamePane;
    TitleScreenPane titleScreenPane;
    ExplainationPane explainationPane;

    public Font standard_font = Font.font("Comic Sans MS", 20);
    public Font title_font = Font.font("Georgia", 150);

    Stage stage;

    public GameHandler(Stage stage) {
        this.stage = stage;

        titleScreenPane = new TitleScreenPane(this);

        stage.setScene(createScene(titleScreenPane));
        stage.show();
    }


    public void startGame(){
        gamePane = new GamePane(this);
        stage.setScene(createScene(gamePane));
    }

    public void showTitleScreen() {
        titleScreenPane = new TitleScreenPane(this);
        stage.setScene(createScene(titleScreenPane));
    }

    public void showExplanationScreen() {
        explainationPane = new ExplainationPane(this);
        stage.setScene(createScene(explainationPane));
    }


    Scene createScene(StackPane stackPane){
        Scene scene = new Scene(stackPane);
        //styles
        scene.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/styles.css")).toExternalForm()
        );
        //
        return scene;
    }


}
