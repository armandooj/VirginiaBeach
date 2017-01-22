package com.mobyview.demo.virginiabeach;

import android.content.Context;
import android.graphics.Typeface;
import android.location.Location;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.mobyview.demo.virginiabeach.data.Attraction;
import com.mobyview.demo.virginiabeach.data.Restaurant;

import java.util.List;

/**
 * @author Armando Ochoa
 */
public class PlaceListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Location currentLocation;
    private List<Object> places;
    private final int ATTRACTION = 0, RESTAURANT = 1;
    private int lastPosition = -1;

    public PlaceListAdapter(List<Object> attractions) {
        this.places = attractions;
    }

    @Override
    public int getItemViewType(int position) {
        // TODO Add a default case
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
        // populate the rows with real data
        if (holder.getItemViewType() == ATTRACTION) {
            AttractionViewHolder viewHolder = (AttractionViewHolder) holder;
            Attraction attraction = (Attraction) places.get(position);
            viewHolder.titleTextView.setText(attraction.getTitle());
            if (attraction.getLocationArea() != null) {
                viewHolder.locationTextView.setText(attraction.getLocationArea().getName());
            }
            if (currentLocation != null && attraction.getGeo() != null) {
                if (attraction.getGeo().getLatlon() != null) {
                    String[] coords = attraction.getGeo().getLatlon().split(",");
                    viewHolder.distanceTextView.setText(getDistanceFromCoordinates(coords) + " miles");
                }
            }
            if (attraction.getAttractionCategory() != null) {
                viewHolder.categoryTextView.setText(attraction.getAttractionCategory().getName());
            }
            if (attraction.getPriceRange() != null) {
                viewHolder.priceTextView.setText(attraction.getPriceRange());
            } else {
                viewHolder.priceTextView.setVisibility(View.GONE);
            }
            setAnimation(holder.itemView, position);
        } else {
            RestaurantViewHolder viewHolder = (RestaurantViewHolder) holder;
            Restaurant restaurant = (Restaurant) places.get(position);
            viewHolder.titleTextView.setText(restaurant.getTitle());
            if (restaurant.getLocationArea() != null) {
                viewHolder.locationTextView.setText(restaurant.getLocationArea().getName());
            }
            if (currentLocation != null && restaurant.getGeo() != null) {
                if (restaurant.getGeo().getLatlon() != null) {
                    String[] coords = restaurant.getGeo().getLatlon().split(",");
                    viewHolder.distanceTextView.setText(getDistanceFromCoordinates(coords) + " miles");
                }
            }
            setAnimation(holder.itemView, position);
        }
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    private void setAnimation(View viewToAnimate, int position)  {
        // if the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)  {
            Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), android.R.anim.fade_in);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    public int getDistanceFromCoordinates(String[] coords) {
        Location destination = new Location("destination");
        destination.setLatitude(Float.valueOf(coords[0]));
        destination.setLongitude(Float.valueOf(coords[1]));
        // get the distance in miles
        return (int) ((currentLocation.distanceTo(destination) / 1000) * 0.621371f);
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    public class AttractionViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public TextView titleTextView;
        public TextView locationTextView;
        public TextView distanceTextView;
        public TextView categoryTextView;
        public TextView priceTextView;

        public AttractionViewHolder(View view) {
            super(view);
            this.view = view;

            titleTextView = (TextView) view.findViewById(R.id.title);
            locationTextView = (TextView) view.findViewById(R.id.location);
            distanceTextView = (TextView) view.findViewById(R.id.distance);
            categoryTextView = (TextView) view.findViewById(R.id.category);
            priceTextView = (TextView) view.findViewById(R.id.price);

            Context c = titleTextView.getContext();
            Typeface font = Typeface.createFromAsset(titleTextView.getContext().getAssets(), "fonts/montserrat_regular.otf");
            titleTextView.setTypeface(font);
            locationTextView.setTypeface(font);
            distanceTextView.setTypeface(font);
            categoryTextView.setTypeface(font);
            priceTextView.setTypeface(font);

            titleTextView.setTextColor(ContextCompat.getColor(c, R.color.darkGrayFont));
            locationTextView.setTextColor(ContextCompat.getColor(c, R.color.grayFont));
            distanceTextView.setTextColor(ContextCompat.getColor(c, R.color.grayFont));
            categoryTextView.setTextColor(ContextCompat.getColor(c, R.color.grayFont));
            priceTextView.setTextColor(ContextCompat.getColor(c, R.color.darkGrayFont));
        }
    }

    public class RestaurantViewHolder extends RecyclerView.ViewHolder {

        public View view;
        public TextView titleTextView;
        public TextView locationTextView;
        public TextView distanceTextView;

        public RestaurantViewHolder(View view) {
            super(view);
            this.view = view;

            titleTextView = (TextView) view.findViewById(R.id.title);
            locationTextView = (TextView) view.findViewById(R.id.location);
            distanceTextView = (TextView) view.findViewById(R.id.distance);

            Context c = titleTextView.getContext();
            Typeface font = Typeface.createFromAsset(c.getAssets(), "fonts/montserrat_regular.otf");
            titleTextView.setTypeface(font);
            locationTextView.setTypeface(font);
            distanceTextView.setTypeface(font);

            titleTextView.setTextColor(ContextCompat.getColor(c, R.color.darkGrayFont));
            locationTextView.setTextColor(ContextCompat.getColor(c, R.color.grayFont));
            distanceTextView.setTextColor(ContextCompat.getColor(c, R.color.grayFont));
        }
    }
}