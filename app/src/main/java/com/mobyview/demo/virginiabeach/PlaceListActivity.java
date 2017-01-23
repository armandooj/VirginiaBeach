package com.mobyview.demo.virginiabeach;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mobyview.demo.virginiabeach.data.Attraction;
import com.mobyview.demo.virginiabeach.data.Place;
import com.mobyview.demo.virginiabeach.data.Restaurant;
import com.mobyview.demo.virginiabeach.data.source.DataSource;
import com.mobyview.demo.virginiabeach.data.source.DataSourceCallback;
import com.mobyview.demo.virginiabeach.utilities.Constants;
import com.mobyview.demo.virginiabeach.utilities.Utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * The main activity once the splash disappears from the screen.
 * It contains a list of {@link Restaurant} and {@link Attraction}.
 *
 * @author Armando Ochoa
 */
public class PlaceListActivity extends Activity implements ActivityCompat.OnRequestPermissionsResultCallback, DataSourceCallback {

    private static final String TAG = PlaceListActivity.class.getName();

    private RecyclerView recyclerView;
    private PlaceListAdapter adapter;
    private ProgressBar progressBar;
    private LinearLayoutManager linearLayoutManager;
    private List<Place> places;
    private List<Place> attractions;
    private List<Place> restaurants;
    private Location currentLocation;

    private boolean isLoading, isLastPage;
    private int page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_list);

        Utilities.setCustomActionBar(this, getActionBar(), getString(R.string.action_bar_title), false);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        recyclerView = (RecyclerView) findViewById(R.id.places_recycler_view);

        // improve performance since we won't change the layout size
        recyclerView.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        // use a scroll listener to handle pagination
        recyclerView.addOnScrollListener(recyclerViewOnScrollListener);

        // get the location to display the distance from the places
        if (Utilities.checkLocationPermission(this)) {
            // request the location. Once obtained we will update the list
            requestLocation();
        } else {
            // ask for permissions
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        // request places from the data source
        isLoading = true;
        DataSource repository = DataSource.getInstance();
        repository.getPlaces(getApplicationContext(), Place.TYPE_ATTRACTION, page, this);
        repository.getPlaces(getApplicationContext(), Place.TYPE_RESTAURANT, page, this);
    }

    @Override
    protected void onDestroy(){
        DataSource.getInstance().cancel();
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // if request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    requestLocation();
                }
                return;
            }
        }
    }

    @Override
    public void onDataLoaded(List<Place> places, int placeType) {
        if (placeType == Place.TYPE_ATTRACTION) {
            attractions = places;
        } else {
            restaurants = places;
        }
        setOrUpdateList();
    }

    @Override
    public void onDataNotAvailable(Exception e) {
        Toast.makeText(getApplicationContext(), "Data is not available! - " + e.getMessage(), Toast.LENGTH_LONG).show();
        progressBar.setVisibility(View.GONE);
        isLoading = false;
        isLastPage = false;
    }


    public void requestLocation() {
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try {
            currentLocation = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (adapter !=  null) {
                adapter.setCurrentLocation(currentLocation);
                adapter.notifyDataSetChanged();
            }
        } catch (SecurityException e) {
            Log.d(TAG, "requestLocation: " + e.getMessage());
        }
    }

    private void setOrUpdateList() {
        // wait until both lists are ready
        if (attractions != null && restaurants != null) {
            // merge both lists
            List<Place> temporal = new ArrayList<>();
            temporal.addAll(attractions);
            temporal.addAll(restaurants);
            // sort by title
            Collections.sort(temporal, new Comparator<Place>() {
                @Override
                public int compare(Place place1, Place place2) {
                    return (place1.getTitle().compareToIgnoreCase(place2.getTitle()));
                }
            });
            // add the new sorted list to the full list of places
            if (places == null) {
                places = temporal;
                adapter = new PlaceListAdapter(places);
                if (currentLocation != null) {
                    adapter.setCurrentLocation(currentLocation);
                }
                recyclerView.setAdapter(adapter);

                // the first time the list is not visible
                recyclerView.setVisibility(View.VISIBLE);
            } else {
                // pagination: update the list
                places.addAll(temporal);
                adapter.notifyDataSetChanged();
            }

            progressBar.setVisibility(View.GONE);
            isLoading = false;
            isLastPage = true;
            attractions = null;
            restaurants = null;
        }
    }

    private RecyclerView.OnScrollListener recyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int visibleItemCount = linearLayoutManager.getChildCount();
            int totalItemCount = linearLayoutManager.getItemCount();
            int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();

            if (!isLoading && isLastPage) {
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0
                        && totalItemCount >= Constants.PAGE_SIZE) {
                    // load more items
                    page++;
                    isLoading = true;
                    isLastPage = false;
                    progressBar.setVisibility(View.VISIBLE);
                    DataSource repository = DataSource.getInstance();
                    repository.getPlaces(getApplicationContext(), Place.TYPE_ATTRACTION, page, PlaceListActivity.this);
                    repository.getPlaces(getApplicationContext(), Place.TYPE_RESTAURANT, page, PlaceListActivity.this);
                }
            }
        }
    };
}
