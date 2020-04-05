package com.realsnake.achom.game.group;

import com.realsnake.achom.game.helpers.CustomColor;
import com.realsnake.achom.game.tools.Heading;

import static com.realsnake.achom.game.tools.Heading.LEFT;
import static com.realsnake.achom.game.tools.Heading.RIGHT;

public class Snake extends ObjectBase {

    private CustomColor color;
    private CustomColor headColor = CustomColor.HEAD;
    private int[] xPos;
    private int[] yPos;
    private int[] xPosTmp;
    private int[] yPosTmp;
    private int length;
    private Heading heading;

    public Snake() {
        super();
    }

    public Snake(int maxLength) {
        super();
        xPos = new int[maxLength];
        yPos = new int[maxLength];
        xPosTmp = new int[maxLength];
        yPosTmp = new int[maxLength];
    }

    public Snake(CustomColor color, CustomColor headColor, int[] xPos, int[] yPos, int length) {
        super();
        this.color = color;
        this.headColor = headColor;
        this.xPos = xPos;
        this.yPos = yPos;
        this.length = length;
    }

    public void startSnakeMovementFromStart() {
        if (getHeading() == Heading.START) {
            changeDirection();
        }
    }

    public CustomColor getColor() {
        return color;
    }

    public void setColor(CustomColor color) {
        this.color = color;
    }

    public int[] getxPos() {
        return xPos;
    }

    public void setxPos(int[] xPos) {
        this.xPos = xPos;
    }

    public int[] getyPos() {
        return yPos;
    }

    public void setyPos(int[] yPos) {
        this.yPos = yPos;
    }

    public int getSize() {
        return length;
    }

    public void setSize(int length) {
        this.length = length;
    }

    public CustomColor getHeadColor() {
        return headColor;
    }

    public void setHeadColor(CustomColor headColor) {
        this.headColor = headColor;
    }

    public int[] getxPosTmp() {
        return xPosTmp;
    }

    public void setxPosTmp(int[] xPosTmp) {
        this.xPosTmp = xPosTmp;
    }

    public int[] getyPosTmp() {
        return yPosTmp;
    }

    public void setyPosTmp(int[] yPosTmp) {
        this.yPosTmp = yPosTmp;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public Heading getHeading() {
        return heading;
    }

    public void setHeading(Heading heading) {
        this.heading = heading;
    }

    @Override
    public void setColorByNumber(int number) {
        switch (number) {
            case 4:
                this.color = CustomColor.GREEN;
                break;
            case 5:
                this.color = CustomColor.BLUE;
                break;
            case 6:
                this.color = CustomColor.RED;
                break;
            case 7:
                this.color = CustomColor.GOLD;
                break;
            case 8:
                this.color = CustomColor.YELLOW;
                break;
            case 9:
                this.color = CustomColor.PURPLE;
                break;
            case 10:
                this.color = CustomColor.DARKSLATEGREY;
                break;
            case 11:
                this.color = CustomColor.MAROON;
                break;
            case 12:
                this.color = CustomColor.ORANGE_RED;
            case 13:
                this.color = CustomColor.LIMEG_REEN;
            case 14:
                this.color = CustomColor.DEEP_SKY_BLUE;
            case 15:
                this.color = CustomColor.MEDIUM_SLATE_BLUE;
            default:
                this.color = CustomColor.GOLD;
                break;
        }
        CustomColor.HEAD.setBlue(color.getHeadBlue());
        CustomColor.HEAD.setGreen(color.getHeadGreen());
        CustomColor.HEAD.setRed(color.getHeadRed());
    }

    public void move() {
        //save in case of death
        xPosTmp = xPos.clone();
        yPosTmp = yPos.clone();
        // Move the body
        for (int i = length; i > 0; i--) {
            //update starts from tail to head (head not moved yet)
            xPos[i] = xPos[i - 1];
            yPos[i] = yPos[i - 1];

        }
        //move head in heading
        switch (heading) {
            case UP:
                yPos[0]--;
                break;
            case RIGHT:
                xPos[0]++;
                break;
            case DOWN:
                yPos[0]++;
                break;
            case LEFT:
                xPos[0]--;
                break;
            case START:
                break;
        }
    }

    public boolean isLegalMove(Heading checking) {
        if (getLength() == 1) {
            return true;
        }
        switch (heading) {
            case DOWN:
                if (checking != Heading.UP) {
                    return true;
                }
                break;
            case UP:
                if (checking != Heading.DOWN) {
                    return true;
                }
                break;
            case LEFT:
                if (checking != Heading.RIGHT) {
                    return true;
                }
                break;
            case RIGHT:
                if (checking != Heading.LEFT) {
                    return true;
                }
                break;
        }
        return false;
    }

    public void changeDirection() {
        switch (heading) {
            case DOWN:
            case START:
                heading = LEFT;
                return;
            case LEFT:
                heading = Heading.UP;
                return;
            case UP:
                heading = RIGHT;
                return;
            case RIGHT:
                heading = Heading.DOWN;
        }
    }
}
