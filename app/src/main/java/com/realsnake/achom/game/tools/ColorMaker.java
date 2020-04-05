package com.realsnake.achom.game.tools;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.realsnake.achom.game.helpers.CustomColor;

public final class ColorMaker {


    public static void drawCircle(int centerX, int centerY, int radius, CustomColor color, Canvas canvas, Paint paint) {
        paint.setColor(Color.argb(color.getAlpha(), color.getRed(), color.getGreen(), color.getBlue()));
        canvas.drawCircle(centerX, centerY, radius, paint);
    }

    public static void drawRect(int left, int top, int right, int bottom, CustomColor color, Canvas canvas, Paint paint) {
        paint.setColor(Color.argb(color.getAlpha(), color.getRed(), color.getGreen(), color.getBlue()));
        canvas.drawRect(left, top, right, bottom, paint);
    }


    public static void drawText(String text, int x, int y, float size, CustomColor color, Canvas canvas, Paint paint) {
        paint.setColor(Color.argb(color.getAlpha(), color.getRed(), color.getGreen(), color.getBlue()));
        paint.setTextSize(size);
        canvas.drawText(text, x, y, paint);
    }

    public static void drawColor(CustomColor color, Canvas canvas) {
        canvas.drawColor(Color.argb(color.getAlpha(), color.getRed(), color.getGreen(), color.getBlue()));
    }
}
