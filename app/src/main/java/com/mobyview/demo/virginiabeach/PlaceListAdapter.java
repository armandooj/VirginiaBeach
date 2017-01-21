package com.mobyview.demo.virginiabeach;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobyview.demo.virginiabeach.data.Attraction;
import com.mobyview.demo.virginiabeach.data.Place;
import com.mobyview.demo.virginiabeach.data.Restaurant;

import java.util.List;

/**
 * @author Armando Ochoa
 */
public class PlaceListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Object> places;
    private final int ATTRACTION = 0, RESTAURANT = 1;

    public PlaceListAdapter(List<Object> attractions) {
        this.places = attractions;
    }

    @Override
    public int getItemViewType(int position) {
        if (places.get(position) instanceof Attraction) {
            return ATTRACTION;
        } else {
            return RESTAURANT;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

       if (viewType == ATTRACTION) {
           View aView = inflater.inflate(R.layout.attraction_cell_content, parent, false);
           viewHolder = new AttractionViewHolder(aView);
       } else {
           View rView = inflater.inflate(R.layout.restaurant_cell_content, parent, false);
           viewHolder = new RestaurantViewHolder(rView);
       }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder.getItemViewType() == ATTRACTION) {
            AttractionViewHolder viewHolder = (AttractionViewHolder) holder;
            Attraction attraction = (Attraction) places.get(position);
            viewHolder.titleTextView.setText(attraction.getTitle());
        } else {
            RestaurantViewHolder viewHolder = (RestaurantViewHolder) holder;
            Restaurant restaurant = (Restaurant) places.get(position);
            viewHolder.titleTextView.setText(restaurant.getTitle());
        }
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    public class AttractionViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public TextView titleTextView;
        public AttractionViewHolder(View view) {
            super(view);
            this.view = view;
            titleTextView = (TextView) view.findViewById(R.id.title);
        }
    }

    public class RestaurantViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public TextView titleTextView;
        public RestaurantViewHolder(View view) {
            super(view);
            this.view = view;
            titleTextView = (TextView) view.findViewById(R.id.title);
        }
    }
}