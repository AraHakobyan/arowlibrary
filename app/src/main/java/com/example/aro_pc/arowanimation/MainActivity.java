package com.example.aro_pc.arowanimation;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.animationhelperlibrary.animation.for_road.ColorAnimation;
import com.example.animationhelperlibrary.animation.for_road.DrawPolylineAnimation;
import com.example.animationhelperlibrary.animation.hide_view.HideAnimation;
import com.example.animationhelperlibrary.roadhelper.RoadHelper;
import com.example.animationhelperlibrary.roadhelper.RoadInfo;
import com.example.animationhelperlibrary.roadhelper.RoadIsReadyListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import static com.example.aro_pc.arowanimation.Consts.ABOVYAN_KINO;
import static com.example.aro_pc.arowanimation.Consts.CHARENTSAVAN_KINO;
import static com.example.aro_pc.arowanimation.Consts.YEREVAN_KENTRON;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, RoadIsReadyListener, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener, View.OnTouchListener, GoogleMap.OnCameraMoveListener, GoogleMap.OnMapLoadedCallback {

    private MapView mapView;
    private GoogleMap googleMap;
    private RoadInfo roadInfo;
    private Polyline line;
    private AnimatorSet animatorSet;
    private RelativeLayout relativeLayout;
    private FloatingActionButton fab;
    private Toolbar myToolbar;
    private View moveLayout;
    private int mY = 0;
    private float mCoordinateY = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        relativeLayout = (RelativeLayout) findViewById(R.id.layout_id_1);
        moveLayout = findViewById(R.id.move_view_id);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        moveLayout.setOnTouchListener(this);
        mapView = (MapView) findViewById(R.id.map_view);
        mapView.onCreate(null);
        mapView.onResume();
        mapView.getMapAsync(this);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        this.googleMap = googleMap;
        googleMap.setOnMapLongClickListener(this);
        googleMap.setOnMapClickListener(this);
        googleMap.setOnMapLoadedCallback(this);
        line = googleMap.addPolyline(new PolylineOptions()
                .width(15)
                .color(Color.BLACK));
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(Consts.ABOVYAN_KINO, 10, 10, 10)));

        RoadHelper.getInstance().setPoints(YEREVAN_KENTRON, ABOVYAN_KINO, CHARENTSAVAN_KINO).addRoadIsReadyListener(this);


    }

    @Override
    public void getRoad(RoadInfo roadInfo) {
        this.roadInfo = roadInfo;
        line.setPoints(roadInfo.getRoadPoints());
        ColorAnimation colorAnimation = new ColorAnimation();
        colorAnimation.addPolyline(line);
        ValueAnimator colorAnimator = colorAnimation.getStartColorAnim();
        DrawPolylineAnimation drawPolylineAnimation = new DrawPolylineAnimation();
        drawPolylineAnimation.setPolyline(line);
        ValueAnimator drawAnim = drawPolylineAnimation.getDrawAnim();
        if (animatorSet == null) {
            animatorSet = new AnimatorSet();
        } else {
            animatorSet.removeAllListeners();
            animatorSet.end();
            animatorSet.cancel();

            animatorSet = new AnimatorSet();
        }
        animatorSet.playSequentially(drawAnim, colorAnimator);
        animatorSet.start();

    }

    private HideAnimation hideAnimation;

    @Override
    public void onMapClick(LatLng latLng) {
        RoadHelper.getInstance().addPoint(latLng);

    }


    @Override
    public void onMapLongClick(LatLng latLng) {
        googleMap.clear();
        line = googleMap.addPolyline(new PolylineOptions()
                .width(15)
                .color(Color.BLACK));
        RoadHelper.getInstance().clear();
        RoadHelper.getInstance().setPoints(YEREVAN_KENTRON, ABOVYAN_KINO, CHARENTSAVAN_KINO).addRoadIsReadyListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(mY == 0){
            mY = moveLayout.getHeight();
            mCoordinateY = moveLayout.getY();
        }
        int id = v.getId();
        int Y = (int) event.getRawY() ;
        switch (id) {
            case R.id.move_view_id:

                moveLayout.setBackgroundColor(Color.RED);
                break;
        }
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_POINTER_DOWN:

                break;
            case MotionEvent.ACTION_POINTER_UP:

                break;
            case MotionEvent.ACTION_MOVE:
                float deltaY = Y - moveLayout.getY();
                Log.d("fab","fab is ACTION_MOVE" + moveLayout.getHeight()) ;
                moveLayout.getLayoutParams().height = (int) (moveLayout.getHeight()+Y);
                moveLayout.requestLayout();
                mY = mY + 100;

                break;
        }


        return true;
    }

    @Override
    public void onCameraMove() {
        hideAnimation.startAll();

    }

    @Override
    public void onMapLoaded() {
        hideAnimation = new HideAnimation(getWindowManager().getDefaultDisplay(), relativeLayout, fab, myToolbar);
        googleMap.setOnCameraMoveListener(this);

    }
}
