package com.ameskate.fingo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    private final static String GAME_MODES = "game_modes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.splash_container);
        if(fragment == null){
            fragment = SplashFragment.newInstance();

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment,
                    R.id.splash_container);
        }
    }



}

