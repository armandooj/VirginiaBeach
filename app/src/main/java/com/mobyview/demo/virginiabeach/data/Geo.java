package com.mobyview.demo.virginiabeach.data;

import io.realm.RealmObject;

/**
 * @author Armando Ochoa
 */
public class Geo extends RealmObject {

    private String latlon;

    public String getLatlon() {
        return latlon;
    }

    public void setLatlon(String latlon) {
        this.latlon = latlon;
    }
}