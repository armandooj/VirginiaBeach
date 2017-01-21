package com.mobyview.demo.virginiabeach;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Implements a splash screen that is available immediately.
 * It will be visible only the amount of time it takes to the app to start.
 *
 * @author Armando Ochoa
 */
public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, PlaceListActivity.class);
        startActivity(intent);
        finish();
    }
}
