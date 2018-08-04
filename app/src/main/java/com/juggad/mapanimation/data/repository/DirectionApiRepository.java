package com.juggad.mapanimation.data.repository;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Aman Jain on 03/08/18.
 */
public class DirectionApiRepository {
    public  DirectionApiService create(){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(DirectionApiService.DIRECTION_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit.create(DirectionApiService.class);
    }
}
