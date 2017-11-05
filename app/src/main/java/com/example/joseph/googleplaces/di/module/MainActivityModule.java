package com.example.joseph.googleplaces.di.module;

import com.example.joseph.googleplaces.data.remote.RemoteDataSource;
import com.example.joseph.googleplaces.view.MainActivity.MainActivityPresenter;

import dagger.Module;

/**
 * Created by joseph on 11/5/17.
 */

@Module
public class MainActivityModule {

    //    Only have the presenter here since this is the only dependency we are passing in the
//    RecipeDetail activity
    MainActivityPresenter provideMainActivityPresenter(RemoteDataSource remoteDataSource){
        return new MainActivityPresenter(remoteDataSource);
    }
}