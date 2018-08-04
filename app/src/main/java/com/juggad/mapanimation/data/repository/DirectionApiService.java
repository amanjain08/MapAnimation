package com.juggad.mapanimation.data.repository;

import com.juggad.mapanimation.data.model.DirectionResults;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Aman Jain on 03/08/18.
 */
public interface DirectionApiService {

    String DIRECTION_API_URL = "https://maps.googleapis.com/maps/api/directions/";

    @GET("json?mode=driving")
    Observable<DirectionResults> getDirections(@Query("key") String key, @Query("origin") String origin,
            @Query("destination") String destination, @Query("waypoints") String waypoints);

}
