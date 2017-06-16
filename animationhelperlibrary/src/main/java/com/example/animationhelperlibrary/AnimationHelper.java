package com.example.animationhelperlibrary;

import com.google.android.gms.maps.model.Polyline;

/**
 * Created by Aro-PC on 6/16/2017.
 */

public class AnimationHelper {
    private static AnimationHelper instance;
    public static synchronized AnimationHelper getInstance(){
        if (instance == null){
            instance = new AnimationHelper();
        }
        return instance;
    }

    public ColorAnimation addColorAnimation(Polyline polyline, int fromColor, int toColor){
        return new ColorAnimation(polyline,fromColor,toColor);
    }

}
