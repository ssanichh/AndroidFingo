package com.ameskate.fingo;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public final class ActivityUtils {

    private ActivityUtils(){}

    public static void addFragmentToActivity(@NonNull FragmentManager manager,
                                             @NonNull Fragment fragment, int frameId) {
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.add(frameId, fragment);
        transaction.commit();
    }

    public static void addFragmentToActivity(@NonNull FragmentManager manager,
                                             @NonNull Fragment fragment, int frameId,
                                             int enterAnimation, int exitAnimation){
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(enterAnimation, exitAnimation);
        transaction.replace(frameId, fragment);
        transaction.commit();
    }
}
