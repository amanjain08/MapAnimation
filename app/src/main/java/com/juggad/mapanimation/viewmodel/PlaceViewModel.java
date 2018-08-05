package com.juggad.mapanimation.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import com.google.android.gms.location.places.Place;
import com.google.maps.android.SphericalUtil;
import com.juggad.mapanimation.data.model.PlaceItem;
import com.juggad.mapanimation.data.model.Resource;
import java.util.ArrayList;

/**
 * Created by Aman Jain on 02/08/18.
 */
public class PlaceViewModel extends ViewModel {

    private MutableLiveData<ArrayList<PlaceItem>> mPlacePoints = new MutableLiveData<>();

    private MutableLiveData<Resource<ArrayList<PlaceItem>>> mNearestPoints = new MutableLiveData<>();

    private MutableLiveData<Resource<ArrayList<PlaceItem>>> mAnimatePoints = new MutableLiveData<>();

    public void addPlaceItem(Place place) {
        ArrayList<PlaceItem> temp = new ArrayList<>();
        if (mPlacePoints.getValue() != null) {
            temp.addAll(mPlacePoints.getValue());
        }
        temp.add(mapPlaceToPlaceItem(place));
        mPlacePoints.postValue(temp);
    }

    public void deletePlaceItem(int position) {
        ArrayList<PlaceItem> temp = new ArrayList<>();
        if (mPlacePoints.getValue() != null) {
            temp.addAll(mPlacePoints.getValue());
        }
        if (!temp.isEmpty()) {
            temp.remove(position);
        }
        mPlacePoints.postValue(temp);
    }

    public LiveData<ArrayList<PlaceItem>> getPlaceLiveData() {
        return mPlacePoints;
    }

    public LiveData<Resource<ArrayList<PlaceItem>>> getNearestPoints() {
        return mNearestPoints;
    }

    public LiveData<Resource<ArrayList<PlaceItem>>> getAnimatePoints() {
        return mAnimatePoints;
    }

    public void showNearestPoint() {
        ArrayList<PlaceItem> placeItems = mPlacePoints.getValue();
        if (placeItems == null || placeItems.size() <= 1) {
            mNearestPoints.postValue(Resource.error("Please add two places atleast"));
        } else {
            ArrayList<PlaceItem> result = calculateNearestPoint(placeItems);
            mNearestPoints.postValue(Resource.success(result));
        }
    }

    private ArrayList<PlaceItem> calculateNearestPoint(final ArrayList<PlaceItem> placeItems) {
        int size = placeItems.size();
        double minDistance = Double.MAX_VALUE;
        int x = 0, y = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i != j) {
                    double distance = SphericalUtil
                            .computeDistanceBetween(placeItems.get(i).getLatLng(), placeItems.get(j).getLatLng());
                    if (distance < minDistance) {
                        minDistance = distance;
                        x = i;
                        y = j;
                    }
                }
            }
        }
        ArrayList<PlaceItem> result = new ArrayList<>();
        result.add(placeItems.get(x));
        result.add(placeItems.get(y));
        return result;
    }

    public void animatePoints() {
        ArrayList<PlaceItem> placeItems = mPlacePoints.getValue();
        if (placeItems == null || placeItems.isEmpty()) {
            mAnimatePoints.postValue(Resource.error("No places to show on map"));
        } else {
            mAnimatePoints.postValue(Resource.success(placeItems));
        }
    }

    private PlaceItem mapPlaceToPlaceItem(Place place) {
        return new PlaceItem(place);
    }
}
