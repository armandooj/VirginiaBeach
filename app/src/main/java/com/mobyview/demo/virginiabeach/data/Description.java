package com.mobyview.demo.virginiabeach.data;

import io.realm.RealmModel;
import io.realm.annotations.RealmClass;

/**
 * @author Armando Ochoa
 */
@RealmClass
public class Description implements RealmModel {

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
