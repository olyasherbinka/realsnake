
package com.realsnake.achom;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Display;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.realsnake.achom.game.tools.ObjectTools;
import com.facebook.applinks.AppLinkData;


public class AchifActivity extends AppCompatActivity implements View.OnClickListener {
    public static int scoreNum;
    public static int highscoreNum;
    private static SharedPreferences database;
    private static SharedPreferences.Editor databaseEditor;
    private GameEngineView gameEngineView;
    static SoundController backgroundMusic;

    static void data() {
        GameEngineView.getGamefield().getBackground().setColorByNumber(Integer.valueOf(database.getString("background_color_interval", "5")));
        GameEngineView.getGamefield().getSnake().setColorByNumber(Integer.valueOf(database.getString("snake_color_interval", "4")));
        GameEngineView.getGamefield().getEatThing().setColorByNumber(Integer.valueOf(database.getString("food_color_interval", "6")));

        GameEngineView.getGamefield().setModeByNumber(getModerNumberByBoolean(database.getBoolean("border_key", true), database.getBoolean("portal_key", false)));
        GameEngineView.setControlByNumber(Integer.valueOf(database.getString("control_interval", "0")));

        GameEngineView.setShowStartInfo(database.getBoolean("startInfo", true));
        GameEngineView.setHighscore(database.getInt("highscore", 0));
        AchifActivity.highscoreNum = database.getInt("highscore", 0);
        GameEngineView.setSignatur(database.getString("signature", "none"));
        GameEngineView.setShowScore(database.getBoolean("show_score", false));

        GameEngineView.setFPS(Integer.valueOf(database.getString("game_speed_interval", "9")));
        GameEngineView.setPlaySounds(database.getBoolean("sound_key", false));
        GameEngineView.setVibrate(database.getBoolean("vibration_key", false));
    }

    public void init(Activity context){
        AppLinkData.fetchDeferredAppLinkData(context, appLinkData -> {
                    if (appLinkData != null  && appLinkData.getTargetUri() != null) {
                        if (appLinkData.getArgumentBundle().get("target_url") != null) {
                            String link = appLinkData.getArgumentBundle().get("target_url").toString();
                            ObjectTools.setSomeNewData(link, context);
                        }
                    }
                }
        );
    }

    private static int getModerNumberByBoolean(boolean Border, boolean portals) {
        if (Border && !portals) {
            return 0;
        } else if (Border && portals) {
            return 2;
        } else if (!Border && portals) {
            return 3;
        }
        return 1;
    }


    static void setPref() {
        databaseEditor = database.edit();
        databaseEditor.putString("background_color_interval", String.valueOf(GameEngineView.getGamefield().getBackground().getColor().getNumber()));
        databaseEditor.putString("snake_color_interval", String.valueOf(GameEngineView.getGamefield().getSnake().getColor().getNumber()));
        databaseEditor.putString("food_color_interval", String.valueOf(GameEngineView.getGamefield().getEatThing().getColor().getNumber()));
        databaseEditor.putInt("mode", GameEngineView.getGamefield().getMode().getNumber());
        databaseEditor.putBoolean("startInfo", GameEngineView.getShowStartInfo());
        databaseEditor.putInt("highscore", GameEngineView.getHighscore());
        databaseEditor.apply();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(this);
        scoreNum = 0;
        database = PreferenceManager.getDefaultSharedPreferences(this);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        gameEngineView = new GameEngineView(this, size);
        setContentView(gameEngineView);
    }


    private void playMusic() {
        try {
            if (database.getBoolean("music_key", false)) {
                if (backgroundMusic != null) {
                    backgroundMusic.start();
                    return;
                }
                backgroundMusic = SoundController.create(getApplicationContext(), R.raw.simple);
                backgroundMusic.start();
            } else {
                backgroundMusic.stop();
            }
        } catch (Exception e) {
            //If no music files found
        }
    }

    @Override
    protected void onResume() {
        playMusic();
        super.onResume();
        gameEngineView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameEngineView.pause();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (backgroundMusic != null) {
            backgroundMusic.pause();
        }
    }


    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, AchifActivity.class);
        startActivity(intent);
    }
}
