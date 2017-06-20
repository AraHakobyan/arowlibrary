package com.example.animationhelperlibrary.animation;

import android.view.View;

import java.util.ArrayList;

/**
 * Created by Aro-PC on 6/20/2017.
 */
 class ViewStartPos {

    private static ViewStartPos instance;
    public static synchronized ViewStartPos getInstance(){
        if (instance == null){
            instance = new ViewStartPos();
        }
        return instance;
    }

    private ArrayList<ViewProperty> viewProperties;
    public ArrayList<ViewProperty> getViewProperties() {
        return viewProperties;
    }

    public void setProperty(ArrayList<View> viewArrayList) {
        viewProperties = new ArrayList<>();
        for (View view : viewArrayList){
            viewProperties.add(new ViewProperty(view.getY(),view.getX(),view.getHeight(),view.getWidth()));
        }
    }
    public void clear(){
        viewProperties = null;
    }

    public boolean isCanAnimate() {
        return canAnimate;
    }

    public void setCanAnimate(boolean canAnimate) {
        this.canAnimate = canAnimate;
    }

    private boolean canAnimate = true;

}
