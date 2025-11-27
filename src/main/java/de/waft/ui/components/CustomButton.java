package de.waft.ui.components;

import javafx.scene.control.Button;

public class CustomButton extends Button {

    public CustomButton(String s,String style, Runnable clickEvent) {
        super(s);
        getStyleClass().add(style);
        setOnAction((_) ->  clickEvent.run());
    }


}
