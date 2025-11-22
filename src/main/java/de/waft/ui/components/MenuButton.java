package de.waft.ui.components;

import javafx.scene.control.Button;

public class MenuButton extends Button {

    public MenuButton(String s, Runnable clickEvent) {
        super(s);
        getStyleClass().add("menu-button");
        setOnAction((_) ->  clickEvent.run());
        hoverProperty().addListener((_, _, isHovered) -> {
            if (isHovered) {
                setText("> " + getText() + " <");
            } else {
                setText(s);
            }
        });
    }
}
