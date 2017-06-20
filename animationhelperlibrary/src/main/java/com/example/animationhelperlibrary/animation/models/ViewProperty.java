package com.example.animationhelperlibrary.animation.models;

/**
 * Created by Aro-PC on 6/20/2017.
 */

public class ViewProperty {
    private final float mY;
    private final float mX;
    private final float mHeight;
    private final float mWidth;

    public ViewProperty(float mY, float mX, float mHeight, float mWidth) {
        this.mY = mY;
        this.mX = mX;
        this.mHeight = mHeight;
        this.mWidth = mWidth;
    }

    public float getmY() {
        return mY;
    }

    public float getmX() {
        return mX;
    }

    public float getmHeight() {
        return mHeight;
    }

    public float getmWidth() {
        return mWidth;
    }
}
