package com.mobyview.demo.virginiabeach.data;

import io.realm.RealmObject;

/**
 * @author Armando Ochoa
 */
public class LocationArea extends RealmObject {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
