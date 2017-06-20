package com.example.animationhelperlibrary.animation.hide_view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.Display;
import android.view.View;

import com.example.animationhelperlibrary.animation.ConstsAnimation;
import com.example.animationhelperlibrary.animation.models.ViewProperty;
import com.example.animationhelperlibrary.animation.utills.ViewStartPos;

import java.util.ArrayList;

/**
 * Created by Aro-PC on 6/20/2017.
 */

public class ShowAnimation {
    private int viewsLengts = 0;
    private int mDisplayHeight;
    private int mDisplayWidth;
    private ArrayList<Animator> allAnimators;
    private AnimatorSet animatorSet;
    private static ShowAnimation instance;
    public static synchronized ShowAnimation getInstance(){
        if (instance == null){
            instance = new ShowAnimation();
        }
        return instance;
    }
    public void setShowAnim(Display display, View... views) {

        stopAll();

        allAnimators = new ArrayList<>();
        ArrayList<View> viewArrayList = new ArrayList<>();
        viewsLengts = views.length;
        for (int i = 0; i < views.length; i++) {
            viewArrayList.add(views[i]);
        }

        mDisplayHeight = display.getHeight();
        mDisplayWidth = display.getWidth();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
             changeAlfaAnimator(viewArrayList, mDisplayHeight, mDisplayWidth);
        }

        ArrayList<ViewProperty> viewProperties = ViewStartPos.getInstance().getViewProperties();
        for (int i = 0; i< viewArrayList.size(); i++){
            View view = viewArrayList.get(i);
            ViewProperty viewProperty = viewProperties.get(i);
            //if view center is above of display center
            if(view.getY() + view.getHeight() / 2 < mDisplayHeight / 2){
                toUpAnimation(view,viewProperty);

            } else {
                toDownAnimation(view,viewProperty);



            }
        }



    }

    public void stopAll(){
        if (animatorSet != null) {
            animatorSet.removeAllListeners();
            animatorSet.end();
            animatorSet.cancel();
        }

    }

    public void startAll() {

        animatorSet = new AnimatorSet();
        animatorSet.playTogether(allAnimators);

        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {


            }

            @Override
            public void onAnimationEnd(Animator animation) {



            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.start();


    }

    private void  toDownAnimation(final View view, ViewProperty viewProperty) {

        final float mY = view.getY();

        ValueAnimator toDownAnimator = ValueAnimator.ofFloat(mDisplayHeight, viewProperty.getmY());
        toDownAnimator.setDuration(ConstsAnimation.SHOW_ANIMATION_DURATION);
        toDownAnimator.setInterpolator(new FastOutSlowInInterpolator());
        toDownAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float delta = (float) animation.getAnimatedValue();
                view.setY(delta);
                view.requestLayout();

            }
        });

        allAnimators.add(toDownAnimator);
    }

    private void toUpAnimation(final View view, ViewProperty viewProperty) {


        final float mY = view.getY();

        ValueAnimator toUpAnimator = ValueAnimator.ofFloat(-210, viewProperty.getmY());
        toUpAnimator.setDuration(ConstsAnimation.SHOW_ANIMATION_DURATION);
        toUpAnimator.setInterpolator(new FastOutSlowInInterpolator());
        toUpAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float delta = (float) animation.getAnimatedValue();
                view.setY(delta);
                view.requestLayout();

            }
        });

        allAnimators.add(toUpAnimator);

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private ArrayList<ValueAnimator> changeAlfaAnimator(ArrayList<View> viewArrayList, int mDisplayHeight, int mDisplayWidth) {
        ArrayList<ValueAnimator> valueAnimators = new ArrayList<>();
        for (final View view : viewArrayList) {

            ValueAnimator changeAlfaAnimator = ValueAnimator.ofInt(1, 254);
            changeAlfaAnimator.setDuration(ConstsAnimation.SHOW_ANIMATION_DURATION);
            changeAlfaAnimator.setInterpolator(new FastOutSlowInInterpolator());
            changeAlfaAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int delta = (int) animation.getAnimatedValue();

                    view.getBackground().setAlpha( delta);
                    view.requestLayout();

                }
            });
            valueAnimators.add(changeAlfaAnimator);
            allAnimators.add(changeAlfaAnimator);

        }
        return valueAnimators;
    }

//    private ArrayList<ValueAnimator> changeBoundsAnimation(ArrayList<View> viewArrayList, int mDisplayHeight, int mDisplayWidth) {
//        ArrayList<ValueAnimator> valueAnimators = new ArrayList<>();
//        for (final View view : viewArrayList) {
//
//            final int mHeight = view.getHeight();
//            final int mWidth = view.getWidth();
//            ValueAnimator changeBoundsAnimator = ValueAnimator.ofInt(0, 100);
//            changeBoundsAnimator.setDuration(ConstsAnimation.SHOW_ANIMATION_DURATION);
//            changeBoundsAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                @Override
//                public void onAnimationUpdate(ValueAnimator animation) {
//                    int delta = 100 - (int) animation.getAnimatedValue();
//
//                    view.getLayoutParams().height = mHeight * delta / 100;
//                    view.getLayoutParams().width = mWidth * delta / 100;
//                    view.requestLayout();
//
//                }
//            });
//            valueAnimators.add(changeBoundsAnimator);
//          //  allAnimators.add(changeBoundsAnimator);
//        }
//        return valueAnimators;
//    }
}
