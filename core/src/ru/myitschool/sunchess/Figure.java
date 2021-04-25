package ru.myitschool.sunchess;

public class Figure {
    static float side1y = ChessGame.paddingBottom-ChessGame.size, side1x = -ChessGame.size/2;
    static float side2y = ChessGame.paddingBottom + ChessGame.SCR_WIDTH + ChessGame.size/6, side2x = -ChessGame.size/2;
    int type;
    int color;
    int side;
    int boardX, boardY;
    float x, y; // координаты на экране
    public static final int FREE = 0, MOVE = 1, KILLED = 2;
    int condition;

    public Figure(int type, int color, int boardX, int boardY, int side) {
        this.type = type;
        this.color = color;
        this.boardX = boardX;
        this.boardY = boardY;
        this.side = side;
        boardToScreen();
    }

    void boardToScreen(){
        x = boardX*ChessGame.size;
        y = boardY*ChessGame.size+ChessGame.paddingBottom;
    }

    void screenToBoard(){
        boardX = (int)(x/ChessGame.size);
        boardY = (int)((y-ChessGame.paddingBottom)/ChessGame.size);
    }

    boolean isHit(float tx, float ty){
        return (int) tx == boardX && (int) ty == boardY;
    }

    void put(float touchX, float touchY, Cell[][] board) {
        int tx = (int)(touchX / ChessGame.size);
        int ty = (int)((touchY - ChessGame.paddingBottom) / ChessGame.size);
        condition = FREE;

        if(tx<0 || tx>7 || ty<0 || ty>7){
            boardToScreen();
            return;
        }
        if(board[tx][ty].figure != null) {
            if (board[tx][ty].figure.color == color || board[tx][ty].figure.type == ChessGame.KING) {
                boardToScreen();
                return;
            } else {
                board[tx][ty].figure.kill();
            }
        }
        board[boardX][boardY].figure = null;
        board[tx][ty].figure = this;
        boardX = tx;
        boardY = ty;
        boardToScreen();
    }

    void move(float tx, float ty) {
        x = tx-ChessGame.size/2;
        y = ty-ChessGame.size/2;
    }

    void kill(){
        condition = KILLED;
        if(side == ChessGame.OTHER_SIDE) {
            side1x += ChessGame.size / 2;
            x = side1x;
            y = side1y;
        } else {
            side2x += ChessGame.size / 2;
            x = side2x;
            y = side2y;
        }
        boardX = -1;
        boardY = -1;
    }
}
