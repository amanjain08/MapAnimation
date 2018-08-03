package com.juggad.mapanimation.ui.home;

import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import com.juggad.mapanimation.R;
import com.juggad.mapanimation.model.PlaceItem;
import com.juggad.mapanimation.ui.home.PlaceAdapter.PlaceViewHolder;
import java.text.DecimalFormat;

/**
 * Created by Aman Jain on 02/08/18.
 */
public class PlaceAdapter extends ListAdapter<PlaceItem, PlaceViewHolder> {

    private DeleteButtonClickListener mDeleteButtonClickListener;

    public PlaceAdapter() {
        super(new PlaceDiffCallback());
    }

    public void setDeleteButtonClickListener(
            final DeleteButtonClickListener deleteButtonClickListener) {
        mDeleteButtonClickListener = deleteButtonClickListener;
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_item, parent, false);
        return new PlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PlaceViewHolder holder, final int position) {
        holder.setData(getItem(position));
        holder.deleteButton.setOnClickListener(v -> {
            if (mDeleteButtonClickListener != null) {
                mDeleteButtonClickListener.onClicked(holder.getAdapterPosition());
            }
        });
    }

    class PlaceViewHolder extends RecyclerView.ViewHolder {

        TextView mPlaceName, mLatLng;

        ImageButton deleteButton;

        PlaceViewHolder(final View itemView) {
            super(itemView);
            mPlaceName = itemView.findViewById(R.id.place_name);
            mLatLng = itemView.findViewById(R.id.lat_lng);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }

        public void setData(final PlaceItem data) {
            mPlaceName.setText(data.getName());
            DecimalFormat f = new DecimalFormat("0.##");
            mLatLng.setText(String.format("%s, %s", f.format(data.getLatLng().latitude),
                    f.format(data.getLatLng().longitude)));
        }
    }

    interface DeleteButtonClickListener {

        void onClicked(int position);
    }


}
