/*
        Simple Snake Android Application
        Copyright (C) 2020  Anton "PoorSkill" Kesy

        This program is free software: you can redistribute it and/or modify
        it under the terms of the GNU General Public License as published by
        the Free Software Foundation, either version 3 of the License, or
        (at your option) any later version.

        This program is distributed in the hope that it will be useful,
        but WITHOUT ANY WARRANTY; without even the implied warranty of
        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
        GNU General Public License for more details.

        You should have received a copy of the GNU General Public License
        along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.realsnake.achom.game.tools;

import com.realsnake.achom.game.group.Gamefield;

public final class OnTouchUtility {

    public static boolean swipeTopRightBottomLeft(float xSwipe1, float ySwipe1, float xSwipe2, float ySwipe2, double percentage, Gamefield gamefield) {
        return xSwipe1 > gamefield.getSizeX() - gamefield.getSizeX() * percentage && xSwipe2 < gamefield.getSizeX() * percentage && ySwipe1 < gamefield.getSizeY() - gamefield.getSizeY() * percentage && ySwipe2 > gamefield.getSizeY() * percentage && xSwipe1 - xSwipe2 > gamefield.getSizeX() * percentage && ySwipe2 - ySwipe1 > gamefield.getSizeY() * percentage;
    }

    public static boolean swipeBottomLeftTopRight(float xSwipe1, float ySwipe1, float xSwipe2, float ySwipe2, double percentage, Gamefield gamefield) {
        return xSwipe1 < gamefield.getSizeX() - gamefield.getSizeX() * percentage && xSwipe2 > gamefield.getSizeX() * percentage && ySwipe1 > gamefield.getSizeY() - gamefield.getSizeY() * percentage && ySwipe2 < gamefield.getSizeY() * percentage && xSwipe2 - xSwipe1 > gamefield.getSizeX() * percentage && ySwipe1 - ySwipe2 > gamefield.getSizeY() * percentage;
    }

    public static boolean swipeBottomRightTopLeft(float xSwipe1, float ySwipe1, float xSwipe2, float ySwipe2, double percentage, Gamefield gamefield) {
        return xSwipe1 > gamefield.getSizeX() * percentage && xSwipe2 < gamefield.getSizeX() - gamefield.getSizeX() * percentage && ySwipe1 > gamefield.getSizeY() * percentage && ySwipe2 < gamefield.getSizeY() - gamefield.getSizeY() * percentage && xSwipe1 - xSwipe2 > gamefield.getSizeX() * percentage && ySwipe1 - ySwipe2 > gamefield.getSizeY() * percentage;
    }

    public static boolean swipeTopLeftBottomRight(float xSwipe1, float ySwipe1, float xSwipe2, float ySwipe2, double percentage, Gamefield gamefield) {
        return xSwipe1 < gamefield.getSizeX() - gamefield.getSizeX() * percentage && xSwipe2 > gamefield.getSizeX() * percentage && ySwipe1 < gamefield.getSizeY() - gamefield.getSizeY() * percentage && ySwipe2 > gamefield.getSizeY() * percentage && xSwipe2 - xSwipe1 > gamefield.getSizeX() * percentage && ySwipe2 - ySwipe1 > gamefield.getSizeY() * percentage;
    }

    public static boolean click(float xSwipe1, float ySwipe1, float xSwipe2, float ySwipe2, float threshold) {
        return Math.abs(xSwipe1 - xSwipe2) < threshold && Math.abs(ySwipe1 - ySwipe2) < threshold;
    }

    public static Heading SwipeControll(float xSwipe1, float ySwipe1, float xSwipe2, float ySwipe2) {
        if (Math.abs(xSwipe1 - xSwipe2) > Math.abs(ySwipe1 - ySwipe2)) {
            if (xSwipe1 < xSwipe2) {
                return Heading.RIGHT;
            } else if (xSwipe1 > xSwipe2) {
                return Heading.LEFT;
            }
        } else if (ySwipe1 < ySwipe2) {
            return Heading.DOWN;
        } else if (ySwipe1 > ySwipe2) {
            return Heading.UP;
        }
        return null;
    }

}
