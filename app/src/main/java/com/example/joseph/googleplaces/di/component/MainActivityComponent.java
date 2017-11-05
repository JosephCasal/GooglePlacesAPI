package com.example.joseph.googleplaces.di.component;

import com.example.joseph.googleplaces.di.module.MainActivityModule;
import com.example.joseph.googleplaces.view.MainActivity.MainActivity;

import dagger.Subcomponent;

/**
 * Created by joseph on 11/5/17.
 */

@Subcomponent(modules = MainActivityModule.class)
public interface MainActivityComponent {

    void inject(MainActivity mainActivity);


}