package com.example.joseph.googleplaces.view.MainActivity;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import com.example.joseph.googleplaces.R;
import com.example.joseph.googleplaces.data.remote.RemoteDataSource;
import com.example.joseph.googleplaces.model.Response;
import com.example.joseph.googleplaces.model.Result;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements MainActivityContract.View, LocationListener, OnMapReadyCallback {

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 10;
    private static final String TAG = "MainActivity";
    FusedLocationProviderClient fusedLocationProviderClient;

    Location currentLocation;

    List<Result> places;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private ItemListAdapter itemListAdapter;

    private GoogleMap mMap;

    private String query;

    @Inject
    MainActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //setup dagger



        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =  findViewById(R.id.mySearchView);

        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        recyclerView = findViewById(R.id.rvPlaces);
        layoutManager = new LinearLayoutManager(this);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        checkPermission();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "onNewIntent: ");
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
            places.clear();
            mMap.clear();
//            presenter.getRecipes(0, query);
            searchByText(query);
        }

        //hide keyboard after search
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);

    }

    private void checkPermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "onCreate: not granted");

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                Log.d(TAG, "checkPermission: permission rationale");

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
                Log.d(TAG, "checkPermission: requesting permission");
            }
        } else {
            Log.d(TAG, "onCreate: already granted");
            getLocation();
        }
    }

    public void getLocation() {
        Log.d(TAG, "updateLocation: ");
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        currentLocation = location;
                        Log.d(TAG, "onSuccess: " + location.toString());

                        String loc = String.valueOf(currentLocation.getLatitude()) + "," + String.valueOf(currentLocation.getLongitude());
                        String radius = "1000";
                        searchNearbyPlaces(loc, radius);

                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: " + e.toString());
                    }
                });
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 1000, 100, this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Log.d(TAG, "onRequestPermissionsResult: granted");
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                    getLocation();

                } else {

                    Log.d(TAG, "onRequestPermissionsResult: permission denied");
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void searchNearbyPlaces(String location, String radius){
        RemoteDataSource.searchPlaces(location, radius)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Response>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(TAG, "onSubscribe: ");
                    }

                    @Override
                    public void onNext(@NonNull Response searchResult) {
                        places = searchResult.getResults();
//                        Log.d(TAG, "onNext: " + hitList.get(0).getRecipe().getImage());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, "onError: " + e.toString());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: ");


                        recyclerView.setLayoutManager(layoutManager);
                        itemListAdapter = new ItemListAdapter(places);
                        recyclerView.setAdapter(itemListAdapter);

                        moveMap();

                    }
                });
    }

    public void searchByText(String query){
        String loc = String.valueOf(currentLocation.getLatitude()) + "," + String.valueOf(currentLocation.getLongitude());
        String radius = "1000";
        RemoteDataSource.searchByText(query, loc, radius)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Response>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(TAG, "onSubscribe: ");
                    }

                    @Override
                    public void onNext(@NonNull Response searchResult) {
                        places = searchResult.getResults();
//                        Log.d(TAG, "onNext: " + hitList.get(0).getRecipe().getImage());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, "onError: " + e.toString());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: ");


                        recyclerView.setLayoutManager(layoutManager);
                        itemListAdapter = new ItemListAdapter(places);
                        recyclerView.setAdapter(itemListAdapter);

                        moveMap();

                    }
                });
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged: ");
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        Log.d(TAG, "onStatusChanged: ");
    }

    @Override
    public void onProviderEnabled(String s) {
        Log.d(TAG, "onProviderEnabled: ");
    }

    @Override
    public void onProviderDisabled(String s) {
        Log.d(TAG, "onProviderDisabled: ");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    public void moveMap(){
        LatLng currentLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng));
        mMap.addMarker(new MarkerOptions().position(currentLatLng).title("My Position").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_my_location_black_24dp)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15));

        LatLng latLng;
        for (Result r: places){
            latLng = new LatLng(r.getGeometry().getLocation().getLat(), r.getGeometry().getLocation().getLng());
            mMap.addMarker(new MarkerOptions().position(latLng).title(r.getName()));
        }

    }

    @Override
    public void showError(String error) {

    }

    @Override
    public void updateResults(List<Result> resultList) {

    }
}
