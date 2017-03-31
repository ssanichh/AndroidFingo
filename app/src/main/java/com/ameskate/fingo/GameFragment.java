package com.ameskate.fingo;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class GameFragment extends Fragment {
    private final static String TAG = GameFragment.class.getSimpleName();
    private final static int ROW = 15;
    private final static int COLUMN = 16;
    private final static int EMPTY = -2;
    private final static String GAME_MODE = "game_mode";

    private GameFragmentListener mListener;
    private Unbinder mUnbinder;

    private GameMode mGameMode;
    private ToggleButton[][] mButtons = new ToggleButton[5][5];
    private ArrayList<Integer> mValues = new ArrayList<>();
    private boolean mFirstUnicorn = true;

    private int lastButtonRow;
    private int lastButtonColumn;
    private int buttonsInARow = 0;
    private int buttonsDirection = EMPTY;

    @BindView(R.id.game_container)
    LinearLayout mMainContainer;

    public GameFragment() {
    }

    public static GameFragment newInstance(GameMode mode) {
        GameFragment fragment = new GameFragment();
        Bundle args = new Bundle();
        args.putParcelable(GAME_MODE, mode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            mGameMode = getArguments().getParcelable(GAME_MODE);
        }
        setRetainInstance(true);
        mFirstUnicorn = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_game, container, false);
        mUnbinder = ButterKnife.bind(this, v);
        Log.d(TAG, "Create view inside fragment");

//        configureContainer();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mMainContainer.getVisibility() != View.VISIBLE) configureContainer();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof GameFragmentListener){
            mListener = (GameFragmentListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void configureContainer(){
        int minSize = getMinimalSize();

        RelativeLayout.LayoutParams params =
                (RelativeLayout.LayoutParams) mMainContainer.getLayoutParams();
        params.width = minSize;
        params.height = minSize;
        mMainContainer.setLayoutParams(params);

        fillContainer();

        mMainContainer.setVisibility(View.VISIBLE);
        mMainContainer.setAlpha(0f);
        mMainContainer.setScaleX(0f);
        mMainContainer.setScaleY(0f);
        mMainContainer.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(500)
                .setStartDelay(300)
                .setInterpolator(new AccelerateInterpolator())
                .start();
    }

    private int getMinimalSize(){
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Log.d(TAG, "Width = "+size.x+", Height = "+size.y);

        return size.x > size.y ? size.y : size.x;
    }

    private void fillContainer(){
        for(int i=0; i<mButtons.length; i++){

            LinearLayout row = ViewUtils.getRow(getContext());
            for(int j=0; j<mButtons[i].length; j++){
                ToggleButton button;
                if(mButtons[i][j] == null) {
                    if(i == 2 && j == 2) {
                        button = ViewUtils.getUnicornButton(getContext(), mCheckedListener);
                        button.setTag(R.string.value, "unicorn");
                    }else {
                        int val = getRandNum();
                        String value = mGameMode.getButtons().get(val);
                        button = ViewUtils.getButton(getContext(), mCheckedListener,
                                value);
                        button.setTag(R.string.value, value);
                    }
                    button.setTag(R.string.row, i);
                    button.setTag(R.string.column, j);
                    mButtons[i][j] = button;
                }else button = mButtons[i][j];

                row.addView(button);
            }

            mMainContainer.addView(row);
        }
    }

    private int getRandNum(){
        Random r = new Random();

        int val;
        while(mValues.contains((val = r.nextInt(24)))){

        }
        mValues.add(val);

        return val;
    }

    private boolean checkWinningRow(int row){
        if(row == 2) return false;
        for(int i=0; i<mButtons[row].length; i++){
            if(!mButtons[row][i].isChecked()) return false;
        }

        return true;
    }

    private boolean checkWinningColumn(int column){
        if(column == 2) return false;
        for(int i=0; i<mButtons.length; i++){
            if(!mButtons[i][column].isChecked()) return false;
        }

        return true;
    }

    private CompoundButton.OnCheckedChangeListener mCheckedListener =
            new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(!isChecked) {
                resetRowVariables();
                return;
            }

            int row = (Integer) buttonView.getTag(R.string.row);
            int column = (Integer) buttonView.getTag(R.string.column);

            if(row == 2 && column == 2){
                resetRowVariables();
                checkUnicorn(buttonView);

            }else if(checkWinningRow(row)){
                if(mListener != null) mListener.gameWin();

            }else if(checkWinningColumn(column)){
                if(mListener != null) mListener.gameWin();

            }else checkCheater(buttonView);
        }
    };

    private void checkUnicorn(CompoundButton buttonView) {
        if(mFirstUnicorn){
            mFirstUnicorn = false;
            buttonView.setChecked(false);
            showNotification("Cheater", "Katie says you're a cheater");

        }else{
            if(mListener != null) mListener.gameWin();
        }
    }

    private void resetRowVariables() {
        lastButtonRow = -1;
        lastButtonColumn = -1;
        buttonsDirection = EMPTY;
        buttonsInARow = 0;
    }

    private void showNotification(String title, String message){
        NotificationUtils.showNotification(getContext(), R.drawable.unicorn, title, message);
        NotificationUtils.showAlertDialog(getContext(), title,message,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "onClick: Alert dialog ok clicked");
                    }
                });
    }

    private void checkCheater(CompoundButton button){
        int row = (int) button.getTag(R.string.row);
        int column = (int) button.getTag(R.string.column);

        if(buttonsInARow == 0 && buttonsDirection == EMPTY){
            buttonsInARow++;

        }else if(buttonsDirection == EMPTY){
            if(Math.abs(row - lastButtonRow) == 1 && column == lastButtonColumn){
                buttonsDirection = COLUMN;
                buttonsInARow++;

            }else if(Math.abs(column - lastButtonColumn) == 1 && row == lastButtonRow){
                buttonsDirection = ROW;
                buttonsInARow++;

            }else resetCheater();

        }else if(buttonsDirection == COLUMN){
            if(Math.abs(row - lastButtonRow) == 1 && column == lastButtonColumn) buttonsInARow++;
            else if(Math.abs(column - lastButtonColumn) == 1 && row == lastButtonRow)
                buttonsDirection = ROW;
            else resetCheater();

        }else if(buttonsDirection == ROW){
            if(Math.abs(column - lastButtonColumn) == 1 && row == lastButtonRow) buttonsInARow++;
            else if(Math.abs(row - lastButtonRow) == 1 && column == lastButtonColumn)
                buttonsDirection = COLUMN;
            else resetCheater();

        }else resetCheater();

        if(buttonsInARow == 3){
            showNotification("Cheater", "Katie says you didn't see "+button.getTag(R.string.value));
            buttonsInARow = 0;
            buttonsDirection = EMPTY;
        }

        lastButtonRow = row;
        lastButtonColumn = column;
    }

    private void resetCheater() {
        buttonsInARow = 1;
        buttonsDirection = EMPTY;
    }

    public interface GameFragmentListener {
        void gameWin();
    }
}
