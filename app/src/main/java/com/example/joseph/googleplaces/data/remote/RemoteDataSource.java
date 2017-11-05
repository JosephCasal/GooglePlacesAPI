package com.example.joseph.googleplaces.data.remote;

import android.content.Context;

import com.example.joseph.googleplaces.model.Response;

import javax.inject.Inject;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by joseph on 11/5/17.
 */

public class RemoteDataSource {

    public static final String API_KEY = "AIzaSyDntT2iBXaor8WEkxQ9ntBpQA0wA048qXU";

    Context context;

    private static String BASE_URL = "https://maps.googleapis.com/maps/api/";
//    private String BASE_URL;

    @Inject
    public RemoteDataSource(String BASE_URL, Context context) {
        this.BASE_URL = BASE_URL;
        this.context = context;
    }

    public static Retrofit create(){


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        return retrofit;

    }

    public static Observable<Response> searchPlaces(String location, String radius){
        Retrofit retrofit = create();
        RemoteService remoteService = retrofit.create(RemoteService.class);
        return remoteService.getPlaces(API_KEY, location, radius);
    }

    public static Observable<Response> searchByText(String query, String location, String radius){
        Retrofit retrofit = create();
        RemoteService remoteService = retrofit.create(RemoteService.class);
        return remoteService.searchByText(query, API_KEY, location, radius);
    }
}