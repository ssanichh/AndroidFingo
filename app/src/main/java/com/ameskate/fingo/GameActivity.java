package com.ameskate.fingo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity implements GameFragment.GameFragmentListener,
        WinFragment.WinFragmentListener{
    public final static String TAG = GameActivity.class.getSimpleName();
    public final static String GAME_MODE = "game_mode";

    private GameMode mGameMode;

    public static Intent getIntent(Context context){
        return new Intent(context, GameActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mGameMode = getIntent().getParcelableExtra(GAME_MODE);

        Fragment fragment = getSupportFragmentManager()
                .findFragmentById(R.id.container);
        if(fragment == null){
            fragment = GameFragment.newInstance(mGameMode);

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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(GAME_MODE, mGameMode);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mGameMode = savedInstanceState.getParcelable(GAME_MODE);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
