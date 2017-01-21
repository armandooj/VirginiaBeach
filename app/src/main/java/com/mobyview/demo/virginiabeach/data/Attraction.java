package com.mobyview.demo.virginiabeach.data;

/**
 * @author Armando Ochoa
 */
public class Attraction extends Place {

    private AttractionCategory attractionCategory;
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

    public class AttractionCategory {

        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
