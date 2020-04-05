package com.realsnake.achom.game.group;

import com.realsnake.achom.game.helpers.CustomColor;
import com.realsnake.achom.game.helpers.Mode;
import com.realsnake.achom.game.tools.Heading;

import java.util.Random;

public class Gamefield {
    private final int NUM_BLOCKS_WIDE = 20;
    private Background background;
    private Portal portal;
    private Snake snake;
    private EatThing eatThing;
    private Mode mode;
    private int sizeX;
    private int sizeY;
    private int blockSize;
    private int numBlocksHigh;

    public Gamefield(int sizeX, int sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.blockSize = sizeX / NUM_BLOCKS_WIDE;
        this.numBlocksHigh = sizeY / blockSize;
        this.portal = new Portal();
        this.eatThing = new EatThing();
        this.background = new Background();
        this.snake = new Snake(NUM_BLOCKS_WIDE * numBlocksHigh + 1);
    }

    public void setModeByNumber(int number) {
        switch (number) {
            case 0:
                this.mode = Mode.NORMAL;
                return;
            case 1:
                this.mode = Mode.NO_BORDER;
                return;
            case 2:
                this.mode = Mode.PORTALS;
                return;
            case 3:
                this.mode = Mode.COMBINED;
                return;
            default:
                this.mode = Mode.NORMAL;
        }
    }

    /**
     * checks if portals are ready to reset (used and free)
     * calls spawnPortals
     */
    public void resetPortals() {
        if (this.getPortal().isUsed() && portalFree()) {
            this.getPortal().setUsed(false);
            this.spawnPortals();
        }
    }


    private boolean portalFree() {
        for (int i : this.getSnake().getxPos()) {
            if (i == this.getPortal().getxPos1() || i == this.getPortal().getxPos2()) {
                return false;
            }
        }
        for (int i : this.getSnake().getyPos()) {
            if (i == this.getPortal().getyPos1() || i == this.getPortal().getyPos2()) {
                return false;
            }
        }
        return true;
    }

    public void newGame() {
        this.getSnake().setLength(1);
        this.getSnake().setHeading(Heading.START);
        this.getSnake().getxPos()[0] = this.getNUM_BLOCKS_WIDE() / 2;
        this.getSnake().getyPos()[0] = this.getNumBlocksHigh() / 2;
        this.spawnFood();
        if (this.getMode() == Mode.PORTALS || this.getMode() == Mode.COMBINED) {
            this.checkSameColor(0);
            this.spawnPortals();
        }
    }

    public boolean checkHitBorder() {
        if (this.getMode() == Mode.NO_BORDER || this.getMode() == Mode.COMBINED) {
        } else {
            // Hit the screen borders
            if (this.getSnake().getxPos()[0] <= -1) return true;
            if (this.getSnake().getxPos()[0] >= this.getNUM_BLOCKS_WIDE()) return true;
            if (this.getSnake().getyPos()[0] <= -1) return true;
            return this.getSnake().getyPos()[0] >= this.getNumBlocksHigh();
        }
        return false;
    }

    public boolean checkEatenItself() {
        for (int i = this.getSnake().getLength() - 1; i > 0; i--) {
            if ((i > 2) && (this.getSnake().getxPos()[0] == this.getSnake().getxPos()[i]) && (this.getSnake().getyPos()[0] == this.getSnake().getyPos()[i])) {
                return true;
            }
        }
        return false;
    }

    public void spawnPortals() {
        checkSameColor(0);
        Random random = new Random();
        this.getPortal().setxPos1(random.nextInt(this.getNUM_BLOCKS_WIDE() - 2) + 1);
        this.getPortal().setyPos1(random.nextInt(this.getNumBlocksHigh() - 2) + 1);
        this.getPortal().setxPos2(random.nextInt(this.getNUM_BLOCKS_WIDE() - 2) + 1);
        this.getPortal().setyPos2(random.nextInt(this.getNumBlocksHigh() - 2) + 1);
        if (!this.getPortal().checkLegalPosition()) {
            spawnPortals();
        }
        for (int i = 0; i < this.getSnake().getxPos().length; ++i) {
            if (this.getPortal().getxPos1() == this.getSnake().getxPos()[i] && this.getPortal().getyPos1() == this.getSnake().getyPos()[i] && this.getPortal().getxPos2() == this.getSnake().getxPos()[i] && this.getPortal().getyPos2() == this.getSnake().getyPos()[i]) {
                spawnPortals();
            }
        }
    }

