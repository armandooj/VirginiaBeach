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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mobyview.demo.virginiabeach.data.Attraction;
import com.mobyview.demo.virginiabeach.data.Place;
import com.mobyview.demo.virginiabeach.data.Restaurant;
import com.mobyview.demo.virginiabeach.data.source.DataSource;
import com.mobyview.demo.virginiabeach.data.source.DataSourceCallback;
import com.mobyview.demo.virginiabeach.utilities.Utilities;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * The main activity once the splash disappears from the screen.
 * It contains a list of {@link Restaurant} and {@link Attraction}.
 *
 * @author Armando Ochoa
 */
public class PlaceListActivity extends Activity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static final String TAG = PlaceListActivity.class.getName();

    private RecyclerView recyclerView;
    private PlaceListAdapter adapter;
    private ProgressBar progressBar;
    private LinearLayoutManager linearLayoutManager;
    private List<Object> places;
    private List<Attraction> attractions;
    private List<Restaurant> restaurants;
    private Location currentLocation;

    private boolean isLoading, isLastPage;
    private int page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_list);

        Utilities.setCustomActionBar(this, getActionBar(), getString(R.string.action_bar_title));

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
            // ask for permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        // request places from the data source
        isLoading = true;
        DataSource repository = DataSource.getInstance();
        repository.getPlaces("attraction", page, new AttractionsCallback());
        repository.getPlaces("restaurant", page, new RestaurantsCallback());
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

    private void setOrUpdateList(List<Object> newPlaces) {
        if (places == null) {
            // populate list
            places = newPlaces;
            adapter = new PlaceListAdapter(places);
            if (currentLocation != null) {
                adapter.setCurrentLocation(currentLocation);
            }
            recyclerView.setAdapter(adapter);
        } else {
            // merge both lists
            places.addAll(newPlaces);
            // sort by title
            Collections.sort(places, new Comparator<Object>() {
                @Override
                public int compare(Object object1, Object object2) {
                    return ((Place) object1).getTitle().compareToIgnoreCase(((Place) object2).getTitle());
                }
            });
            recyclerView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            // update the list
            adapter.notifyDataSetChanged();

            isLoading = false;
            isLastPage = true;
        }
    }

    private void setOrUpdateList() {
        // wait until both lists are ready
        if (attractions != null && restaurants != null) {
            // merge both lists
            List<Object> temporal = new ArrayList<>();
            temporal.addAll(attractions);
            temporal.addAll(restaurants);
            // sort by title
            Collections.sort(temporal, new Comparator<Object>() {
                @Override
                public int compare(Object object1, Object object2) {
                    return ((Place) object1).getTitle().compareToIgnoreCase(((Place) object2).getTitle());
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
                        && totalItemCount >= DataSource.PAGE_SIZE) {
                    // load more items
                    page++;
                    isLoading = true;
                    isLastPage = false;
                    progressBar.setVisibility(View.VISIBLE);
                    DataSource repository = DataSource.getInstance();
                    repository.getPlaces("attraction", page, new AttractionsCallback());
                    repository.getPlaces("restaurant", page, new RestaurantsCallback());
                }
            }
        }
    };

    private class AttractionsCallback implements DataSourceCallback {

        @Override
        public void onDataLoaded(String response) {
            // convert the JSON objects into attractions
            Type collectionType = new TypeToken<Collection<Attraction>>(){}.getType();
            attractions = new Gson().fromJson(response , collectionType);
            setOrUpdateList();
        }

        @Override
        public void onDataNotAvailable(Exception e) {
            notifyDataNotAvailable(e);
        }
    }

    private class RestaurantsCallback implements DataSourceCallback {

        @Override
        public void onDataLoaded(String response) {
            // convert the JSON objects into restaurants
            Type collectionType = new TypeToken<Collection<Restaurant>>(){}.getType();
            restaurants = new Gson().fromJson(response , collectionType);
            setOrUpdateList();
        }

        @Override
        public void onDataNotAvailable(Exception e) {
            notifyDataNotAvailable(e);
        }
    }

    private void notifyDataNotAvailable(Exception e) {
        Toast.makeText(getApplicationContext(), "Data is not available! - " + e.getMessage(), Toast.LENGTH_LONG).show();
        progressBar.setVisibility(View.GONE);
        isLoading = false;
        isLastPage = false;
    }
}
