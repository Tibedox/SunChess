package ru.myitschool.sunchess;

public class Cell {
    int x, y;
    int color;
    Figure figure;

    public Cell(int x, int y, int color, Figure figure) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.figure = figure;
    }
}
