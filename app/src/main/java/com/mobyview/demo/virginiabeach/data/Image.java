package com.mobyview.demo.virginiabeach.data;

import io.realm.RealmObject;

/**
 * @author Armando Ochoa
 */
public class Image extends RealmObject {

    private String uri;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
