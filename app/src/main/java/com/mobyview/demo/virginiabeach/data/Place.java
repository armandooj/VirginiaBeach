package com.mobyview.demo.virginiabeach.data;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author Armando Ochoa
 */
public class Place extends RealmObject {

    public static final int TYPE_ATTRACTION = 0;
    public static final int TYPE_RESTAURANT = 1;

    @PrimaryKey
    private long id;
    private String title;
    private Image image;
    private LocationArea location_area;
    private Description description;
    private Geo geo;
    private String address;

    private AttractionCategory attraction_category;
    private String price_range;

    // auxiliary attribute, manually set
    private int pageNumber;
    // restaurant or attraction, manually set
    private int placeType;

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

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public LocationArea getLocation_area() {
        return location_area;
    }

    public void setLocation_area(LocationArea location_area) {
        this.location_area = location_area;
    }

    public Description getDescription() {
        return description;
    }

    public void setDescription(Description description) {
        this.description = description;
    }

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

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPlaceType() {
        return placeType;
    }

    public void setPlaceType(int placeType) {
        this.placeType = placeType;
    }

    public AttractionCategory getAttraction_category() {
        return attraction_category;
    }

    public void setAttraction_category(AttractionCategory attraction_category) {
        this.attraction_category = attraction_category;
    }

    public String getPrice_range() {
        return price_range;
    }

    public void setPrice_range(String price_range) {
        this.price_range = price_range;
    }
}
