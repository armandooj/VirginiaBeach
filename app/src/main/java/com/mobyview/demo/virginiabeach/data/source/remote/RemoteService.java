package com.mobyview.demo.virginiabeach.data.source.remote;

import com.mobyview.demo.virginiabeach.data.Place;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author Armando Ochoa
 */
public interface RemoteService {

    @GET("api/entities")
    Call<List<Place>> listPlaces(@Query("parameters[type]") String type, @Query("page") int page,
                           @Query("pagesize") int pageSize, @Query("sort") String sort);
}
