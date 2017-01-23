package com.mobyview.demo.virginiabeach.utilities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobyview.demo.virginiabeach.R;

/**
 * @author Armando Ochoa
 */
public class Utilities {

    public static boolean checkLocationPermission(Context c) {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = c.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    // The distance from currentLocation to coords in miles
    public static int getDistanceFromCoordinates(Location currentLocation, String[] coords) {
        Location destination = new Location("destination");
        destination.setLatitude(Float.valueOf(coords[0]));
        destination.setLongitude(Float.valueOf(coords[1]));
        // get the distance in miles
        return (int) ((currentLocation.distanceTo(destination) / 1000) * Constants.KM_TO_MILE);
    }

    public static ActionBar setCustomActionBar(final Activity activity, ActionBar actionBar, String title, boolean backButtonEnabled) {
        if (actionBar != null) {
            ViewGroup actionBarLayout;
            if (backButtonEnabled) {
                actionBarLayout = (ViewGroup) activity.getLayoutInflater().inflate(R.layout.custom_action_bar_back, null);
                ImageView back = (ImageView) actionBarLayout.findViewById(R.id.back);
                recreatePressedState(back);
                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.finish();
                    }
                });
            } else {
                actionBarLayout = (ViewGroup) activity.getLayoutInflater().inflate(R.layout.custom_action_bar, null);
                actionBar.setDisplayShowHomeEnabled(false);
            }

            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setCustomView(actionBarLayout);
            actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(activity, R.color.colorPrimary)));

            TextView actionBarTitle = (TextView) actionBarLayout.findViewById(R.id.text_actionbar_title);
            Typeface font = Typeface.createFromAsset(activity.getAssets(), "fonts/montserrat_regular.otf");
            actionBarTitle.setText(title);
            actionBarTitle.setTypeface(font);
        }
        return actionBar;
    }

    public static void recreatePressedState(final ImageView imageView) {
        imageView.setOnTouchListener(new View.OnTouchListener() {
            private Rect rect;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    imageView.setColorFilter(Color.argb(50, 0, 0, 0));
                    rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    imageView.setColorFilter(Color.argb(0, 0, 0, 0));
                }
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    if (!rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())) {
                        imageView.setColorFilter(Color.argb(0, 0, 0, 0));
                    }
                }
                return false;
            }
        });
    }

}
