package com.ameskate.fingo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity implements View.OnClickListener{
    private final static String GAME_MODES = "game_modes";

    @BindView(R.id.logo)
    ImageView mLogo;
    @BindView(R.id.game_title)
    TextView mTitle;
    @BindView(R.id.menu_buttons_scroll)
    ScrollView mMenuView;
    @BindView(R.id.menu_buttons)
    LinearLayout mButtonsContainer;
    @BindView(R.id.progress)
    ProgressBar mProgBar;

    private ArrayList<GameMode> mGameModes = null;
    private List<Button> mButtons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ButterKnife.bind(this);

        Log.d("GAME_TEST", "Start get data from data manager");
        DataManager dataManager = App.getInstance().getDataManager();
        if(mGameModes == null) dataManager.getGameData(new DataManager.GameDataCallback(){
            @Override
            public void result(ArrayList<GameMode> res) {
                mGameModes = res;
                mProgBar.setVisibility(View.GONE);
                createButtons();
            }
        });
//        if(isOnline()){
//            DatabaseReference db = FirebaseDatabase.getInstance().getReference();
//            db.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    Log.d("FIREBASE_TEST", "Database result = "+dataSnapshot.getValue());
//                    Map<String, String> value = (Map<String, String>) dataSnapshot.getValue();
//                    Log.d("FIREBASE_TEST", "Result = "+value.toString());
//                    JSONObject json = new JSONObject(value);
//                    Log.d("FIREBASE_TEST", "JSONObject = "+json);
//                    Log.d("FIREBASE_TEST", "JSONObject names = "+json.names());
//                    Iterator<String> keyIterator = json.keys();
//                    while(keyIterator.hasNext()){
//                        String key = keyIterator.next();
//                        try {
//                            Log.d("FIREBASE_TEST", "Key = "+key+", value = "+json.get(key));
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
////                    try {
////                        JSONObject json  = new JSONObject(dataSnapshot.getValue().toString());
////                    } catch (JSONException e) {
////                        e.printStackTrace();
////                    }
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//                    Log.d("FIREBASE_TEST", "onCancelled: "+databaseError.getMessage());
//                }
//            });
//        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if(hasFocus && mLogo.getVisibility() == View.INVISIBLE){
            animateViews();
        }
    }

    private void createButtons(){
        if(mGameModes == null || mGameModes.size() == 0) return;

        for(GameMode mode : mGameModes){
            Button button = ViewUtils.getMenuButton(this, mode, this);
            mButtons.add(button);
            mButtonsContainer.addView(button);
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

        mMenuView.setVisibility(View.VISIBLE);
        toggleButtons(true);
        mMenuView.setAlpha(0f);
        mMenuView.setTranslationY(mMenuView.getHeight());
        mMenuView.animate()
                .alpha(1f)
                .translationY(0)
                .setDuration(duration)
                .setStartDelay(delay)
                .setInterpolator(new OvershootInterpolator())
                .start();

//        mStartButton.setVisibility(View.VISIBLE);
//        mStartButton.setEnabled(true);
//        mStartButton.setAlpha(0f);
//        mStartButton.setTranslationY(mStartButton.getHeight() * 2);
//        mStartButton.animate()
//                .alpha(1f)
//                .translationY(0)
//                .setDuration(duration)
//                .setStartDelay(delay)
//                .setInterpolator(new OvershootInterpolator())
//                .start();
    }


    private void toggleButtons(boolean state){
        for(Button b : mButtons){
            b.setEnabled(state);
        }
    }

    @Override
    public void onClick(View v) {
        toggleButtons(false);

        long duration = 300;
        final GameMode gameMode = (GameMode) v.getTag();

        mLogo.animate()
                .alpha(0f)
                .translationY(mLogo.getHeight() * -0.7f)
                .setDuration(duration)
                .setInterpolator(new AnticipateInterpolator())
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        mLogo.setVisibility(View.INVISIBLE);
                        Intent intent = GameActivity.getIntent(SplashActivity.this);
                        intent.putExtra(GameActivity.GAME_MODE, gameMode);
                        startActivity(intent);
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

        mMenuView.animate()
                .alpha(0f)
                .translationY(mMenuView.getHeight())
                .setDuration(duration)
                .setInterpolator(new AnticipateInterpolator())
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        mMenuView.setVisibility(View.INVISIBLE);
                    }
                })
                .start();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(GAME_MODES, mGameModes);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mGameModes = savedInstanceState.getParcelableArrayList(GAME_MODES);
    }
}
