package de.waft;

import de.waft.logic.GameHandler;
import javafx.application.Application;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

public class Blackjack extends Application {

    void main() {
        launch();
    }


    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Schwarzer Jakob");
        primaryStage.setFullScreenExitHint("");
        primaryStage.setResizable(false);
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        primaryStage.setFullScreen(true);

        primaryStage.setOnCloseRequest(_ -> System.exit(0));


        new GameHandler(primaryStage);

        primaryStage.show();

    }
}
