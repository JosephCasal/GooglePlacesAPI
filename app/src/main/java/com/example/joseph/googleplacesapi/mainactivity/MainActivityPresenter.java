package com.example.joseph.googleplacesapi.mainactivity;

import android.util.Log;
import android.view.View;

import javax.inject.Inject;

/**
 * Created by Admin on 11/2/2017.
 */

public class MainActivityPresenter implements MainActivityContractor.Presenter {

//    private static final String TAG = "MainActivityPresenter";
//
//    @Inject
//    public MainActivityPresenter() {
//    }

    MainActivityContractor.View view;

    @Override
    public void addView(MainActivityContractor.View view) {
//        Log.d(TAG, "addView: ");
        this.view = view;
    }

    @Override
    public void removeView() {
        this.view = null;
    }

    public void pickPlace(){
        view.setUpAutoComplete();
    }
}
