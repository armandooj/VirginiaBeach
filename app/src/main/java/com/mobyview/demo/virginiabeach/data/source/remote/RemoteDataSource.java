package com.mobyview.demo.virginiabeach.data.source.remote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mobyview.demo.virginiabeach.data.Description;
import com.mobyview.demo.virginiabeach.data.Image;
import com.mobyview.demo.virginiabeach.data.Place;
import com.mobyview.demo.virginiabeach.data.source.DataSourceCallback;
import com.mobyview.demo.virginiabeach.utilities.Constants;

import java.lang.reflect.Type;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * An auxiliary class that communicates with the server.
 * On success, the callback's onDataLoaded() will be called.
 * On failure, the onDataNotAvailable() method.
 *
 * @author Armando Ochoa
 */
public class RemoteDataSource  {

    private DataSourceCallback.RemoteDataSourceCallback callback;
    private String targetUrl;

    public RemoteDataSource(DataSourceCallback.RemoteDataSourceCallback callback, String targetUrl) {
        this.callback = callback;
        this.targetUrl = targetUrl;
    }

    public void requestPlaces(String type, int page, String sort) {
        Call<List<Place>> call = prepareService().listPlaces(type, page, (Constants.PAGE_SIZE / 2), sort);
        call.enqueue(new Callback<List<Place>>() {

            @Override
            public void onResponse(Call<List<Place>> call, Response<List<Place>> response) {
                callback.onDataLoaded(response.body());
            }

            @Override
            public void onFailure(Call<List<Place>> call, Throwable t) {
                callback.onDataNotAvailable(t.getMessage());
            }
        });
    }

    private RemoteService prepareService() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Description.class, new CustomDescriptionDeserializer())
                .registerTypeAdapter(Image.class, new CustomImageDeserializer())
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(targetUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit.create(RemoteService.class);
    }

    private class CustomDescriptionDeserializer implements JsonDeserializer<Description> {

        @Override
        public Description deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {
            if (je.isJsonArray()) {
                return null;
            } else {
                Description description = new Description();
                JsonElement descriptionValue = je.getAsJsonObject().get("value");
                description.setValue(descriptionValue.getAsString());
                return description;
            }
        }
    }

    private class CustomImageDeserializer implements JsonDeserializer<Image> {

        @Override
        public Image deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {
            if (je.isJsonArray()) {
                return null;
            } else {
                Image image = new Image();
                JsonElement file = je.getAsJsonObject().get("file");
                JsonElement uriFull = file.getAsJsonObject().get("uri_full");
                image.setUri(uriFull.getAsString());
                return image;
            }
        }
    }

}
