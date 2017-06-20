package com.example.animationhelperlibrary.animation;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.view.Display;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by Aro-PC on 6/20/2017.
 */

public class HideAnimation {

    CanShowCallback canShowCallback = new CanShowCallback() {
        @Override
        public void canShowCallback(boolean isCanShow) {
            if(isCanShow) showAnimation.startAll();
        }
    };
    private ArrayList<ValueAnimator> changeBoundsAnimators;
    private ArrayList<ValueAnimator> changeAlfaAnimators;
    private ArrayList<ValueAnimator> animateToUp;
    private ArrayList<ValueAnimator> animateToDown;
    private int viewsLengts = 0;
    private int mDisplayHeight;
    private int mDisplayWidth;
    private ArrayList<Animator> allAnimators;
    private AnimatorSet animatorSet;
    private ShowAnimation showAnimation;


    private final Handler handler = new Handler();
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            canShowCallback.canShowCallback(true);
            ViewStartPos.getInstance().setCanAnimate(true);
        }
    };



    public HideAnimation(Display display, View... views) {
        stopAll();

        allAnimators = new ArrayList<>();
        ArrayList<View> viewArrayList = new ArrayList<>();
        viewsLengts = views.length;
        for (int i = 0; i < views.length; i++) {
            viewArrayList.add(views[i]);
        }
        if(ViewStartPos.getInstance().getViewProperties() == null)
        ViewStartPos.getInstance().setProperty(viewArrayList);
        prepeareToHide(display,viewArrayList);
        showAnimation = new ShowAnimation(display,views);




    }

    private void prepeareToHide(Display display, ArrayList<View> viewArrayList) {
        mDisplayHeight = display.getHeight();
        mDisplayWidth = display.getWidth();
        //changeBoundsAnimators = changeBoundsAnimation(viewArrayList, mDisplayHeight, mDisplayWidth);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            changeAlfaAnimators = changeAlfaAnimator(viewArrayList, mDisplayHeight, mDisplayWidth);
        }


        for (View view : viewArrayList){
            //if view center is above of display center
            if(view.getY() + view.getHeight() / 2 < mDisplayHeight / 2){
                toUpAnimation(view, mDisplayHeight, mDisplayWidth);

            } else {
                toDownAnimation(view, mDisplayHeight, mDisplayWidth);

            }
        }
    }

    public void startAll() {

        showAnimation.stopAll();

        if(ViewStartPos.getInstance().isCanAnimate()){
            animatorSet = new AnimatorSet();
            animatorSet.playTogether(allAnimators);

            animatorSet.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    handler.removeCallbacks(runnable);
                    ViewStartPos.getInstance().setCanAnimate(false);
                    handler.postDelayed(runnable,3000);
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
        } else {
            handler.removeCallbacks(runnable);
            handler.postDelayed(runnable,3000);

        }





    }

    private void stopAll(){
        if (animatorSet != null) {
            animatorSet.removeAllListeners();
            animatorSet.end();
            animatorSet.cancel();
        }

    }

    private void  toDownAnimation(final View view, int mDisplayHeight, int mDisplayWidth) {

            final float mY = view.getY();

            ValueAnimator toDownAnimator = ValueAnimator.ofFloat(mY, mDisplayHeight );
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

    private void toUpAnimation(final View view, int mDisplayHeight, int mDisplayWidth) {


            final float mY = view.getY();

            ValueAnimator toUpAnimator = ValueAnimator.ofFloat(mY, -210);
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

            ValueAnimator changeAlfaAnimator = ValueAnimator.ofInt(254, 1);
            changeAlfaAnimator.setDuration(1500);
            changeAlfaAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int delta = (int) animation.getAnimatedValue();

                    view.getBackground().setAlpha(delta);
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
