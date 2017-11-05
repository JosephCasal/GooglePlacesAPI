package com.example.joseph.googleplaces.data.remote;

import com.example.joseph.googleplaces.model.Response;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by joseph on 11/5/17.
 */

public interface RemoteService {


    @GET("place/nearbysearch/json")
    Observable<Response> getPlaces(@Query("key") String key, @Query("location") String location, @Query("radius") String radius);

    @GET("place/textsearch/json")
    Observable<Response> searchByText(@Query("query") String query, @Query("key") String key, @Query("location") String location, @Query("radius") String radius);

}
