package com.example.ssani.fingo;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class WinFragment extends Fragment {

    private WinFragmentListener mListener;
    private Unbinder mUnbinder;

    public WinFragment() {
    }
    public static WinFragment newInstance() {
        return new WinFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_win, container, false);

        ButterKnife.bind(this, v);

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof WinFragmentListener){
            mListener = (WinFragmentListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @OnClick(R.id.home)
    public void homeClick(){
        if(mListener != null) mListener.backToMenu();
    }

    public interface WinFragmentListener {
//        void onFragmentInteraction(Uri uri);
        void backToMenu();
    }
}