    public void checkSameColor(int newColorEntry) {
        this.getPortal().setColor1(CustomColor.BLUE);
        this.getPortal().setColor2(CustomColor.ORANGE_RED);
//PORTALS
        if (this.getBackground().getColor() == this.getPortal().getColor1()) {
            if (this.getPortal().getColor1() == CustomColor.BLUE) {
                this.getPortal().setColor1(CustomColor.LIMEG_REEN);
            } else {
                this.getPortal().setColor1(CustomColor.BLUE);
            }
        }
        if (this.getBackground().getColor() == this.getPortal().getColor2()) {
            if (this.getPortal().getColor2() == CustomColor.ORANGE_RED) {
                this.getPortal().setColor2(CustomColor.DARKSLATEGREY);
            } else {
                this.getPortal().setColor2(CustomColor.ORANGE_RED);
            }
        }
//ENTRY
        if (this.getBackground().getColor() == this.getSnake().getColor() || this.getBackground().getColor() == this.getEatThing().getColor() || this.getSnake().getColor() == this.getEatThing().getColor()) {
            switch (newColorEntry) {
                case -1:
                    if (this.getBackground().getColor() == this.getSnake().getColor()) {
                        this.getSnake().setColorByNumber(this.getSnake().getColor().getNumber() + 1);
                    }
                    if (this.getBackground().getColor() == this.getEatThing().getColor()) {
                        this.getEatThing().setColorByNumber(this.getEatThing().getColor().getNumber() + 1);
                    }
                    if (this.getSnake().getColor() == this.getEatThing().getColor()) {
                        this.getEatThing().setColorByNumber(this.getEatThing().getColor().getNumber() + 1);
                    }
                    break;
                case 0:
                    //PORTALS
                    break;
                case 1:
                    this.getBackground().setColorByNumber(this.getBackground().getColor().getNumber() + 1);
                    break;
                case 2:
                    this.getSnake().setColorByNumber(this.getSnake().getColor().getNumber() + 1);
                    break;
                case 3:
                    this.getEatThing().setColorByNumber(this.getEatThing().getColor().getNumber() + 1);
                    break;
            }
        }
    }

    private void goTroughPortal(int portalNumber) {
        this.portal.setUsed(true);
        if (portalNumber == 1) {
            this.getSnake().getxPos()[0] = this.getPortal().getxPos2();
            this.getSnake().getyPos()[0] = this.getPortal().getyPos2();
        } else if (portalNumber == 2) {
            this.getSnake().getxPos()[0] = this.getPortal().getxPos1();
            this.getSnake().getyPos()[0] = this.getPortal().getyPos1();
        }
    }

public void spawnFood() {
        Random random = new Random();
        this.getEatThing().setxPos(random.nextInt(this.getNUM_BLOCKS_WIDE() - 1) + 1);
        this.getEatThing().setyPos(random.nextInt(this.getNumBlocksHigh() - 1) + 1);
        //check if legal
        if (this.getEatThing().getxPos() == this.getPortal().getxPos1() || this.getEatThing().getxPos() == this.getPortal().getxPos2() || this.getEatThing().getyPos() == this.getPortal().getyPos1() || this.getEatThing().getyPos() == this.getPortal().getyPos2()) {
            spawnFood();
        }
        for (int i = 0; i < this.getSnake().getxPos().length; ++i) {
            if (this.getEatThing().getxPos() == this.getSnake().getxPos()[i] && this.getEatThing().getyPos() == this.getSnake().getyPos()[i]) {
                spawnFood();
            }
        }
    }

    /**
     * Checks if Snake collides with border and if in no border modes updates
     */
    public void checkBorder() {
        if (this.getMode() == Mode.NO_BORDER || this.getMode() == Mode.COMBINED) {
            if (this.getSnake().getxPos()[0] < 0) {
                this.getSnake().getxPos()[0] = this.getNUM_BLOCKS_WIDE() - 1;
            } else if (this.getSnake().getxPos()[0] > this.getNUM_BLOCKS_WIDE() - 1) {
                this.getSnake().getxPos()[0] = 0;
            }
            if (this.getSnake().getyPos()[0] < 0) {
                this.getSnake().getyPos()[0] = this.getNumBlocksHigh() - 1;
            } else if (this.getSnake().getyPos()[0] > this.getNumBlocksHigh() - 1) {
                this.getSnake().getyPos()[0] = 0;
            }
        }
    }

    public boolean checkPortals() {
        if (this.getMode() == Mode.PORTALS || this.getMode() == Mode.COMBINED) {
            if (this.getSnake().getxPos()[0] == this.getPortal().getxPos1() && this.getSnake().getyPos()[0] == this.getPortal().getyPos1()) {
                //Portal1
                this.goTroughPortal(1);
                return true;
            } else if (this.getSnake().getxPos()[0] == this.getPortal().getxPos2() && this.getSnake().getyPos()[0] == this.getPortal().getyPos2()) {
                //Portal2
                this.goTroughPortal(2);
                return true;
            }
        }
        return false;
    }

    public int getSizeX() {
        return sizeX;
    }

    public void setSizeX(int sizeX) {
        this.sizeX = sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public void setSizeY(int sizeY) {
        this.sizeY = sizeY;
    }

    public int getBlockSize() {
        return blockSize;
    }

    public void setBlockSize(int blockSize) {
        this.blockSize = blockSize;
    }

    private int getNumBlocksHigh() {
        return numBlocksHigh;
    }

    public void setNumBlocksHigh(int numBlocksHigh) {
        this.numBlocksHigh = numBlocksHigh;
    }

    private int getNUM_BLOCKS_WIDE() {
        return NUM_BLOCKS_WIDE;
    }

    public Background getBackground() {
        return background;
    }

    public void setBackground(Background background) {
        this.background = background;
    }

    public Portal getPortal() {
        return portal;
    }

    public void setPortal(Portal portal) {
        this.portal = portal;
    }

    public Snake getSnake() {
        return snake;
    }

    public void setSnake(Snake snake) {
        this.snake = snake;
    }

    public EatThing getEatThing() {
        return eatThing;
    }

    public void setEatThing(EatThing eatThing) {
        this.eatThing = eatThing;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

}
