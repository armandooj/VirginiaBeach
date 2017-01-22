package com.mobyview.demo.virginiabeach.data.source;

import android.os.AsyncTask;
import android.util.Log;

import com.mobyview.demo.virginiabeach.data.source.remote.RemoteDataSource;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

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

    private static final String TAG = DataSource.class.getName();

    public static final int PAGE_SIZE = 10;
    static final String baseURL = "http://virginia.mobyview.eu/api/entities";
    static final String charset = "UTF-8";

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

    public void getPlaces(String type, int page, final DataSourceCallback callback) {
        try {
            String query = String.format("parameters[type]=%s&page=%s&pagesize=%s&sort=%s",
            URLEncoder.encode(type, charset),
            URLEncoder.encode(String.valueOf(page), charset),
            URLEncoder.encode((PAGE_SIZE / 2) + "", charset),
            URLEncoder.encode("title", charset));

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
            }, baseURL + "?" + query);
            remoteDataSource.execute();
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "Error encoding parameters: " + e.getMessage());
            callback.onDataNotAvailable(e);
        }
    }
}