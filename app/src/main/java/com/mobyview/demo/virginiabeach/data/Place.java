package com.mobyview.demo.virginiabeach.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author Armando Ochoa
 */
public class Place {

    private long id;
    private String title;
//    private Image image;
    @SerializedName("location_area")
    private LocationArea locationArea;
//    private Description description;
    private Geo geo;
    private String address;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

//    public Image getImage() {
//        return image;
//    }
//
//    public void setImage(Image image) {
//        this.image = image;
//    }

    public LocationArea getLocationArea() {
        return locationArea;
    }

    public void setLocationArea(LocationArea locationArea) {
        this.locationArea = locationArea;
    }

//    public Description getDescription() {
//        return description;
//    }
//
//    public void setDescription(Description description) {
//        this.description = description;
//    }

    public Geo getGeo() {
        return geo;
    }

    public void setGeo(Geo geo) {
        this.geo = geo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public class Description {

        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
