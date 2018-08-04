package com.juggad.mapanimation.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Aman Jain on 02/08/18.
 */
public class PlaceItem implements Parcelable {

    String name;

    String id;

    LatLng mLatLng;

    public PlaceItem(final String name, final String id, final LatLng latLng) {
        this.name = name;
        this.id = id;
        mLatLng = latLng;
    }

    public PlaceItem(Place place) {
        this.id = place.getId();
        this.name = place.getName().toString();
        this.mLatLng = place.getLatLng();
    }

    protected PlaceItem(Parcel in) {
        name = in.readString();
        id = in.readString();
        mLatLng = in.readParcelable(LatLng.class.getClassLoader());
    }

    public static final Creator<PlaceItem> CREATOR = new Creator<PlaceItem>() {
        @Override
        public PlaceItem createFromParcel(Parcel in) {
            return new PlaceItem(in);
        }

        @Override
        public PlaceItem[] newArray(int size) {
            return new PlaceItem[0];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public LatLng getLatLng() {
        return mLatLng;
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeString(name);
        dest.writeString(id);
        dest.writeParcelable(mLatLng,flags);
    }

}
