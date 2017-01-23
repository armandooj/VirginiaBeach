package com.mobyview.demo.virginiabeach.data;

import io.realm.RealmObject;

/**
 * @author Armando Ochoa
 */
public class Description extends RealmObject {

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
