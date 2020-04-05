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
package com.realsnake.achom;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.realsnake.achom.game.group.Background;
import com.realsnake.achom.game.group.EatThing;
import com.realsnake.achom.game.group.Gamefield;
import com.realsnake.achom.game.group.ObjectBase;
import com.realsnake.achom.game.group.Snake;
import com.realsnake.achom.game.helpers.CustomColor;
import com.realsnake.achom.game.helpers.Mode;
import com.realsnake.achom.game.tools.ColorMaker;
import com.realsnake.achom.game.tools.Heading;

import static android.content.Context.VIBRATOR_SERVICE;
import static com.realsnake.achom.AchifActivity.setPref;
import static com.realsnake.achom.game.tools.OnTouchUtility.SwipeControll;
import static com.realsnake.achom.game.tools.OnTouchUtility.click;
import static com.realsnake.achom.game.tools.OnTouchUtility.swipeBottomLeftTopRight;
import static com.realsnake.achom.game.tools.OnTouchUtility.swipeBottomRightTopLeft;
import static com.realsnake.achom.game.tools.OnTouchUtility.swipeTopLeftBottomRight;
import static com.realsnake.achom.game.tools.OnTouchUtility.swipeTopRightBottomLeft;


public class GameEngineView extends SurfaceView implements Runnable {
    private int deadClicks;
    private static boolean playSounds;
    private static boolean vibrate;
    protected static boolean clickControls;
    protected static boolean swipeControls;
    protected static boolean hControls;
    protected static boolean showToast, showScore;
    protected static String signatur;
    protected static Gamefield gamefield;
    protected static int highscore;
    protected static long fps = 9;
    protected static boolean showStartInfo;
    protected final Handler handler;
    protected Thread thread = null;
    protected Context context;
    protected boolean blinking, touched, updateNeeded, dead, justDied, startUp;
    protected float xSwipe1;
    protected float ySwipe1;
    protected long nextFrameTime;
    protected int score;
    protected volatile boolean isPlaying;
    protected final SurfaceHolder surfaceHolder;
    protected final Paint paint;
    protected Toast toast;
    protected Bitmap pauseSignBlack;
    protected Bitmap pauseSignWhite;

    Vibrator vibrator;

    protected boolean swipeSettings;
    protected boolean alreadyPoped;

    protected static SoundPool soundPool;
    protected static int eatSound;
    protected static int deadSound;
    protected static int portalSound;


    public GameEngineView(Context context, Point size) {
        super(context);
        alreadyPoped = false;
        gamefield = new Gamefield(size.x, size.y);
        deadClicks = 0;
        pauseSignBlack = BitmapFactory.decodeResource(getResources(), R.drawable.pause);
        int scale = (int) (gamefield.getSizeX() * 0.08);
        pauseSignBlack = Bitmap.createScaledBitmap(pauseSignBlack, scale, scale, false);
        pauseSignWhite = BitmapFactory.decodeResource(getResources(), R.drawable.pause);
        pauseSignWhite = Bitmap.createScaledBitmap(pauseSignWhite, scale, scale, false);
        swipeSettings = false;
        handler = new Handler();
        touched = true;
        updateNeeded = false;
        surfaceHolder = getHolder();
        paint = new Paint();
        startUp = false;
        dataa();
        newGame();
        if (showStartInfo) {
            startUp = true;
        }
        handler.post(() -> {
            if (showToast) {
                Toast.makeText(getContext(), getResources().getString(R.string.highscore) + " " + highscore, Toast.LENGTH_SHORT).show();
            }
        });
        try {
            SoundPlayer(getContext());
            vibrator = (Vibrator) getContext().getSystemService(VIBRATOR_SERVICE);
        } catch (Exception e) {
            //NO SOUND FILES or SDK Error
        }

        Intent intent = new Intent(getContext(), HelloActivity.class);
        getContext().startActivity(intent);
    }

    public void SoundPlayer(Context context) {
        soundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
        deadSound = soundPool.load(context, R.raw.deads, 1);
        portalSound = soundPool.load(context, R.raw.portald, 1);
        eatSound = soundPool.load(context, R.raw.eat, 1);
    }

    public static void setSignatur(String string) {
        signatur = string;
    }

    public static void setControlByNumber(Integer number) {
        clickControls = false;
        swipeControls = false;
        hControls = false;
        switch (number) {
            case 1:
                clickControls = true;
                return;
            case 2:
                hControls = true;
                return;
            default:
                swipeControls = true;
        }
    }




    public static void setFPS(int value) {
        fps = value;
    }

