package com.juggad.mapanimation.data.model;

import com.google.gson.annotations.SerializedName;

public class Route {

    @SerializedName("overview_polyline")
    private OverviewPolyLine overviewPolyLine;

    public OverviewPolyLine getOverviewPolyLine() {
        return overviewPolyLine;
    }
}
