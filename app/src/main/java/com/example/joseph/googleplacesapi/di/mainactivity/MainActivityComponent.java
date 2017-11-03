package com.example.joseph.googleplacesapi.di.mainactivity;

//import com.example.admin.googleplaces.mainactivity.MainActivity;

import com.example.joseph.googleplacesapi.mainactivity.MainActivity;

import dagger.Component;

/**
 * Created by Admin on 11/2/2017.
 */

@Component(modules = MainActivityModule.class)
public interface MainActivityComponent {
    void inject(MainActivity mainActivity);
}
