package com.mobyview.demo.virginiabeach;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mobyview.demo.virginiabeach.data.Place;
import com.mobyview.demo.virginiabeach.utilities.Utilities;
import com.squareup.picasso.Picasso;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Overlay;

/**
 * A detail view of {@link Place} elements.
 *
 * @author Armando Ochoa
 */
public class PlaceDetailActivity extends Activity {

    private Place place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // this is how to manually change the cache dir (no need to ask for permission)
        Configuration.getInstance().setOsmdroidTileCache(getCacheDir());

        setContentView(R.layout.activity_place_detail);

        Utilities.setCustomActionBar(this, getActionBar(), getString(R.string.action_bar_title), true);

        // retrieve the place from the bundle
        String stringObject = getIntent().getExtras().getString("object");

        place = new Gson().fromJson(stringObject, Place.class);
        if (place != null) {
            // set the views that correspond to both restaurants and attractions
            setupCommonViews(place);
            // specific views
            setupSpecificViews(place);
        }
    }

    private void setupCommonViews(Place place) {
        if (place.getImage() != null) {
            ImageView image = (ImageView) findViewById(R.id.image);
            Picasso.with(this).load(place.getImage().getUri()).into(image);
        }
        Typeface mFont = Typeface.createFromAsset(getAssets(), "fonts/montserrat_regular.otf");
        Typeface oFont = Typeface.createFromAsset(getAssets(), "fonts/opensans_regular.ttf");

        TextView title = (TextView) findViewById(R.id.title);
        title.setText(place.getTitle());
        title.setTypeface(mFont);

        TextView about = (TextView) findViewById(R.id.about);
        about.setTypeface(mFont);

        TextView description = (TextView) findViewById(R.id.description);
        description.setTypeface(oFont);
        if (place.getDescription() != null) {
            description.setText(place.getDescription().getValue());
        }

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
        map.getOverlays().add(touchOverlay);

        // point to the place's location
        if (place.getGeo() != null) {
            String[] coords = place.getGeo().getLatlon().split(",");
            IMapController mapController = map.getController();
            mapController.setZoom(18);
            GeoPoint startPoint = new GeoPoint(Float.valueOf(coords[0]), Float.valueOf(coords[1]));
            mapController.setCenter(startPoint);
        }
    }

    private void setupSpecificViews(Place place) {
        TextView category = (TextView) findViewById(R.id.category);
        if (place.getPlaceType() == Place.TYPE_ATTRACTION) {
            if (place.getAttractionCategory() != null) {
                category.setText(place.getAttractionCategory().getName());
            }
            TextView priceTextView = (TextView) findViewById(R.id.price);
            priceTextView.setText(place.getPriceRange());
        } else if (place.getPlaceType() == Place.TYPE_RESTAURANT) {
            category.setVisibility(View.GONE);

            LinearLayout priceSeparator = (LinearLayout) findViewById(R.id.price_separator);
            priceSeparator.setVisibility(View.GONE);

            RelativeLayout priceLayout = (RelativeLayout) findViewById(R.id.price_container);
            priceLayout.setVisibility(View.GONE);
        }
    }

    private Overlay touchOverlay = new Overlay() {

        @Override
        public void draw(Canvas c, MapView osmv, boolean shadow) {}

        @Override
        public boolean onSingleTapConfirmed(final MotionEvent e, final MapView mapView) {
            // open google maps
            String[] coords = place.getGeo().getLatlon().split(",");
            String uri = "geo:0,0" + coords[0] + "," + coords[1] + "?q=" + place.getAddress();
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(intent);
            return false;
        }
    };
}