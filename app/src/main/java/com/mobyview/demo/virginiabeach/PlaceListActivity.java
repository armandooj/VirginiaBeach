package com.mobyview.demo.virginiabeach;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mobyview.demo.virginiabeach.data.Attraction;
import com.mobyview.demo.virginiabeach.data.Place;
import com.mobyview.demo.virginiabeach.data.Restaurant;
import com.mobyview.demo.virginiabeach.data.source.DataSource;
import com.mobyview.demo.virginiabeach.data.source.DataSourceCallback;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PlaceListActivity extends Activity {

    private RecyclerView recyclerView;
    private PlaceListAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private List<Object> places;

    // TODO implement proper pagination
    private int page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_list);

        recyclerView = (RecyclerView) findViewById(R.id.places_recycler_view);

        // TODO improve performance since we won't change the layout size?
        // recyclerView.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        // request places from the data source
        DataSource repository = DataSource.getInstance();
        repository.getPlaces("attraction", page, new AttractionsCallback());
        repository.getPlaces("restaurant", page, new RestaurantsCallback());
    }

    private void setOrUpdateList(List<Object> newPlaces) {
        if (places == null) {
            // populate list
            places = newPlaces;
            adapter = new PlaceListAdapter(places);
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
            // update the list
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy(){
        DataSource.getInstance().cancel();
        super.onDestroy();
    }

    private class AttractionsCallback implements DataSourceCallback {

        @Override
        public void onDataLoaded(String response) {
            // convert the JSON objects into attractions
            Type collectionType = new TypeToken<Collection<Attraction>>(){}.getType();
            List<Object> attractions = new Gson().fromJson(response , collectionType);
            setOrUpdateList(attractions);
        }

        @Override
        public void onDataNotAvailable(Exception e) {
            Toast.makeText(getApplicationContext(), "Data is not available! - " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private class RestaurantsCallback implements DataSourceCallback {

        @Override
        public void onDataLoaded(String response) {
            // convert the JSON objects into restaurants
            Type collectionType = new TypeToken<Collection<Restaurant>>(){}.getType();
            List<Object> restaurants = new Gson().fromJson(response , collectionType);
            setOrUpdateList(restaurants);
        }

        @Override
        public void onDataNotAvailable(Exception e) {
            Toast.makeText(getApplicationContext(), "Data is not available! - " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
