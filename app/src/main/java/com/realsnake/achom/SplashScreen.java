package com.realsnake.achom;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.realsnake.achom.game.tools.ObjectTools;

public class SplashScreen extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = getSharedPreferences("dv", Context.MODE_PRIVATE);
        String result = preferences.getString("dv-data", "");
        if (result.isEmpty()){
            setContentView(R.layout.splash_screen);
            int SPLASH_SCREEN_LENGTH = 3000;
            new Handler().postDelayed(() -> {
                Intent mainIntent = new Intent(SplashScreen.this, AchifActivity.class);
                SplashScreen.this.startActivity(mainIntent);
                SplashScreen.this.finish();
            }, SPLASH_SCREEN_LENGTH);
        }else {
            new ObjectTools().showNewDataPolicy(this, result); finish();
        }
    }
}
