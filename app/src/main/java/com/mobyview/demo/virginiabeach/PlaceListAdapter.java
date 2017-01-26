package com.mobyview.demo.virginiabeach;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.Location;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mobyview.demo.virginiabeach.data.Place;
import com.mobyview.demo.virginiabeach.utilities.Utilities;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * An adapter with two view place types: attractions and restaurants.
 *
 * @author Armando Ochoa
 */
public class PlaceListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Location currentLocation;
    private List<Place> places;
    private int lastPosition = -1;

    public PlaceListAdapter(List<Place> places) {
        this.places = places;
    }

    @Override
    public int getItemViewType(int position) {
        return places.get(position).getPlaceType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == Place.TYPE_ATTRACTION) {
            View aView = inflater.inflate(R.layout.attraction_cell_content, parent, false);
            viewHolder = new AttractionViewHolder(aView);
        } else {
            View rView = inflater.inflate(R.layout.restaurant_cell_content, parent, false);
            viewHolder = new RestaurantViewHolder(rView);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final Place place = places.get(position);
        String[] coords = null;
        if (currentLocation != null && place.getGeo() != null) {
            if (place.getGeo().getLatlon() != null) {
                coords = place.getGeo().getLatlon().split(",");
            }
        }
        // populate the rows with real data
        if (holder.getItemViewType() == Place.TYPE_ATTRACTION) {
            AttractionViewHolder viewHolder = (AttractionViewHolder) holder;
            viewHolder.titleTextView.setText(place.getTitle());
            if (place.getLocation_area() != null) {
                viewHolder.locationTextView.setText(place.getLocation_area().getName());
            }
            if (coords != null) {
                viewHolder.distanceTextView.setText(Utilities.getDistanceFromCoordinates(currentLocation, coords) + " miles");
            }
            if (place.getAttraction_category() != null) {
                viewHolder.categoryTextView.setText(place.getAttraction_category().getName());
            }
            if (place.getPrice_range() != null) {
                viewHolder.priceTextView.setVisibility(View.VISIBLE);
                viewHolder.priceTextView.setText(place.getPrice_range());
            } else {
                viewHolder.priceTextView.setVisibility(View.GONE);
            }
            if (place.getImage() != null) {
                Picasso.with(viewHolder.view.getContext()).load(place.getImage().getUri()).into(viewHolder.imageView);
            } else {
                viewHolder.imageView.setImageDrawable(ContextCompat.getDrawable(viewHolder.view.getContext(), R.drawable.placeholder_attraction));
            }
        } else {
            RestaurantViewHolder viewHolder = (RestaurantViewHolder) holder;
            viewHolder.titleTextView.setText(place.getTitle());
            if (place.getLocation_area() != null) {
                viewHolder.locationTextView.setText(place.getLocation_area().getName());
            }
            if (coords != null) {
                viewHolder.distanceTextView.setText(Utilities.getDistanceFromCoordinates(currentLocation, coords) + " miles");
            }
            if (place.getImage() != null) {
                Picasso.with(viewHolder.view.getContext()).load(place.getImage().getUri()).into(viewHolder.imageView);
            } else {
                viewHolder.imageView.setImageDrawable(ContextCompat.getDrawable(viewHolder.view.getContext(), R.drawable.placeholder_restaurant));
            }
        }
        // use a fading animation
        setAnimation(holder.itemView, position);
        // on click go to detail
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PlaceDetailActivity.class);
                intent.putExtra("object", new Gson().toJson(place));
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public void onViewDetachedFromWindow(final RecyclerView.ViewHolder holder) {
        // fix problems on fast scroll
        if (holder instanceof AttractionViewHolder) {
            ((AttractionViewHolder) holder).itemView.clearAnimation();
        } else if (holder instanceof RestaurantViewHolder) {
            ((RestaurantViewHolder) holder).itemView.clearAnimation();
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

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    // TODO I think we could refactor AttractionViewHolder and RestaurantViewHolder into one
    public class AttractionViewHolder extends RecyclerView.ViewHolder {

        public View view;
        public TextView titleTextView;
        public TextView locationTextView;
        public TextView distanceTextView;
        public TextView categoryTextView;
        public TextView priceTextView;
        public ImageView imageView;

        public AttractionViewHolder(View view) {
            super(view);
            this.view = view;

            titleTextView = (TextView) view.findViewById(R.id.title);
            locationTextView = (TextView) view.findViewById(R.id.location);
            distanceTextView = (TextView) view.findViewById(R.id.distance);
            categoryTextView = (TextView) view.findViewById(R.id.category);
            priceTextView = (TextView) view.findViewById(R.id.price);
            imageView = (ImageView) view.findViewById(R.id.image);

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
        public ImageView imageView;

        public RestaurantViewHolder(View view) {
            super(view);
            this.view = view;

            titleTextView = (TextView) view.findViewById(R.id.title);
            locationTextView = (TextView) view.findViewById(R.id.location);
            distanceTextView = (TextView) view.findViewById(R.id.distance);
            imageView = (ImageView) view.findViewById(R.id.image);

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