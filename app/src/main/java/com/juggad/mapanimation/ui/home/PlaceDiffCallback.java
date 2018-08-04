package com.juggad.mapanimation.ui.home;

import android.support.v7.util.DiffUtil;
import com.juggad.mapanimation.data.model.PlaceItem;

class PlaceDiffCallback extends DiffUtil.ItemCallback<PlaceItem> {

    @Override
    public boolean areItemsTheSame(final PlaceItem oldItem, final PlaceItem newItem) {
        return oldItem.getId().equals(newItem.getId());
    }

    @Override
    public boolean areContentsTheSame(final PlaceItem oldItem, final PlaceItem newItem) {
        return oldItem.getLatLng().equals(newItem.getLatLng());
    }
}
