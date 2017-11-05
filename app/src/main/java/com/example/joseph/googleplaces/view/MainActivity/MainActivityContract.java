package com.example.joseph.googleplaces.view.MainActivity;

import com.example.joseph.googleplaces.model.Result;
import com.example.joseph.googleplaces.utils.BasePresenter;
import com.example.joseph.googleplaces.utils.BaseView;

import java.util.List;

/**
 * Created by joseph on 11/5/17.
 */

public interface MainActivityContract {

    interface View extends BaseView {

        void updateResults(List<Result> resultList);
//        void showProgress(int MODE);
    }

    interface Presenter extends BasePresenter<View> {
        void getPlaces(String location, String radius);


    }

}
