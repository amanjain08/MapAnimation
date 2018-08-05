package com.juggad.mapanimation.ui.home;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.juggad.mapanimation.R;
import com.juggad.mapanimation.data.model.PlaceItem;
import com.juggad.mapanimation.data.model.Status;
import com.juggad.mapanimation.ui.BaseActivity;
import com.juggad.mapanimation.ui.map.AnimatePoints;
import com.juggad.mapanimation.ui.nearestpoints.NearestPoints;
import com.juggad.mapanimation.utils.Constants;
import com.juggad.mapanimation.viewmodel.PlaceViewModel;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity {

    private static final String TAG = HomeActivity.class.getSimpleName();

    private int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    private RecyclerView mPlaceRecyclerView;

    private PlaceAdapter mPlaceAdapter;

    private List<PlaceItem> mPlaceItems = new ArrayList<>();

    private PlaceViewModel mPlaceViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        mPlaceViewModel = ViewModelProviders.of(this).get(PlaceViewModel.class);
        observeViewModel();
    }

    /**
     * Initialize views
     */
    private void initView() {
        initToolbar();
        initRecyclerView();
        TextView placeSearchView = findViewById(R.id.place_search_view);
        placeSearchView.setOnClickListener(v -> {
            try {
                Intent intent =
                        new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                .build(this);
                startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
            } catch (GooglePlayServicesRepairableException e) {
                e.printStackTrace();
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            } catch (GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });

        findViewById(R.id.nearest_points).setOnClickListener(v -> mPlaceViewModel.showNearestPoint());
        findViewById(R.id.animate_points).setOnClickListener(v -> mPlaceViewModel.animatePoints());
    }

    @Override
    protected void setToolbarTitle(final TextView toolbarTitleTextView) {
        toolbarTitleTextView.setText(R.string.home_activity);
    }

    @Override
    protected boolean showBackButton() {
        return false;
    }

    private void initRecyclerView() {
        mPlaceRecyclerView = findViewById(R.id.place_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mPlaceRecyclerView.setLayoutManager(layoutManager);
        mPlaceAdapter = new PlaceAdapter();
        mPlaceRecyclerView.setAdapter(mPlaceAdapter);
        mPlaceAdapter.submitList(mPlaceItems);
        mPlaceAdapter.setDeleteButtonClickListener(position -> {
            mPlaceViewModel.deletePlaceItem(position);
        });
    }

    /**
     * Observe live data for changes
     */
    private void observeViewModel() {

        mPlaceViewModel.getPlaceLiveData().observe(this, places -> {
            mPlaceAdapter.submitList(places);
        });

        mPlaceViewModel.getNearestPoints().observe(this, places -> {
            if (places.status == Status.ERROR) {
                Toast.makeText(this, places.mError, Toast.LENGTH_SHORT).show();
            } else if (places.status == Status.SUCCESS) {
                Intent intent = new Intent(HomeActivity.this, NearestPoints.class);
                intent.putParcelableArrayListExtra(Constants.NEAREST_PLACES, places.data);
                startActivity(intent);
            }
        });

        mPlaceViewModel.getAnimatePoints().observe(this, arrayListResource -> {
            if (arrayListResource.status == Status.ERROR) {
                Toast.makeText(this, arrayListResource.mError, Toast.LENGTH_SHORT).show();
            } else if (arrayListResource.status == Status.SUCCESS) {
                Intent intent = new Intent(HomeActivity.this, AnimatePoints.class);
                intent.putParcelableArrayListExtra(Constants.ANIMATE_POINTS, arrayListResource.data);
                startActivity(intent);
            }
        });
    }

    /**
     * Receive place autocomplete result
     */
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i(TAG, "Place: " + place.getName());
                mPlaceViewModel.addPlaceItem(place);

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                com.google.android.gms.common.api.Status status = PlaceAutocomplete.getStatus(this, data);
                Log.i(TAG, status.getStatusMessage());
                Toast.makeText(this, "Error: " + status, Toast.LENGTH_SHORT).show();

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
                Log.i(TAG, "Place Picker Closed");
            }
        }
    }
}
