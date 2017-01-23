package com.mobyview.demo.virginiabeach.data.source;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mobyview.demo.virginiabeach.data.Place;
import com.mobyview.demo.virginiabeach.data.source.local.LocalDataSource;
import com.mobyview.demo.virginiabeach.data.source.remote.RemoteDataSource;
import com.mobyview.demo.virginiabeach.utilities.Constants;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.List;

/**
 * A collection of operations to obtain places from cache and/or the server.
 * A {@link DataSourceCallback} must be passed to the requests in order for the
 * {@link RemoteDataSource} to return the result and/or errors of the remoteDataSource.
 *
 * Activities should call {@method cancel()} on orientation changes and/or when
 * the activity is destroyed.
 *
 * @author Armando Ochoa
 */
public class DataSource {

    private static final String TAG = DataSource.class.getName();

    static final String baseURL = "http://virginia.mobyview.eu/api/entities";
    static final String charset = "UTF-8";

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

    public void cancel() {
        if (remoteDataSource != null && remoteDataSource.getStatus() != AsyncTask.Status.FINISHED) {
            remoteDataSource.cancel(false);
        }
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
            public void onDataNotAvailable(Exception e) {
                Log.d(TAG, "No cache, requesting data.");
                // get data from remote data source
                try {
                    String query = String.format("parameters[type]=%s&page=%s&pagesize=%s&sort=%s",
                            URLEncoder.encode((type == Place.TYPE_RESTAURANT) ? "restaurant" : "attraction", charset),
                            URLEncoder.encode(String.valueOf(page), charset),
                            URLEncoder.encode((Constants.PAGE_SIZE / 2) + "", charset),
                            URLEncoder.encode("title", charset));

                    // get data from remote data source
                    remoteDataSource = new RemoteDataSource(new DataSourceCallback.RemoteDataSourceCallback() {

                        @Override
                        public void onDataLoaded(String response) {
                            // convert the JSON objects into places
                            Type collectionType = new TypeToken<Collection<Place>>(){}.getType();
                            List<Place> places = new Gson().fromJson(response , collectionType);
                            for (Place place : places) {
                                place.setPageNumber(page);
                                place.setPlaceType(type);
                            };
                            // persist objects with Realm (LocalDataSource)
                            localDataSource.persistObjects(Place.class, new Gson().toJson(places));
                            callback.onDataLoaded(places, type);
                        }

                        @Override
                        public void onDataNotAvailable(Exception e) {
                            callback.onDataNotAvailable(e);
                        }
                    }, baseURL + "?" + query);
                    remoteDataSource.execute();
                } catch (UnsupportedEncodingException ex) {
                    Log.e(TAG, "Error encoding parameters: " + ex.getMessage());
                    callback.onDataNotAvailable(ex);
                }
            }
        });
    }
}