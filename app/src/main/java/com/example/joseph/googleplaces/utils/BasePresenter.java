package com.example.joseph.googleplaces.utils;

/**
 * Created by joseph on 11/5/17.
 */

public interface BasePresenter<V extends BaseView> {

    void attachView(V view);
    void detachView();
}
