package com.juggad.mapanimation.ui.map;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLoadedCallback;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.juggad.mapanimation.R;
import com.juggad.mapanimation.data.model.PlaceItem;
import com.juggad.mapanimation.data.model.Resource;
import com.juggad.mapanimation.data.model.Status;
import com.juggad.mapanimation.ui.BaseActivity;
import com.juggad.mapanimation.utils.Constants;
import com.juggad.mapanimation.utils.Utils;
import com.juggad.mapanimation.viewmodel.DirectionViewModel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Aman Jain on 02/08/18.
 */
public class AnimatePoints extends BaseActivity implements OnMapReadyCallback, OnMapLoadedCallback {

    private static final String TAG = AnimatePoints.class.getSimpleName();

    List<PlaceItem> mPlaceItems = new ArrayList<>();

    DirectionViewModel mDirectionViewModel;

    GoogleMap mMap;

    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        initToolbar();
        if (getIntent() != null && getIntent().hasExtra(Constants.ANIMATE_POINTS)) {
            mPlaceItems = getIntent().getParcelableArrayListExtra(Constants.ANIMATE_POINTS);
        }
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mDirectionViewModel = ViewModelProviders.of(this).get(DirectionViewModel.class);
        observeViewModel();
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        googleMap.setOnMapLoadedCallback(this);
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style.", e);
        }

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        if (mPlaceItems == null || mPlaceItems.isEmpty()) {
            return;
        }

        if (mPlaceItems.size() > 1) {
            showProgressDialog();
        }

        //Bound camera to all the place items
        for (PlaceItem placeItem : mPlaceItems) {
            builder.include(placeItem.getLatLng());
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 300));
    }


    @Override
    public void onMapLoaded() {
        if (mPlaceItems == null || mPlaceItems.isEmpty()) {
            return;
        }
        if (mPlaceItems.size() == 1) {
            com.google.android.gms.maps.model.LatLng latLng = mPlaceItems.get(0).getLatLng();
            mMap.addMarker(new MarkerOptions().position(latLng));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));
        } else {
            mDirectionViewModel.getDirections(getString(R.string.MAP_KEY), mPlaceItems);
        }
    }

    /**
     * Observe live data
     */
    private void observeViewModel() {
        mDirectionViewModel.getDirectionApiResponse().observe(this, stringResource -> {
            if (stringResource.status == Status.LOADING) {
                showProgressDialog();
            } else if (stringResource.status == Status.SUCCESS) {
                hideProgressDialog();
                animateRoute(mMap, decodePolyline(stringResource));
            } else if (stringResource.status == Status.ERROR) {
                hideProgressDialog();
                Toast.makeText(this, "Error Occured: " + stringResource.mError, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Decode polyline into latlnt list from encoded string.
     */
    private List<LatLng> decodePolyline(final Resource<String> stringResource) {
        List<LatLng> decodedLatLng = PolyUtil.decode(stringResource.data);
        decodedLatLng.add(0, mPlaceItems.get(0).getLatLng());
        decodedLatLng.add(decodedLatLng.size(), mPlaceItems.get(mPlaceItems.size() - 1).getLatLng());
        return decodedLatLng;
    }

    /**
     * Animate polyline and add markers to all the placeitems
     */
    public void animateRoute(GoogleMap googleMap, List<LatLng> bangaloreRoute) {
        List<LatLng> foregroundPoints = new ArrayList<>();

        final PolylineOptions optionsForeground = new PolylineOptions().add(bangaloreRoute.get(0))
                .color(ContextCompat.getColor(this, R.color.polylineColor)).width(10);

        Polyline foregroundPolyline = googleMap.addPolyline(optionsForeground);

        RelativeLayout view = (RelativeLayout) LayoutInflater.from(AnimatePoints.this)
                .inflate(R.layout.marker_view, null);
        TextView placeName = view.findViewById(R.id.place_name);

        int size = bangaloreRoute.size();
        int duration = ((size / 2 + 2) * 16);
        final AtomicInteger index = new AtomicInteger();

        ValueAnimator polylineAnimator = ValueAnimator.ofInt(0, (size / 2) + 2);
        polylineAnimator.setDuration(duration);
        polylineAnimator.setInterpolator(new LinearInterpolator());

        polylineAnimator.addUpdateListener(valueAnimator -> {
            if (index.get() < size) {
                foregroundPoints.add(bangaloreRoute.get(index.getAndIncrement()));
                foregroundPoints.add(bangaloreRoute.get(index.getAndIncrement()));
                foregroundPolyline.setPoints(foregroundPoints);
            }
        });

        polylineAnimator.addListener(new AnimatorListener() {
            @Override
            public void onAnimationStart(final Animator animation) {

                placeName.setText(mPlaceItems.get(0).getName());
                mMap.addMarker(new MarkerOptions().position(bangaloreRoute.get(0))
                        .icon(Utils.createBitmapFromView(AnimatePoints.this, view)).anchor(0, 0.5f));
            }

            @Override
            public void onAnimationEnd(final Animator animation) {
                for (PlaceItem placeItem : mPlaceItems.subList(1, mPlaceItems.size())) {
                    placeName.setText(placeItem.getName());
                    mMap.addMarker(new MarkerOptions().position(placeItem.getLatLng())
                            .icon(Utils.createBitmapFromView(AnimatePoints.this, view)).anchor(0, 0.5f));
                }
            }

            @Override
            public void onAnimationCancel(final Animator animation) {

            }

            @Override
            public void onAnimationRepeat(final Animator animation) {

            }
        });
        polylineAnimator.start();

    }

    @Override
    protected void setToolbarTitle(final TextView toolbarTitleTextView) {
        toolbarTitleTextView.setText(R.string.map);
    }

    @Override
    protected boolean showBackButton() {
        return true;
    }

    /**
     * Show progress dialog
     */
    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Loading...");
        }
        mProgressDialog.show();
    }

    /**
     * Hide progress dialog
     */
    private void hideProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }
}
