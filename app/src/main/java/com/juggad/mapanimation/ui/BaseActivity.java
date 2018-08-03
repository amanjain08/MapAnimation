package com.juggad.mapanimation.ui;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import com.juggad.mapanimation.R;

/**
 * Created by Aman Jain on 02/08/18.
 */
public abstract class BaseActivity extends AppCompatActivity {

    TextView titleTextView;

    public void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        titleTextView = findViewById(R.id.title);
        setToolbarTitle(titleTextView);
        setSupportActionBar(toolbar);
        if (showBackButton() && getSupportActionBar() != null) {
            //To show back button in toolbar
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    protected abstract void setToolbarTitle(TextView toolbarTitleTextView);

    protected abstract boolean showBackButton();

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
