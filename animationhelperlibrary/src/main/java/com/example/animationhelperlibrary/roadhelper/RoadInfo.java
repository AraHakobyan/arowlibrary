package com.example.animationhelperlibrary.roadhelper;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by Aro-PC on 6/19/2017.
 */

public class RoadInfo {

    private double distance;
    private int duration;

    public double getDistance() {
        return distance;
    }

    public int getDuration() {
        return duration;
    }

    public String getStartAdress() {
        return startAdress;
    }

    public String getEndAdress() {
        return endAdress;
    }

    public ArrayList<LatLng> getRoadPoints() {
        return roadPoints;
    }

    private String startAdress, endAdress;
    private ArrayList<LatLng> roadPoints;

    public RoadInfo(ArrayList<LatLng> roadPoints, double distance, int duration, String startAdress, String endAdress ) {
        this.distance = distance;
        this.duration = duration;
        this.startAdress = startAdress;
        this.endAdress = endAdress;
        this.roadPoints = roadPoints;
    }


}
