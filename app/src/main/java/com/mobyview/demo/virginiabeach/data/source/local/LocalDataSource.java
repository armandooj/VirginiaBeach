package com.mobyview.demo.virginiabeach.data.source.local;

import android.content.Context;

import com.mobyview.demo.virginiabeach.data.Place;
import com.mobyview.demo.virginiabeach.data.source.DataSourceCallback;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmModel;
import io.realm.RealmResults;

/**
 * Persist and retrieve objects using Realm.
 *
 * @author Armando Ochoa
 */
public class LocalDataSource {

    private Realm realm;
    static final boolean USE_CACHE = true;

    public LocalDataSource(Context context) {
        realm = initializeRealm(context);
    }

    private Realm initializeRealm(Context context) {
        Realm.init(context);
        RealmConfiguration config = new RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build();
        return Realm.getInstance(config);
    }

    public <E extends RealmModel> void persistObjects(Class<E> clazz, String response) {
        realm.beginTransaction();
        realm.createOrUpdateAllFromJson(clazz, response);
        realm.commitTransaction();
    }

    // Returns an empty list when no items were found
    public <E extends RealmModel> void getPlaces(int type, int page, DataSourceCallback callback) {
        if (USE_CACHE) {
            RealmResults<Place> results = realm.where(Place.class)
                    .equalTo("placeType", type)
                    .equalTo("pageNumber", page).findAll();
            if (results.size() > 0) {
                List<Place> items = realm.copyFromRealm(results);
                callback.onDataLoaded(items, type);
            } else {
                callback.onDataNotAvailable(null);
            }
        } else {
            callback.onDataNotAvailable(null);
        }
    }
}

