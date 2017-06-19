package com.example.aro_pc.arowanimation;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.animationhelperlibrary.ColorAnimation;
import com.example.animationhelperlibrary.DrawPolylineAnimation;
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

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, RoadIsReadyListener, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {

    private MapView mapView;
    private GoogleMap googleMap;
    private RoadInfo roadInfo;
    private Polyline line;
    private AnimatorSet animatorSet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
}
