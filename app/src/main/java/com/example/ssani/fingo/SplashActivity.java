package com.example.ssani.fingo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.fabric.sdk.android.Fabric;

public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.logo)
    ImageView mLogo;
    @BindView(R.id.game_title)
    TextView mTitle;
    @BindView(R.id.start_button)
    Button mStartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash);

        ButterKnife.bind(this);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if(hasFocus && mLogo.getVisibility() == View.INVISIBLE){
            animateViews();
        }
    }

    private void animateViews(){
        long duration = 800;
        long delay = 500;
//        mLogo.setTranslationY();
//        mLogo.animate().
        mLogo.setVisibility(View.VISIBLE);
        mLogo.setAlpha(0f);
        mLogo.setTranslationY(mLogo.getHeight() * -0.6f);
        mLogo.animate()
                .alpha(1f)
                .translationY(0)
                .setDuration(duration)
                .setStartDelay(delay)
                .setInterpolator(new OvershootInterpolator())
                .start();

        mTitle.setVisibility(View.VISIBLE);
        mTitle.setScaleX(0f);
        mTitle.setScaleY(0f);
        mTitle.setAlpha(0f);
        mTitle.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(duration)
                .setStartDelay(delay)
                .setInterpolator(new OvershootInterpolator())
                .start();

        mStartButton.setVisibility(View.VISIBLE);
        mStartButton.setEnabled(true);
        mStartButton.setAlpha(0f);
        mStartButton.setTranslationY(mStartButton.getHeight() * 2);
        mStartButton.animate()
                .alpha(1f)
                .translationY(0)
                .setDuration(duration)
                .setStartDelay(delay)
                .setInterpolator(new OvershootInterpolator())
                .start();
    }

    @OnClick(R.id.start_button)
    public void startGame(){
        long duration = 300;

        mLogo.animate()
                .alpha(0f)
                .translationY(mLogo.getHeight() * -0.7f)
                .setDuration(duration)
                .setInterpolator(new AnticipateInterpolator())
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        mLogo.setVisibility(View.INVISIBLE);
                        startActivity(GameActivity.getIntent(SplashActivity.this));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }
                })
                .start();

        mTitle.animate()
                .alpha(0f)
                .scaleX(0f)
                .scaleY(0f)
                .setDuration(duration)
                .setInterpolator(new AnticipateInterpolator())
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        mTitle.setVisibility(View.INVISIBLE);
                    }
                })
                .start();

        mStartButton.setEnabled(false);
        mStartButton.animate()
                .alpha(0f)
                .translationY(mStartButton.getHeight() * 2)
                .setDuration(duration)
                .setInterpolator(new AnticipateInterpolator())
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        mStartButton.setVisibility(View.INVISIBLE);
                    }
                })
                .start();

//        startActivity(GameActivity.getIntent(this));
    }
}
