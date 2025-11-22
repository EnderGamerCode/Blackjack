package de.waft;

import de.waft.logic.GameHandler;
import javafx.application.Application;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Blackjack extends Application {

    void main() {
        launch();
    }


    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Schwarzer Jakob");
        primaryStage.setResizable(true);
        primaryStage.setMaximized(true);
        primaryStage.setOnCloseRequest(_ -> System.exit(0));

        IO.println(Font.getFamilies());

        new GameHandler(primaryStage);


    }
}
