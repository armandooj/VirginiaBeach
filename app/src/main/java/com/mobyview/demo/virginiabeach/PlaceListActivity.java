package com.mobyview.demo.virginiabeach;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.mobyview.demo.virginiabeach.data.source.DataSource;
import com.mobyview.demo.virginiabeach.data.source.DataSourceCallback;

public class PlaceListActivity extends Activity implements DataSourceCallback {

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_list);

        recyclerView = (RecyclerView) findViewById(R.id.places_recycler_view);

        // TODO improve performance since we won't change the layout size?
        // recyclerView.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        // request places from the data source
        DataSource repository = DataSource.getInstance();
        repository.getAttractions(getApplicationContext(), this);
    }

    @Override
    public void onDataLoaded(String response) {
        // TODO convert the JSON objects into places
        // TODO populate list
    }

    @Override
    public void onDataNotAvailable(Exception e) {
        Toast.makeText(getApplicationContext(), "Data is not available! - " + e.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy(){
        DataSource.getInstance().cancel();
        super.onDestroy();
    }
}
