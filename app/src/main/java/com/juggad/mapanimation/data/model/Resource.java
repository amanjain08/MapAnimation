package com.juggad.mapanimation.data.model;

import android.support.annotation.NonNull;
import io.reactivex.annotations.Nullable;

public class Resource<T> {

    @NonNull
    public final Status status;

    @Nullable
    public final T data;

    @Nullable
    public final String mError;

    private Resource(@NonNull Status status, @Nullable T data, @Nullable String error) {
        this.status = status;
        this.data = data;
        this.mError = error;
    }

    public static <T> Resource<T> success(@NonNull T data) {
        return new Resource<>(Status.SUCCESS, data, null);
    }

    public static <T> Resource<T> error(String error) {
        return new Resource<>(Status.ERROR, null, error);
    }

    public static <T> Resource<T> loading() {
        return new Resource<>(Status.LOADING, null, null);
    }
}