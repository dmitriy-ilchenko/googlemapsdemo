package com.example.googlemapsdemo;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;

import java.util.Arrays;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnPolylineClickListener, GoogleMap.OnPolygonClickListener, GoogleMap.OnCircleClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        initMapFragment();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        showLocation(googleMap);
        showPolyline(googleMap);
        showPolygon(googleMap);
        showCircle(googleMap);
    }

    @Override
    public void onPolylineClick(@NonNull Polyline polyline) {
        if (polyline.getPattern() != null) {
            polyline.setPattern(null);
        } else {
            polyline.setPattern(Arrays.asList(new Gap(8), new Dot()));
        }
    }

    @Override
    public void onPolygonClick(@NonNull Polygon polygon) {
        int fillColor = polygon.getFillColor();
        int strokeColor = polygon.getStrokeColor();
        polygon.setFillColor(strokeColor);
        polygon.setStrokeColor(fillColor);
    }

    @Override
    public void onCircleClick(Circle circle) {
        int fillColor = circle.getFillColor();
        int strokeColor = circle.getStrokeColor();
        circle.setFillColor(strokeColor);
        circle.setStrokeColor(fillColor);
    }


    private void initMapFragment() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    private void showLocation(@NonNull GoogleMap googleMap) {
        LatLng sydney = new LatLng(-34, 151);
        googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private void showPolyline(@NonNull GoogleMap googleMap) {
        LatLng[] polylineVertexes = {
                new LatLng(-35.016, 143.321),
                new LatLng(-34.747, 145.592),
                new LatLng(-34.364, 147.891),
                new LatLng(-33.501, 150.217),
                new LatLng(-32.306, 149.248),
                new LatLng(-32.491, 147.309)
        };

        PolylineOptions polylineOptions = new PolylineOptions()
                .clickable(true)
                .add(polylineVertexes);

        Polyline polyline = googleMap.addPolyline(polylineOptions);
        polyline.setStartCap(new RoundCap());
        polyline.setEndCap(new RoundCap());
        polyline.setWidth(12);
        polyline.setColor(0xFFD81B60);
        polyline.setJointType(JointType.ROUND);

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-23.684, 133.903), 4));

        googleMap.setOnPolylineClickListener(this);
    }

    private void showPolygon(@NonNull GoogleMap googleMap) {
        LatLng[] polygonVertexes = {
                new LatLng(-27.457, 153.040),
                new LatLng(-33.852, 151.211),
                new LatLng(-37.813, 144.962),
                new LatLng(-34.928, 138.599)
        };

        PolygonOptions polygonOptions = new PolygonOptions()
                .clickable(true)
                .add(polygonVertexes);

        Polygon polygon = googleMap.addPolygon(polygonOptions);
        polygon.setStrokePattern(Arrays.asList(new Dot(), new Gap(8),  new Dash(6), new Gap(8)));
        polygon.setStrokeWidth(9);
        polygon.setStrokeColor(0xffF57F17);
        polygon.setFillColor(0xffF9A825);

        googleMap.setOnPolygonClickListener(this);
    }

    private void showCircle(@NonNull GoogleMap googleMap) {
        CircleOptions circleOptions = new CircleOptions()
                .clickable(true)
                .center(new LatLng(-33.87365, 151.20689))
                .radius(1000000)
                .strokeColor(Color.RED)
                .fillColor(Color.BLUE);

        googleMap.addCircle(circleOptions);

        googleMap.setOnCircleClickListener(this);
    }
}
