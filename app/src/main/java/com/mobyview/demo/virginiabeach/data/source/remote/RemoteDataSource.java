package com.mobyview.demo.virginiabeach.data.source.remote;

import android.os.AsyncTask;
import android.util.Log;

import com.mobyview.demo.virginiabeach.data.source.DataSourceCallback;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * An auxiliary {@link AsyncTask} that communicates with the server.
 * On success, the callback's onDataLoaded() will be called.
 * On failure, the onDataNotAvailable() method.
 *
 * @author Armando Ochoa
 */
public class RemoteDataSource extends AsyncTask<Void, Void, RemoteDataSource.AsyncTaskResult<String>> {

    private static final String TAG = RemoteDataSource.class.getName();
    private DataSourceCallback.RemoteDataSourceCallback callback;
    private String targetUrl;

    public RemoteDataSource(DataSourceCallback.RemoteDataSourceCallback callback, String targetUrl) {
        this.callback = callback;
        this.targetUrl = targetUrl;
    }

    @Override
    protected AsyncTaskResult<String> doInBackground(Void... params) {
        try {
            String response = sendRequest();
            return new AsyncTaskResult<>(response);
        } catch (IOException e) {
            return new AsyncTaskResult<>(e);
        }
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<String> result) {
        if (result.getError() != null) {
            callback.onDataNotAvailable(result.getError());
        } else {
            callback.onDataLoaded(result.getResponse());
        }
    }

    private String sendRequest() throws IOException {
        URL url = new URL(targetUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        String response = "";
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            String line;
            StringBuilder builder = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            // interrupt while running if the activity finishes
            while (((line = reader.readLine()) != null) && !isCancelled()) {
                builder.append(line);
            }
            response = builder.toString();
            Log.d(TAG, "Server response: " + response);
        }

        connection.disconnect();
        return response;
    }

    /**
     * Auxiliary class used to delegate the error-handling task to onPostExecute().
     */
    public class AsyncTaskResult<T> {
        private T response;
        private Exception error;

        public AsyncTaskResult(T response) {
            super();
            this.response = response;
        }

        public AsyncTaskResult(Exception error) {
            super();
            this.error = error;
        }

        public T getResponse() {
            return response;
        }

        public Exception getError() {
            return error;
        }
    }
}
