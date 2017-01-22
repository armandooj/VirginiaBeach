package com.mobyview.demo.virginiabeach.data;

import io.realm.RealmModel;
import io.realm.annotations.RealmClass;

/**
 * @author Armando Ochoa
 */
@RealmClass
public class Geo implements RealmModel {

    private String latlon;

    public String getLatlon() {
        return latlon;
    }

    public void setLatlon(String latlon) {
        this.latlon = latlon;
    }
}