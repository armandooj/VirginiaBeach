package com.mobyview.demo.virginiabeach.data.source;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.mobyview.demo.virginiabeach.data.Place;
import com.mobyview.demo.virginiabeach.data.source.local.LocalDataSource;
import com.mobyview.demo.virginiabeach.data.source.remote.RemoteDataSource;

import java.util.List;

/**
 * A collection of operations to obtain places from cache and/or the server.
 * A {@link DataSourceCallback} must be passed to the requests in order for the
 * {@link RemoteDataSource} to return the result and/or errors of the remoteDataSource.
 *
 * @author Armando Ochoa
 */
public class DataSource {

    private static final String TAG = DataSource.class.getName();

    static final String baseURL = "http://virginia.mobyview.eu/";

    private static DataSource instance;
    private LocalDataSource localDataSource;
    private RemoteDataSource remoteDataSource;

    private DataSource() {
    }

    public static DataSource getInstance() {
        if (instance == null) {
            instance = new DataSource();
        }
        return instance;
    }

    public void getPlaces(Context c, final int type, final int page, final DataSourceCallback callback) {
        // first try to get cached places
        localDataSource = new LocalDataSource(c);
        localDataSource.getPlaces(type, page, new DataSourceCallback() {

            @Override
            public void onDataLoaded(List<Place> places, int type) {
                // respond immediately with cache if available
                Log.d(TAG, "Using cache: " + places.size());
                callback.onDataLoaded(places, type);
            }

            @Override
            public void onDataNotAvailable(String e) {
                Log.d(TAG, "No cache, requesting data.");
                // get data from remote data source
                remoteDataSource = new RemoteDataSource(new RemoteDataSourceCallback() {
                    @Override
                    public void onDataLoaded(List<Place> places) {
                        for (Place place : places) {
                            place.setPageNumber(page);
                            place.setPlaceType(type);
                        };
                        // persist objects with Realm (LocalDataSource)
                        localDataSource.persistObjects(Place.class, new Gson().toJson(places));
                        callback.onDataLoaded(places, type);
                    }

                    @Override
                    public void onDataNotAvailable(String error) {
                        callback.onDataNotAvailable(error);
                    }
                }, baseURL);
                remoteDataSource.requestPlaces((type == Place.TYPE_RESTAURANT) ? "restaurant" : "attraction", page, "title");
            }
        });
    }
}