package com.mobyview.demo.virginiabeach.data.source.local;


import android.content.Context;

import com.google.gson.Gson;
import com.mobyview.demo.virginiabeach.data.Attraction;
import com.mobyview.demo.virginiabeach.data.Restaurant;
import com.mobyview.demo.virginiabeach.data.source.DataSourceCallback;
import com.mobyview.demo.virginiabeach.utilities.Constants;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmModel;
import io.realm.RealmResults;

/**
 * @author Armando Ochoa
 */
public class LocalDataSource {

    private Realm realm;

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
//    public <E extends RealmModel> void getPlaces(int type, int page, DataSourceCallback callback) {
//        if (type == Constants.ATTRACTION) {
//            RealmResults<Attraction> results = realm.where(Attraction.class).findAll();
//            if (results.size() > 0) {
//                List<Attraction> items = realm.copyFromRealm(results);
//                callback.onDataLoaded(new Gson().toJson(items));
//            } else {
//                callback.onDataNotAvailable(null);
//            }
//        } else if (type == Constants.RESTAURANT){
//            RealmResults<Restaurant> results = realm.where(Restaurant.class).findAll();
//            if (results.size() > 0) {
//                List<Restaurant> items = realm.copyFromRealm(results);
//                callback.onDataLoaded(new Gson().toJson(items));
//            } else {
//                callback.onDataNotAvailable(null);
//            }
//        }
//    }
}

