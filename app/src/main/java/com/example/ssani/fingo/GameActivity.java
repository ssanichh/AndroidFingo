package com.example.ssani.fingo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity implements GameFragment.GameFragmentListener,
        WinFragment.WinFragmentListener{
    public final static String TAG = GameActivity.class.getSimpleName();

    public static Intent getIntent(Context context){
        return new Intent(context, GameActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Fragment fragment = getSupportFragmentManager()
                .findFragmentById(R.id.container);
        if(fragment == null){
            fragment = GameFragment.newInstance();

            ActivityUtils
                    .addFragmentToActivity(getSupportFragmentManager(), fragment, R.id.container);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    public void gameWin() {
        WinFragment fragment = WinFragment.newInstance();
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                fragment, R.id.container, R.anim.scale_in, R.anim.fade_out);
    }

    @Override
    public void backToMenu() {
        finish();
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
