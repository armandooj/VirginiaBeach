package com.mobyview.demo.virginiabeach.data;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmModel;
import io.realm.annotations.RealmClass;

/**
 * @author Armando Ochoa
 */
@RealmClass
public class Attraction extends Place implements RealmModel {

    @SerializedName("attraction_category")
    private AttractionCategory attractionCategory;
    @SerializedName("price_range")
    private String priceRange;

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
