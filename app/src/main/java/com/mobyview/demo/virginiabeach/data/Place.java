package com.mobyview.demo.virginiabeach.data;

import com.google.gson.annotations.SerializedName;

/**
 * @author Armando Ochoa
 */
public class Place {

    public static final int TYPE_ATTRACTION = 0;
    public static final int TYPE_RESTAURANT = 1;

    // @PrimaryKey
    private long id;
    private String title;
//    private Image image;
    @SerializedName("location_area")
    private LocationArea locationArea;
//    private Description description;
    private Geo geo;
    private String address;

    @SerializedName("attraction_category")
    private AttractionCategory attractionCategory;
    @SerializedName("price_range")
    private String priceRange;

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

    public AttractionCategory getAttractionCategory() {
        return attractionCategory;
    }

    public void setAttractionCategory(AttractionCategory attractionCategory) {
        this.attractionCategory = attractionCategory;
    }

    public String getPriceRange() {
        return priceRange;
    }

    public void setPriceRange(String priceRange) {
        this.priceRange = priceRange;
    }
}
