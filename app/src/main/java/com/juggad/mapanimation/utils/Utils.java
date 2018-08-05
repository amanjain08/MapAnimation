package com.juggad.mapanimation.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.RelativeLayout;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

/**
 * Created by Aman Jain on 05/08/18.
 */
public class Utils {

    public static BitmapDescriptor createBitmapFromView(Activity activity, View view) {
        if (activity == null || activity.isFinishing())
            return null;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        Bitmap bitmap = null;

        try {
            activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            view.setLayoutParams(new RelativeLayout.LayoutParams
                    (RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT));
            view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
            view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
            view.buildDrawingCache();
            bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(),
                    Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            canvas.drawColor(Color.TRANSPARENT);
            view.draw(canvas);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}
