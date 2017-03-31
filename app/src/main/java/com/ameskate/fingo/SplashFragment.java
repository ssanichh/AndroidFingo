package com.ameskate.fingo;

import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SplashFragment extends Fragment implements View.OnClickListener{

    @BindView(R.id.menu_container)
    LinearLayout mContainer;
    @BindView(R.id.game_title)
    TextView mTitle;
    @BindView(R.id.progress)
    ProgressBar mProgBar;

    private ArrayList<GameMode> mGameModes = null;
    private List<Button> mButtons = new ArrayList<>();
    private Button[][] mButtonsMatrix = new Button[5][5];

    private Unbinder mUnbinder;

    public SplashFragment() {}

    public static SplashFragment newInstance() {
        return new SplashFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_splash, container, false);

        mUnbinder = ButterKnife.bind(this, v);

        final DataManager dataManager = App.getInstance().getDataManager();
        if(mGameModes == null) dataManager.getGameData(new DataManager.GameDataCallback(){
            @Override
            public void result(ArrayList<GameMode> res) {
                loadFont();
                if(res.size() == 0){
                    res = dataManager.getOfflineGameData();
                }
                mGameModes = res;
                Collections.sort(mGameModes);
                mProgBar.setVisibility(View.GONE);
                createButtons();
            }
        });

        if(mGameModes != null) createButtons();

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mContainer.getVisibility() == View.INVISIBLE) configureContainer();

        if(mGameModes != null && mContainer.getVisibility() == View.INVISIBLE){
            animateViews();
        }
    }

    private void loadFont(){
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),
                Constants.ORANGE_FONT_FILE);
        mTitle.setTypeface(tf);
    }

    private void configureContainer() {
        int minSize = getMinimalSize();

        RelativeLayout.LayoutParams params =
                (RelativeLayout.LayoutParams) mContainer.getLayoutParams();
        params.width = minSize;
        params.height = minSize;
        mContainer.setLayoutParams(params);
    }

    private int getMinimalSize(){
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        return size.x > size.y ? size.y : size.x;
    }

    private void createButtons(){
        if(mGameModes == null || mGameModes.size() == 0) return;

        ArrayList<GameMode> tmp = new ArrayList<>(mGameModes);
        GameMode classic = findClassicMode(tmp);
        int position = 0;

        for(int i=0; i<mButtonsMatrix.length; i++){
            LinearLayout row = ViewUtils.getRow(getContext());

            for(int j=0; j<mButtonsMatrix[i].length; j++){
                Button button;
                if(mButtonsMatrix[i][j] == null){

                    if(i==2 && j==2){
                        button = ViewUtils.getMenuUnicorn(getContext(), classic, this);
                        position--;

                    }else{
                        GameMode mode = null;
                        if(position < tmp.size()) mode = tmp.get(position);
                        button = ViewUtils.getMenuButton(getContext(), mode, this);
                    }

                    mButtonsMatrix[i][j] = button;

                }else button = mButtonsMatrix[i][j];

                row.addView(button);
                position++;
            }

            mContainer.addView(row);
        }

        if(mContainer.getVisibility() != View.VISIBLE) animateViews();
    }

    private GameMode findClassicMode(ArrayList<GameMode> modes){
        for(int i=0; i<modes.size(); i++){
            GameMode mode = modes.get(i);
            if(mode.getName().equalsIgnoreCase(Constants.CLASSIC_MODE_NAME)){
                modes.remove(i);
                return mode;
            }
        }

        return null;
    }

    private void animateViews(){
        long duration = 500;
        long delay = 300;
//        mLogo.setTranslationY();
//        mLogo.animate().
        mContainer.setVisibility(View.VISIBLE);
        mContainer.setScaleX(0f);
        mContainer.setScaleY(0f);
        mContainer.setAlpha(0f);
        mContainer.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(duration)
                .setStartDelay(delay)
                .setInterpolator(new AccelerateInterpolator())
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        toggleButtons(true);
                    }
                })
                .start();

        mTitle.setVisibility(View.VISIBLE);
        mTitle.setTranslationY(mTitle.getHeight() * 2);
        mTitle.setAlpha(0f);
        mTitle.animate()
                .alpha(1f)
                .translationY(0)
                .setDuration(duration)
                .setStartDelay(delay)
                .setInterpolator(new OvershootInterpolator())
                .start();
    }


    private void toggleButtons(boolean state){
        for(int i=0; i<mButtonsMatrix.length; i++){
            for(int j=0; j<mButtonsMatrix[i].length; j++){
                mButtonsMatrix[i][j].setEnabled(state);
            }
        }
    }

    @Override
    public void onClick(View v) {
        toggleButtons(false);

        long duration = 300;
        final GameMode gameMode = (GameMode) v.getTag();

        mContainer.animate()
                .alpha(0f)
                .scaleX(0f)
                .scaleY(0f)
                .setDuration(duration)
                .setInterpolator(new AccelerateInterpolator())
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        mContainer.setVisibility(View.INVISIBLE);
                        Intent intent = GameActivity.getIntent(getContext());
                        intent.putExtra(GameActivity.GAME_MODE, gameMode);
                        startActivity(intent);
                        getActivity().overridePendingTransition(android.R.anim.fade_in,
                                android.R.anim.fade_out);
                    }
                })
                .start();

        mTitle.animate()
                .alpha(0f)
                .translationY(mTitle.getHeight() * 2)
                .setDuration(duration)
                .setInterpolator(new AnticipateInterpolator())
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        mTitle.setVisibility(View.INVISIBLE);
                    }
                })
                .start();
    }
}
