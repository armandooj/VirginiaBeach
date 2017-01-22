package com.mobyview.demo.virginiabeach.utilities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.view.ViewGroup;
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

    public static ActionBar setCustomActionBar(Activity activity, ActionBar actionBar, String title) {
        if (actionBar != null) {
            ViewGroup actionBarLayout = (ViewGroup) activity.getLayoutInflater().inflate(R.layout.custom_action_bar, null);

            actionBar.setDisplayShowHomeEnabled(false);
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

}
