package com.juggad.mapanimation.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import com.google.android.gms.location.places.Place;
import com.juggad.mapanimation.model.PlaceItem;
import java.util.ArrayList;

/**
 * Created by Aman Jain on 02/08/18.
 */
public class PlaceViewModel extends ViewModel {

    private MutableLiveData<ArrayList<PlaceItem>> mPlacePoints = new MutableLiveData<>();

    private MutableLiveData<ArrayList<PlaceItem>> mNearestPoints = new MutableLiveData<>();

    private MutableLiveData<ArrayList<PlaceItem>> mAnimatePoints = new MutableLiveData<>();

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

    public LiveData<ArrayList<PlaceItem>> getNearestPoints() {
        return mNearestPoints;
    }

    public LiveData<ArrayList<PlaceItem>> getAnimatePoints() {
        return mAnimatePoints;
    }

    public void showNearestPoint() {

    }

    public void animatePoints() {
        mAnimatePoints.postValue(mPlacePoints.getValue());
    }

    private PlaceItem mapPlaceToPlaceItem(Place place) {
        return new PlaceItem(place);
    }
}
