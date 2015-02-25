package com.almasb.battleship;

import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Ship extends Parent {
    public int type;
    // TODO: impl
    public boolean vertical = true;

    private int health;

    public Ship(int type) {
        this.type = type;
        health = type;

        VBox vbox = new VBox();
        for (int i = 0; i < type; i++) {
            Rectangle square = new Rectangle(30, 30);
            square.setFill(null);
            square.setStroke(Color.BLACK);
            vbox.getChildren().add(square);
        }

        getChildren().add(vbox);
    }

    public void hit() {
        health--;
    }

    public boolean isAlive() {
        return health > 0;
    }
}