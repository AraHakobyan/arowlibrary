package com.example.animationhelperlibrary;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;

import com.google.android.gms.maps.model.Polyline;

/**
 * Created by Aro-PC on 6/16/2017.
 */

public class ColorAnimation {
    public ColorAnimation() {
    }

    private int duration = 1500;
    private Polyline polyline;

    public void setInterpolator(Interpolator interpolator) {
        this.interpolator = interpolator;
    }

    private Interpolator interpolator = new AccelerateInterpolator();
    private int fromColor = Color.GRAY;
    private int toColor = Color.BLACK;

    public ValueAnimator getStartColorAnim() {
        if(polyline != null) {
            startColorAnim = createValueAnimator(polyline);
        }
        return startColorAnim;
    }

    private ValueAnimator startColorAnim;

    public ColorAnimation setColors(int fromColor, int toColor){
        this.fromColor = fromColor;
        this.toColor = toColor;
        return this;
    }

    public ColorAnimation setDuration(int duration){
        this.duration = duration;
        return this;
    }

    private ValueAnimator createValueAnimator(final Polyline polyline) {
        final ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), toColor, fromColor);
        colorAnimation.setInterpolator(interpolator);
        colorAnimation.setDuration(duration);

        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                polyline.setColor((int) animator.getAnimatedValue());
            }

        });
        colorAnimation.setRepeatMode(ValueAnimator.REVERSE);
        colorAnimation.setRepeatCount(ValueAnimator.INFINITE);

        startColorAnim = ValueAnimator.ofObject(new ArgbEvaluator(), fromColor, toColor);
        startColorAnim.setInterpolator(new AccelerateInterpolator());
        startColorAnim.setDuration(duration);
        startColorAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                polyline.setColor((int) animation.getAnimatedValue());

            }
        });
        startColorAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                colorAnimation.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        return startColorAnim;
    }

    public void addPolyline(Polyline polyline) {
        this.polyline = polyline;
    }
}
