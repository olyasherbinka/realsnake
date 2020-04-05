
package com.realsnake.achom.game.helpers;

public enum CustomColor {

    HEAD("HEAD", 0, 255, 255, 255),
     GREEN("GREEN", 4, 59, 250, 86),
    BLUE("BLUE", 5,  255, 255, 0),
    RED("RED", 6, 255, 6, 6),
    GOLD("GOLD", 7, 255, 215, 0),
    YELLOW("YELLOW", 8, 255, 255, 0),
    PURPLE("PURPLE", 9, 255, 0, 255),
    DARKSLATEGREY("DARKSLATEGREY", 10, 47, 79, 79),
    MAROON("MAROON", 11, 128, 0, 0),
    ORANGE_RED("ORANGE", 12, 255, 140, 0),
    LIMEG_REEN("LIME_GREEN", 13, 50, 205, 50),
    DEEP_SKY_BLUE("MAROON", 14, 0, 191, 255),
    MEDIUM_SLATE_BLUE("MEDIUM_SLATE_BLUE", 15, 123, 104, 238);

    private String name;
    private int number;
    private int alpha;
    private int red;
    private int green;
    private int blue;

    CustomColor(String name, int number, int red, int green, int blue) {
        this.name = name;
        this.number = number;
        this.alpha = 255;
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getAlpha() {
        return alpha;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    public int getRed() {
        return red;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public int getHeadRed() {
        return red > 225 ? red - 30 : red + 30;
    }

    public int getGreen() {
        return green;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public int getHeadGreen() {
        return green > 225 ? green - 30 : green + 30;
    }

    public int getBlue() {
        return blue;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }

    public int getHeadBlue() {
        return blue > 225 ? blue - 30 : blue + 30;
    }


}
