package com.almasb.battleship;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Board extends Parent {
    private VBox rows = new VBox();
    private boolean enemy = false;
    public int ships = 5;

    public Board(boolean enemy, EventHandler<? super MouseEvent> handler) {
        this.enemy = enemy;
        for (int y = 0; y < 10; y++) {
            HBox row = new HBox();
            for (int x = 0; x < 10; x++) {
                Cell c = new Cell(x, y);
                c.setOnMouseClicked(handler);
                row.getChildren().add(c);
            }

            rows.getChildren().add(row);
        }

        getChildren().add(rows);
    }

    public boolean placeShip(Ship ship, int x, int y) {
        if (canPlaceShip(ship, x, y)) {
            int length = ship.type;

            for (int i = y; i < y + length; i++) {
                Cell cell = getCell(x, i);
                cell.ship = Optional.of(ship);
                if (!enemy) {
                    cell.setFill(Color.WHITE);
                    cell.setStroke(Color.GREEN);
                }
            }

            return true;
        }

        return false;
    }

    private Cell getCell(int x, int y) {
        return (Cell)((HBox)rows.getChildren().get(y)).getChildren().get(x);
    }

    private Cell[] getNeighbors(int x, int y) {
        Point2D[] points = new Point2D[] {
                new Point2D(x - 1, y),
                new Point2D(x + 1, y),
                new Point2D(x, y - 1),
                new Point2D(x, y + 1)
        };

        List<Cell> neighbors = new ArrayList<Cell>();

        for (Point2D p : points) {
            if (isValidPoint(p)) {
                neighbors.add(getCell((int)p.getX(), (int)p.getY()));
            }
        }

        return neighbors.toArray(new Cell[0]);
    }

    private boolean canPlaceShip(Ship ship, int x, int y) {
        int length = ship.type;

        for (int i = y; i < y + length; i++) {
            if (!isValidPoint(x, i))
                return false;

            Cell cell = getCell(x, i);
            if (cell.ship.isPresent())
                return false;

            for (Cell neighbor : getNeighbors(x, i)) {
                if (!isValidPoint(x, i))
                    return false;

                if (neighbor.ship.isPresent())
                    return false;
            }
        }

        return true;
    }

    private boolean isValidPoint(Point2D point) {
        return isValidPoint(point.getX(), point.getY());
    }

    private boolean isValidPoint(double x, double y) {
        return x >= 0 && x < 10 && y >= 0 && y < 10;
    }

    public class Cell extends Rectangle {
        public int x, y;
        public Optional<Ship> ship = Optional.empty();

        public Cell(int x, int y) {
            super(30, 30);
            this.x = x;
            this.y = y;
            setFill(Color.LIGHTGRAY);
            setStroke(Color.BLACK);
        }

        public boolean wasShot() {
            return !getFill().equals(Color.LIGHTGRAY);
        }
    }
}