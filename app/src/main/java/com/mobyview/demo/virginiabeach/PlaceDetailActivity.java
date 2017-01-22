package com.mobyview.demo.virginiabeach;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageSwitcher;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mobyview.demo.virginiabeach.data.Attraction;
import com.mobyview.demo.virginiabeach.data.Place;
import com.mobyview.demo.virginiabeach.data.Restaurant;
import com.mobyview.demo.virginiabeach.utilities.Constants;
import com.mobyview.demo.virginiabeach.utilities.Utilities;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.views.MapView;

/**
 * @author Armando Ochoa
 */
public class PlaceDetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detail);

        Utilities.setCustomActionBar(this, getActionBar(), getString(R.string.action_bar_title), true);

        // retrieve the place from the bundle
        String stringObject = getIntent().getExtras().getString("object");
        int type = getIntent().getExtras().getInt("type");

        Object object = null;
        if (type == Constants.ATTRACTION) {
            object = new Gson().fromJson(stringObject, Attraction.class);
        } else if (type == Constants.RESTAURANT) {
            object = new Gson().fromJson(stringObject, Restaurant.class);
        }

        if (object != null) {
            // set the views that correspond to both restaurants and attractions
            setupCommonViews((Place) object);
            // specific views
            setupSpecificViews(object);
        }
    }

    private void setupCommonViews(Place place) {
        Typeface mFont = Typeface.createFromAsset(getAssets(), "fonts/montserrat_regular.otf");
        Typeface oFont = Typeface.createFromAsset(getAssets(), "fonts/opensans_regular.ttf");

        TextView title = (TextView) findViewById(R.id.title);
        title.setText(place.getTitle());
        title.setTypeface(mFont);

        TextView about = (TextView) findViewById(R.id.about);
        about.setTypeface(mFont);

        TextView description = (TextView) findViewById(R.id.description);
        description.setTypeface(oFont);
        // TODO put it back once the description works
        // description.setText(place.getDescription());

        TextView location = (TextView) findViewById(R.id.location);
        location.setTypeface(mFont);
        if (place.getLocationArea() != null) {
            location.setText(place.getLocationArea().getName());
        }

        TextView priceTextView = (TextView) findViewById(R.id.price);
        priceTextView.setTypeface(mFont);

        TextView priceLabel = (TextView) findViewById(R.id.price_label);
        priceLabel.setTypeface(mFont);

        TextView locationLabel = (TextView) findViewById(R.id.location_label);
        locationLabel.setTypeface(mFont);

        TextView addressLabel = (TextView) findViewById(R.id.address_label);
        addressLabel.setTypeface(mFont);

        TextView address = (TextView) findViewById(R.id.address);
        address.setTypeface(mFont);
        address.setText(place.getAddress());

        // let's use open street maps. Yay!
        MapView map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
    }

    private void setupSpecificViews(Object object) {
        // TODO set the image / title to black
        ImageSwitcher placeHolder = (ImageSwitcher) findViewById(R.id.imageSwitcher);
        TextView category = (TextView) findViewById(R.id.category);

        if (object instanceof Attraction) {
            Attraction attraction = (Attraction) object;
            placeHolder.setBackgroundResource(R.drawable.placeholder_attraction);
            if (attraction.getAttractionCategory() != null) {
                category.setText(attraction.getAttractionCategory().getName());
            }

            TextView priceTextView = (TextView) findViewById(R.id.price);
            priceTextView.setText(attraction.getPriceRange());
        } else if (object instanceof Restaurant) {
            placeHolder.setBackgroundResource(R.drawable.placeholder_restaurant);
            category.setVisibility(View.GONE);

            LinearLayout priceSeparator = (LinearLayout) findViewById(R.id.price_separator);
            priceSeparator.setVisibility(View.GONE);

            RelativeLayout priceLayout = (RelativeLayout) findViewById(R.id.price_container);
            priceLayout.setVisibility(View.GONE);

        }
    }
}
