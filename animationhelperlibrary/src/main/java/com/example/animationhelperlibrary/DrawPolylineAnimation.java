package com.example.animationhelperlibrary;

import android.animation.ValueAnimator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aro-PC on 6/16/2017.
 */

public class DrawPolylineAnimation {

    private ValueAnimator drawAnim;
    private int duration = 1500;

    public void setPolyline(Polyline polyline) {
        this.polyline = polyline;
    }

    private Polyline polyline;

    public void setInterpolator(Interpolator interpolator) {
        this.interpolator = interpolator;
    }

    private Interpolator interpolator = new AccelerateInterpolator();

    public DrawPolylineAnimation setDuration(int duration){
        this.duration = duration;
        return this;
    }

    public ValueAnimator getDrawAnim() {
        if(polyline != null) {
            createValueAnimator(polyline);
        }
        return drawAnim;
    }

    private void createValueAnimator(final Polyline polyline) {
        List<LatLng> points = new ArrayList<>();
        points = polyline.getPoints();
        drawAnim = ValueAnimator.ofInt(0,100);
        drawAnim.setInterpolator(interpolator);
        drawAnim.setDuration(duration);
        final ArrayList<LatLng> arr = new ArrayList<>();
        final List<LatLng> finalPoints = points;
        drawAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                List<LatLng> foregroundPoints = finalPoints;

                int percentageValue = (int) animation.getAnimatedValue();
                int pointcount = foregroundPoints.size();
                int countTobeRemoved = (int) (pointcount * (percentageValue / 100.0f));
                List<LatLng> subListTobeRemoved = foregroundPoints.subList(0, countTobeRemoved);
                for (LatLng la : subListTobeRemoved)
                    arr.add(la);
                polyline.setPoints(arr);
                subListTobeRemoved.clear();
            }
        });
    }


}
