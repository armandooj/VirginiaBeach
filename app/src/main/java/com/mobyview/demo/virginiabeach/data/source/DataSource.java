package com.mobyview.demo.virginiabeach.data.source;

import android.content.Context;
import android.os.AsyncTask;

import com.mobyview.demo.virginiabeach.data.source.remote.RemoteDataSource;

/**
 * A collection of operations to be sent to the server. A
 * {@link DataSourceCallback} must be passed to the requests in order for the
 * {@link RemoteDataSource} to return the result and/or errors of the remoteDataSource.
 * <p>
 * Activities should call {@method cancel()} on orientation changes and/or when
 * the activity is destroyed.
 *
 * @author Armando Ochoa
 */
public class DataSource {

    static final String baseURL = "https://jsonplaceholder.typicode.com";
    static final String itemsBaseRequest = "/posts";

    private static DataSource instance;
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

    public void getAttractions(Context context, final DataSourceCallback callback) {
        // get data from remote data source
        remoteDataSource = new RemoteDataSource(new DataSourceCallback() {

            @Override
            public void onDataLoaded(String response) {
                // TODO persist objects with Realm (LocalDataSource)
                callback.onDataLoaded(response);
            }

            @Override
            public void onDataNotAvailable(Exception e) {
                callback.onDataNotAvailable(e);
            }
        }, baseURL + itemsBaseRequest);
        remoteDataSource.execute();
    }
}