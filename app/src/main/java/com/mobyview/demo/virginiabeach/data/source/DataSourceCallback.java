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

    void onDataNotAvailable(String error);

    interface RemoteDataSourceCallback {

        void onDataLoaded(List<Place> places);

        void onDataNotAvailable(String error);
    }

}
