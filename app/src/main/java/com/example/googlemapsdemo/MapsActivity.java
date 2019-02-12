package com.example.googlemapsdemo;

import android.Manifest;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

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
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;

import java.util.Arrays;

public class MapsActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        GoogleMap.OnPolylineClickListener,
        GoogleMap.OnPolygonClickListener,
        GoogleMap.OnCircleClickListener,
        GoogleMap.OnPoiClickListener,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMapLongClickListener,
        GoogleMap.OnCameraMoveStartedListener,
        GoogleMap.OnCameraMoveListener,
        GoogleMap.OnCameraMoveCanceledListener,
        GoogleMap.OnCameraIdleListener,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener
{
    private static final int PERMISSION_REQUEST_CODE = 101;
    private static final String[] PERMISSIONS = { Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION };

    private static final String[] MAP_TYPES = { "None", "Normal", "Satellite", "Terrain", "Hybrid" };

    private Toast toast;
    private GoogleMap googleMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);

        requestPermissions(PERMISSIONS);
        initMapFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.item_select_map_type) {
            showMapTypeSelectionDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;

        googleMap.setOnMapClickListener(this);
        googleMap.setOnMapLongClickListener(this);
        googleMap.setOnPoiClickListener(this);

        googleMap.setOnMyLocationButtonClickListener(this);
        googleMap.setOnMyLocationClickListener(this);

        googleMap.setOnCameraIdleListener(this);
        googleMap.setOnCameraMoveStartedListener(this);
        googleMap.setOnCameraMoveListener(this);
        googleMap.setOnCameraMoveCanceledListener(this);

        applyMapStyle(googleMap);
        setMapPadding(googleMap);
        enableMyLocationButton(googleMap);

        showLocation(googleMap);
        showPolyline(googleMap);
        showPolygon(googleMap);
        showCircle(googleMap);
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        showToast(String.format("Map click, location = %s", latLng.toString()));
    }

    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {
        showToast(String.format("Map long click, location = %s", latLng.toString()));
    }

    @Override
    public void onPoiClick(@NonNull PointOfInterest pointOfInterest) {
        String message =
                "Name = " + pointOfInterest.name + "\n" +
                        "PlaceId = " + pointOfInterest.placeId + "\n" +
                        "Coordinates = " + pointOfInterest.latLng.toString();

        showToast(message);
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
    public void onCircleClick(@NonNull Circle circle) {
        int fillColor = circle.getFillColor();
        int strokeColor = circle.getStrokeColor();
        circle.setFillColor(strokeColor);
        circle.setStrokeColor(fillColor);
    }

    @Override
    public boolean onMyLocationButtonClick() {
        showToast("onMyLocationButtonClick");
        return true;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        showToast(String.format("onMyLocationClick, %s", location.toString()));
    }

    @Override
    public void onCameraMoveStarted(int reason) {
        String message = null;

        switch (reason) {
            case GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE: {
                message = "The user gestured on the map.";
                break;
            }
            case GoogleMap.OnCameraMoveStartedListener.REASON_API_ANIMATION: {
                message = "The user tapped something on the map.";
                break;
            }
            case GoogleMap.OnCameraMoveStartedListener.REASON_DEVELOPER_ANIMATION: {
                message = "The app moved the camera.";
                break;
            }
            default: {
                break;
            }
        }

        if (message != null) {
            showToast(message);
        }
    }

    @Override
    public void onCameraMove() {
        //showToast("onCameraMove");
    }

    @Override
    public void onCameraMoveCanceled() {
        showToast("onCameraMoveCanceled");
    }

    @Override
    public void onCameraIdle() {
        showToast("onCameraIdle");
    }


    private void requestPermissions(@NonNull String[] permissions) {
        PermissionsUtil.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
    }

    private void initMapFragment() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    private void showMapTypeSelectionDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Map type")
                .setItems(MAP_TYPES, (dialog, which) -> setMapType(googleMap, which))
                .create()
                .show();
    }

    private void showToast(@NonNull String message) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.show();
    }


    private void enableMyLocationButton(@NonNull GoogleMap googleMap) {
        try {
            googleMap.setMyLocationEnabled(true);
        } catch (SecurityException ex) {
            ex.printStackTrace();
        }
    }

    private void setMapPadding(@NonNull GoogleMap googleMap) {
        googleMap.setPadding(0, 0, 100, 100);
    }

    private void setMapType(@NonNull GoogleMap googleMap, int mapType) {
        googleMap.setMapType(mapType);
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


    private void applyMapStyle(@NonNull GoogleMap googleMap) {
        MapStyleOptions mapStyleOptions = MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style);
        googleMap.setMapStyle(mapStyleOptions);
    }
}