    public static boolean getShowStartInfo() {
        return showStartInfo;
    }

    public static void setShowStartInfo(boolean value) {
        showStartInfo = value;
    }

    public static int getHighscore() {
        return highscore;
    }

    public static void setHighscore(int value) {
        highscore = value;
    }

    public static Gamefield getGamefield() {
        return gamefield;
    }

    protected void dataa() {
        AchifActivity.data();
    }

    @Override
    public void run() {

        while (isPlaying) {
            // Updates by fps count
            if (updater()) {
                update();
                draw();
            }

        }
    }

    public void pause() {
        isPlaying = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            // Error
        }
    }

    public void resume() {
        dataa();
        gamefield.checkSameColor(-1);
        setPref();
        alreadyPoped = false;
        if (gamefield.getMode() == Mode.PORTALS || gamefield.getMode() == Mode.COMBINED) {
            gamefield.spawnPortals();
        }
        isPlaying = true;
        thread = new Thread(this);
        thread.start();
    }

    protected void newGame() {
        dataa();
        gamefield.newGame();
        score = 0;
        nextFrameTime = System.currentTimeMillis();

    }

    protected void eatFood() {
        gamefield.getSnake().setLength(gamefield.getSnake().getLength() + 1);
        vibrate(300);

        if (playSounds) {
            try {
                soundPool.play(eatSound, 1.0f, 1.0f, 1, 0, 1.0f);
            } catch (Exception e) {

            }
        }
        gamefield.spawnFood();
        score++;
        drawtext();
    }

    protected void moveSnake() {
        gamefield.getSnake().move();
        detectFood();
    }



    private void drawtext(){

    }

