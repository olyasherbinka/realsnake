package com.realsnake.achom.game.group;

import com.realsnake.achom.game.helpers.CustomColor;

public abstract class ObjectBase {
    private CustomColor color;

    ObjectBase() {

    }

    public CustomColor getColor() {
        return color;
    }

    public void setColor(CustomColor color) {
        this.color = color;
    }

    public void setColorByNumber(int number) {
        switch (number) {
            case 4:
                this.color = CustomColor.GREEN;
                return;
            case 5:
                this.color = CustomColor.BLUE;
                return;
            case 6:
                this.color = CustomColor.RED;
                return;
            case 7:
                this.color = CustomColor.GOLD;
                return;
            case 8:
                this.color = CustomColor.YELLOW;
                return;
            case 9:
                this.color = CustomColor.PURPLE;
                return;
            case 10:
                this.color = CustomColor.DARKSLATEGREY;
                return;
            case 11:
                this.color = CustomColor.MAROON;
                return;
            case 12:
                this.color = CustomColor.ORANGE_RED;
                return;
            case 13:
                this.color = CustomColor.LIMEG_REEN;
                return;
            case 14:
                this.color = CustomColor.DEEP_SKY_BLUE;
                return;
            case 15:
                this.color = CustomColor.MEDIUM_SLATE_BLUE;
                return;
            default:
                this.color = CustomColor.GOLD;
        }
    }

}
