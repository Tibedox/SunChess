package ru.myitschool.sunchess;

import com.google.gson.annotations.SerializedName;

public class ChessData {
    @SerializedName("id")
    int id;
    @SerializedName("gamecode")
    int gamecode;
    @SerializedName("color")
    int color;
    @SerializedName("turn")
    int turn;
    @SerializedName("state")
    int state;
    @SerializedName("gamedate")
    String gamedate;
    @SerializedName("gameturn")
    String gameturn;
    @SerializedName("name1")
    String name1;
    @SerializedName("name2")
    String name2;
    @SerializedName("wins1")
    int wins1;
    @SerializedName("wins2")
    int wins2;
}
