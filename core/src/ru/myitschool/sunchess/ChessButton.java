package ru.myitschool.sunchess;

public class ChessButton {
    float x, y;
    float width, height;
    int state;

    public ChessButton(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    void press(){
        state = 1;
    }

    void realise(){
        state = 0;
    }

    boolean isHit(float tx, float ty){
        return tx>x && ty>y && tx<x+width && ty<y+height;
    }
}
