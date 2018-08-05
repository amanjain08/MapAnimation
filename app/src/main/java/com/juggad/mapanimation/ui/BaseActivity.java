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

    /**
     * Call this method to initialize toolbar
     */
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

    /**
     * Override this method to set toolbar title
     */
    protected abstract void setToolbarTitle(TextView toolbarTitleTextView);

    /**
     * Override this method to toggle the visibility of back button. By default, back button is hidden.
     * @return
     */
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
