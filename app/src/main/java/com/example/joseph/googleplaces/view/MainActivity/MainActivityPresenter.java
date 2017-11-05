package com.example.joseph.googleplaces.view.MainActivity;

import android.util.Log;

import com.example.joseph.googleplaces.data.remote.RemoteDataSource;
import com.example.joseph.googleplaces.model.Response;
import com.example.joseph.googleplaces.model.Result;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by joseph on 11/5/17.
 */

public class MainActivityPresenter implements MainActivityContract.Presenter {
    public static final int INIT_ITEMS = 0;

    RemoteDataSource remoteDataSource;

    @Inject
    public MainActivityPresenter(RemoteDataSource remoteDataSource) {
        this.remoteDataSource = remoteDataSource;
    }

    MainActivityContract.View view;
    List<Result> resultList = new ArrayList<>();
    public static final String TAG = "RecipeListPresenter";

    @Override
    public void attachView(MainActivityContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;

    }

    @Override
    public void getPlaces(String location, String radius) {

//        if (fromItemNumber == INIT_ITEMS)
//            view.showProgress(INIT_ITEMS);
//        else
//            view.showProgress(fromItemNumber);


        Observer<Result> observer = new Observer<Result>() {
            @Override
            public void onSubscribe(Disposable d) {

                Log.d(TAG, "onSubscribe: ");
            }

            @Override
            public void onNext(Result result) {

                resultList.add(result);
            }

            @Override
            public void onError(Throwable e) {
                view.showError(e.getMessage());
            }

            @Override
            public void onComplete() {
                view.updateResults(resultList);
            }
        };

        remoteDataSource.searchPlaces(location, radius)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .flatMap(new Function<Response, Observable<Result>>() {
                    @Override
                    public Observable<Result> apply(final Response response) throws Exception {

                        return Observable.create(new ObservableOnSubscribe<Result>() {
                            @Override
                            public void subscribe(ObservableEmitter<Result> e) throws Exception {

                                for (Result r : response.getResults()) {
//                                    e.onNext(r.getRecipe());
                                    Log.d(TAG, "subscribe: " + r.getName());
                                }
                                e.onComplete();


                            }
                        });
                    }
                })
                .subscribe(observer);


    }
}
