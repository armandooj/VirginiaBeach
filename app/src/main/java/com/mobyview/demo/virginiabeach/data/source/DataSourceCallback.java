package com.mobyview.demo.virginiabeach.data.source;

import com.mobyview.demo.virginiabeach.data.Place;

import java.util.List;

/**
 * To be implemented by classes using {@link DataSource}.
 *
 * @author Armando Ochoa
 */
public interface DataSourceCallback {

    void onDataLoaded(List<Place> places, int placeType);

    void onDataNotAvailable(Exception e);

    interface RemoteDataSourceCallback {

        void onDataLoaded(String response);

        void onDataNotAvailable(Exception e);
    }

}
