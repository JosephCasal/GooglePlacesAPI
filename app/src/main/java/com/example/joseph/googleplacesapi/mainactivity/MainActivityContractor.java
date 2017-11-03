package com.example.joseph.googleplacesapi.mainactivity;

import android.content.Intent;

import com.example.joseph.googleplacesapi.BasePresenter;
import com.example.joseph.googleplacesapi.BaseView;

//import com.example.admin.googleplaces.BasePresenter;
//import com.example.admin.googleplaces.BaseView;

/**
 * Created by Admin on 11/2/2017.
 */

public interface MainActivityContractor {
    public interface View extends BaseView {
        public void checkPermission();
        public void setUpAutoComplete();
        public void onResults(int requestCode, int resultCode, Intent data);
    }

    public interface Presenter extends BasePresenter<View> {
        public void pickPlace();
    }
}
