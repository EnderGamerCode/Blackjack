package de.waft.ui.components;

import javafx.scene.control.Button;

public class ActionButton extends Button {

    public ActionButton(String s, Runnable clickEvent) {
        super(s);
        getStyleClass().add("action-button");
        setOnAction((_) ->  clickEvent.run());
    }


}
