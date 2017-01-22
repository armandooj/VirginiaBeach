package com.mobyview.demo.virginiabeach.data;

import io.realm.RealmModel;
import io.realm.annotations.RealmClass;

/**
 * @author Armando Ochoa
 */
@RealmClass
public class AttractionCategory implements RealmModel {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
