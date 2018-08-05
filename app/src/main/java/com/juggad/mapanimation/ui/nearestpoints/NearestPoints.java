package com.juggad.mapanimation.ui.nearestpoints;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import com.juggad.mapanimation.R;
import com.juggad.mapanimation.data.model.PlaceItem;
import com.juggad.mapanimation.ui.BaseActivity;
import com.juggad.mapanimation.ui.home.PlaceAdapter;
import com.juggad.mapanimation.utils.Constants;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aman Jain on 02/08/18.
 */
public class NearestPoints extends BaseActivity {

    RecyclerView mNearestPointRecyclerView;

    PlaceAdapter mPlaceAdapter;

    List<PlaceItem> mPlaceItems = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearest_points);
        if (getIntent() != null && getIntent().hasExtra(Constants.NEAREST_PLACES)) {
            mPlaceItems = getIntent().getParcelableArrayListExtra(Constants.NEAREST_PLACES);
        }
        initView();
    }

    private void initView() {
        initToolbar();
        initRecyclerView();
    }

    @Override
    protected void setToolbarTitle(final TextView toolbarTitleTextView) {
        toolbarTitleTextView.setText(R.string.nearest_points);
    }

    @Override
    protected boolean showBackButton() {
        return true;
    }

    private void initRecyclerView() {
        mNearestPointRecyclerView = findViewById(R.id.nearest_place_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mNearestPointRecyclerView.setLayoutManager(layoutManager);
        mPlaceAdapter = new PlaceAdapter(false);
        mNearestPointRecyclerView.setAdapter(mPlaceAdapter);
        mPlaceAdapter.submitList(mPlaceItems);
    }
}
