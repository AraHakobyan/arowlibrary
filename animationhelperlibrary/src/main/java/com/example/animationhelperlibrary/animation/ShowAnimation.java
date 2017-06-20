package com.example.animationhelperlibrary.animation;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.Display;
import android.view.View;

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

    public ShowAnimation(Display display, View... views) {

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

    private void stopAll(){
        if (animatorSet != null) {
            animatorSet.removeAllListeners();
            animatorSet.end();
            animatorSet.cancel();
        }

    }

    public void startAll() {

        animatorSet = new AnimatorSet();
        animatorSet.playTogether(allAnimators);
        animatorSet.start();
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

                ViewStartPos.getInstance().setCanAnimate(true);
            }

            @Override
            public void onAnimationEnd(Animator animation) {

                ViewStartPos.getInstance().clear();

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });


    }

    private void  toDownAnimation(final View view, ViewProperty viewProperty) {

        final float mY = view.getY();

        ValueAnimator toDownAnimator = ValueAnimator.ofFloat(mY, viewProperty.getmY());
        toDownAnimator.setDuration(1500);
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

        ValueAnimator toUpAnimator = ValueAnimator.ofFloat(mY, viewProperty.getmY());
        toUpAnimator.setDuration(1500);
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
            changeAlfaAnimator.setDuration(1500);
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

    private ArrayList<ValueAnimator> changeBoundsAnimation(ArrayList<View> viewArrayList, int mDisplayHeight, int mDisplayWidth) {
        ArrayList<ValueAnimator> valueAnimators = new ArrayList<>();
        for (final View view : viewArrayList) {

            final int mHeight = view.getHeight();
            final int mWidth = view.getWidth();
            ValueAnimator changeBoundsAnimator = ValueAnimator.ofInt(0, 100);
            changeBoundsAnimator.setDuration(1500);
            changeBoundsAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int delta = 100 - (int) animation.getAnimatedValue();

                    view.getLayoutParams().height = mHeight * delta / 100;
                    view.getLayoutParams().width = mWidth * delta / 100;
                    view.requestLayout();

                }
            });
            valueAnimators.add(changeBoundsAnimator);
            allAnimators.add(changeBoundsAnimator);
        }
        return valueAnimators;
    }
}