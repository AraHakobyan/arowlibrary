package com.example.animationhelperlibrary.roadhelper;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Aro-PC on 6/19/2017.
 */

public class RoadHelper {
    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance + this.distance;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration + this.duration;
    }

    public String getStartAdress() {
        return startAdress;
    }

    public void setStartAdress(String startAdress) {
        this.startAdress = startAdress;
    }

    public String getEndAdress() {
        return endAdress;
    }

    public void setEndAdress(String endAdress) {
        this.endAdress = endAdress;
    }

    private double distance = 0;
    private int duration = 0;
    private String startAdress, endAdress;
    private CreatRoad creatRoad;
    private static RoadHelper instance;
    public static synchronized RoadHelper getInstance(){
        if (instance == null){
            instance = new RoadHelper();
        }
        return instance;
    }

    public void addRoadIsReadyListener(RoadIsReadyListener roadIsReadyListener){
        creatRoad.setRoadIsReadyListener(roadIsReadyListener);
    }

    public RoadHelper setPoints(LatLng... latLngs){
      creatRoad = new CreatRoad(latLngs);
      return this;
    }

    public RoadHelper addPoint(LatLng newPoint){
        if(creatRoad != null){
            creatRoad.addpoint(newPoint);
        }
        return this;
    }

    public void clear(){
        instance = null;
    }




}
