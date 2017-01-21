package com.mobyview.demo.virginiabeach.data.source;

/**
 * To be implemented by classes using {@link DataSource}.
 *
 * @author Armando Ochoa
 */
public interface DataSourceCallback {

    void onDataLoaded(String response);

    void onDataNotAvailable(Exception e);
}
