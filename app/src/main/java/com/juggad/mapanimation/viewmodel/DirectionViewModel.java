package com.juggad.mapanimation.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import com.google.android.gms.maps.model.LatLng;
import com.juggad.mapanimation.data.model.Resource;
import com.juggad.mapanimation.data.model.PlaceItem;
import com.juggad.mapanimation.data.repository.DirectionApiRepository;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aman Jain on 03/08/18.
 */
public class DirectionViewModel extends ViewModel {

    private MutableLiveData<Resource<String>> mApiResponseLiveData = new MutableLiveData<>();

    private DirectionApiRepository mDirectionApiRepository;

    private Disposable disposables;

    public DirectionViewModel() {
        mDirectionApiRepository = new DirectionApiRepository();
    }

    public LiveData<Resource<String>> getDirectionApiResponse() {
        return mApiResponseLiveData;
    }

    public void getDirections(String key, List<PlaceItem> placeItems) {
        int size = placeItems.size();
        if (size <= 1) {
            mApiResponseLiveData.postValue(Resource.success(""));
            return;
        }
        LatLng origin = null, destination = null;
        List<LatLng> wayPoints = null;
        if (size > 1) {
            origin = placeItems.get(0).getLatLng();
            destination = placeItems.get(size - 1).getLatLng();
            if (size > 2) {
                wayPoints = new ArrayList<>();
                for (PlaceItem placeItem : placeItems.subList(1, size - 1)) {
                    wayPoints.add(placeItem.getLatLng());
                }
            }
        }
        disposables = mDirectionApiRepository.create()
                .getDirections(key, getLatLngString(origin), getLatLngString(destination),
                        wayPointsToString(wayPoints))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> mApiResponseLiveData.setValue(Resource.loading()))
                .subscribe(directionResults -> mApiResponseLiveData.postValue(
                        Resource.success(directionResults.getRoutes().get(0).getOverviewPolyLine().getPoints())),
                        throwable -> mApiResponseLiveData.postValue(Resource.error(throwable.getMessage())));
    }

    private String getLatLngString(final LatLng latLng) {
        return latLng.latitude + "," + latLng.longitude;
    }

    private String wayPointsToString(List<LatLng> wayPoints) {
        if (wayPoints != null && !wayPoints.isEmpty()) {
            StringBuilder string = new StringBuilder("");
            string.append(wayPoints.get(0).latitude).append(",").append(wayPoints.get(0).longitude);
            for (int i = 1; i < wayPoints.size(); i++) {
                string.append("|").append(wayPoints.get(i).latitude).append(",").append(wayPoints.get(i).longitude);
            }
            return string.toString();
        }
        return null;
    }

    @Override
    protected void onCleared() {
        if (disposables != null) {
            disposables.dispose();
        }
    }
}