protected boolean die() {
        if (gamefield.checkEatenItself()) {

        }
        return gamefield.checkHitBorder() || gamefield.checkEatenItself();
    }


    protected void update() {
        //recycleVariables();
        updateNeeded = false;
        if (dead) {
            deadClicks++;
            return;
        }
        moveSnake();
        if (justDied || die()) {
            vibrate(700);
            if (playSounds) {
                try {
                    soundPool.play(deadSound, 1.0f, 1.0f, 1, 0, 1.0f);
                } catch (Exception e) {

                }
            }
            if (score > highscore) {
                if (showToast) {
                    toastTextNoHandler(getResources().getString(R.string.newHighscore) + ": " + score);
                }
                setHighscore(score);
                setPref();
            } else {
                if (showToast) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), getResources().getString(R.string.score) + ": " + score, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            //start again
            touched = false;
            dead = true;
            justDied = false;
            return;
        }
        if (gamefield.getSnake().getLength() > gamefield.getSnake().getxPos().length) {
            toastTextNoHandler(getResources().getString(R.string.win) + " " + score);
        }
        //Check Border
        gamefield.checkBorder();
        //Check Portal
        if (gamefield.checkPortals()) {
            vibrate(100);
            if (playSounds) {
                try {
                    soundPool.play(portalSound, 1.0f, 1.0f, 1, 0, 1.0f);
                } catch (Exception e) {

                }
            }
        }
        detectFood();
        gamefield.resetPortals();


    }





    protected void vibrate(int lengthInMilliseconds) {
        if (vibrate) {
            try {
                if (Build.VERSION.SDK_INT >= 26) {
                    vibrator.vibrate(VibrationEffect.createOneShot(lengthInMilliseconds, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    vibrator.vibrate(lengthInMilliseconds);
                }
            } catch (Exception e) {
                //SDK Error}
            }
        }
    }

    protected void toastTextNoHandler(final String text) {
        if (showToast) {
            handler.post(() -> Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show());
        }
    }

    protected void detectFood() {
        if (gamefield.getSnake().getxPos()[0] == gamefield.getEatThing().getxPos() && gamefield.getSnake().getyPos()[0] == gamefield.getEatThing().getyPos()) {
            eatFood();
        }
    }

    protected boolean updater() {
        if (nextFrameTime <= System.currentTimeMillis()) {
            long MILLIS_PER_SECOND = 1000;
            nextFrameTime = System.currentTimeMillis() + MILLIS_PER_SECOND / fps;
            return true;
        }
        return false;
    }


    @Override
    public boolean onTouchEvent(MotionEvent touchEvent) {
        startUp = false;
        performClick();
        switch (touchEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                xSwipe1 = touchEvent.getX();
                ySwipe1 = touchEvent.getY();
                //Menu
                if (!alreadyPoped && xSwipe1 < gamefield.getSizeX() / 10 + gamefield.getSizeX() / 13 && ySwipe1 < gamefield.getSizeX() / 20 + gamefield.getSizeX() / 13) {
                    alreadyPoped = true;
                    AchifActivity.scoreNum = score;
                    Intent intent = new Intent(getContext(), MenuActivity.class);
                    getContext().startActivity(intent);
                }
                //miss restart
                if (!touched && deadClicks > 5) {
                    deadClicks = 0;
                    touched = true;
                    dead = false;
                    newGame();
                }
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                float xSwipe2 = touchEvent.getX();
                float ySwipe2 = touchEvent.getY();
                if (swipeSettings) {
                    //TOP Right to bottom left

                    if (swipeTopRightBottomLeft(xSwipe1, ySwipe1, xSwipe2, ySwipe2, 0.65, gamefield)) {
                        setNextPickerColor(gamefield.getBackground());
                        return true;
                    }
                    //BOTTOM LEFT TO TOP RIGHT
                    if (swipeBottomLeftTopRight(xSwipe1, ySwipe1, xSwipe2, ySwipe2, 0.65, gamefield)) {
                        setNextPickerColor(gamefield.getSnake());
                        return true;
                    }
                    //BOTTOM Right to TOP left
                    if (swipeBottomRightTopLeft(xSwipe1, ySwipe1, xSwipe2, ySwipe2, 0.65, gamefield)) {
                        setNextPickerColor(gamefield.getEatThing());
                        return true;
                    }
                    //TOP LEFT TO BOTTOM RIGHT
                    if (swipeTopLeftBottomRight(xSwipe1, ySwipe1, xSwipe2, ySwipe2, 0.65, gamefield)) {
                        setKomaNextThingMode();
                        return true;
                    }
                }
                //If Snake in starting position
                gamefield.getSnake().startSnakeMovementFromStart();
                //just click
                if (clickControls && click(xSwipe1, ySwipe1, xSwipe2, ySwipe2, 10)) {
                    if (!updateNeeded) {
                        gamefield.getSnake().changeDirection();
                        updateNeeded = true;
                    }
                    return true;
                }

                //SwipeSnakeControll
                if (swipeControls) {
                    Heading swiped = SwipeControll(xSwipe1, ySwipe1, xSwipe2, ySwipe2);
                    if (swiped != null && gamefield.getSnake().isLegalMove(swiped)) {
                        gamefield.getSnake().setHeading(swiped);
                    }
                }

                return true;
        }
        return super.onTouchEvent(touchEvent);
    }

    protected void setNextPickerColor(ObjectBase object) {

        setShowStartInfo(false);
        object.setColorByNumber(object.getColor().getNumber() + 1);
        int colorEntryChangePrefernce = 0;
        String toastTestContent = "";
        if (object instanceof Background) {
            colorEntryChangePrefernce = 1;
        } else if (object instanceof EatThing) {
            colorEntryChangePrefernce = 3;
        } else if (object instanceof Snake) {
            colorEntryChangePrefernce = 2;
        }
        gamefield.checkSameColor(colorEntryChangePrefernce);
        toastText(this.getContext(), toastTestContent, Toast.LENGTH_SHORT);
        setPref();
    }

    protected void setKomaNextThingMode() {
        setShowStartInfo(false);
        gamefield.setModeByNumber(gamefield.getMode().getNumber() + 1);
        if (gamefield.getMode() == Mode.PORTALS) {
            gamefield.spawnPortals();
        }
        setPref();
    }

    protected void draw() {
        if (surfaceHolder.getSurface().isValid()) {
            Canvas canvas = surfaceHolder.lockCanvas();
            //Background
            ColorMaker.drawColor(gamefield.getBackground().getColor(), canvas);
            //Snake
            if (!touched && blinking) {
                blinking = false;
                for (int i = 0; i < gamefield.getSnake().getSize(); i++) {
                    if (i == 0) {
                        //snake head
                        ColorMaker.drawRect(gamefield.getSnake().getxPosTmp()[i] * gamefield.getBlockSize(), gamefield.getSnake().getyPosTmp()[i] * gamefield.getBlockSize(), (gamefield.getSnake().getxPosTmp()[i] * gamefield.getBlockSize()) + gamefield.getBlockSize(), (gamefield.getSnake().getyPosTmp()[i] * gamefield.getBlockSize()) + gamefield.getBlockSize(), gamefield.getSnake().getHeadColor(), canvas, paint);
                        continue;
                    }
                    ColorMaker.drawRect(gamefield.getSnake().getxPosTmp()[i] * gamefield.getBlockSize(), gamefield.getSnake().getyPosTmp()[i] * gamefield.getBlockSize(), (gamefield.getSnake().getxPosTmp()[i] * gamefield.getBlockSize()) + gamefield.getBlockSize(), (gamefield.getSnake().getyPosTmp()[i] * gamefield.getBlockSize()) + gamefield.getBlockSize(), gamefield.getSnake().getColor(), canvas, paint);
                }
            } else if (!touched) {
                blinking = true;
            } else {
                for (int i = 0; i < gamefield.getSnake().getSize(); i++) {
                    if (i == 0) {
                        //snake head
                        ColorMaker.drawRect(gamefield.getSnake().getxPos()[i] * gamefield.getBlockSize(), gamefield.getSnake().getyPos()[i] * gamefield.getBlockSize(), (gamefield.getSnake().getxPos()[i] * gamefield.getBlockSize()) + gamefield.getBlockSize(), (gamefield.getSnake().getyPos()[i] * gamefield.getBlockSize()) + gamefield.getBlockSize(), gamefield.getSnake().getHeadColor(), canvas, paint);
                        continue;
                    }
                    ColorMaker.drawRect(gamefield.getSnake().getxPos()[i] * gamefield.getBlockSize(), gamefield.getSnake().getyPos()[i] * gamefield.getBlockSize(), (gamefield.getSnake().getxPos()[i] * gamefield.getBlockSize()) + gamefield.getBlockSize(), (gamefield.getSnake().getyPos()[i] * gamefield.getBlockSize()) + gamefield.getBlockSize(), gamefield.getSnake().getColor(), canvas, paint);
                }
            }
            //Food
            ColorMaker.drawRect(gamefield.getEatThing().getxPos() * gamefield.getBlockSize(), gamefield.getEatThing().getyPos() * gamefield.getBlockSize(), (gamefield.getEatThing().getxPos() * gamefield.getBlockSize()) + gamefield.getBlockSize(), (gamefield.getEatThing().getyPos() * gamefield.getBlockSize()) + gamefield.getBlockSize(), gamefield.getEatThing().getColor(), canvas, paint);
            //Portals
            if (gamefield.getMode() == Mode.PORTALS || gamefield.getMode() == Mode.COMBINED) {
                //Draw Portals
                ColorMaker.drawCircle(gamefield.getPortal().getxPos1() * gamefield.getBlockSize() + gamefield.getBlockSize() / 2, gamefield.getPortal().getyPos1() * gamefield.getBlockSize() + gamefield.getBlockSize() / 2, gamefield.getBlockSize() / 2, gamefield.getPortal().getColor1(), canvas, paint);
                ColorMaker.drawCircle(gamefield.getPortal().getxPos2() * gamefield.getBlockSize() + gamefield.getBlockSize() / 2, gamefield.getPortal().getyPos2() * gamefield.getBlockSize() + gamefield.getBlockSize() / 2, gamefield.getBlockSize() / 2, gamefield.getPortal().getColor2(), canvas, paint);
            }
            //Menu Icon
            if (gamefield.getBackground().getColor() == CustomColor.GOLD) {
                canvas.drawBitmap(pauseSignWhite, (float) (gamefield.getSizeX() * 0.025), (float) (gamefield.getSizeX() * 0.025), null);
            } else {
                canvas.drawBitmap(pauseSignBlack, (float) (gamefield.getSizeX() * 0.025), (float) (gamefield.getSizeX() * 0.025), null);
            }
            if (showScore) {
                ColorMaker.drawText("" + score, (int) (gamefield.getSizeX() - gamefield.getSizeX() * 0.1), (int) (gamefield.getSizeY() * 0.05), (float) (gamefield.getSizeX() * .05), gamefield.getSnake().getColor(), canvas, paint);
            }
            if (startUp) {
                //TODO Tutorial
            }
            surfaceHolder.unlockCanvasAndPost(canvas);

        }
    }


    protected void toastText(Context toastContext, String text, int toastDuration) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(toastContext, text, toastDuration);
        if (showToast) {
            toast.show();
        }
    }

    public static void setShowToast(boolean showToast) {
        GameEngineView.showToast = showToast;
    }
    public static void setShowScore(boolean showScore) {
        GameEngineView.showScore = showScore;
    }

    @Override
    public Handler getHandler() {
        return handler;
    }

    public static void setPlaySounds(boolean value) {
        playSounds = value;
    }

    public static void setVibrate(boolean value) {
        vibrate = value;
    }
}
