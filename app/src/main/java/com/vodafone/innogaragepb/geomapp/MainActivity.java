package com.vodafone.innogaragepb.geomapp;


import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public Boolean ready;
    public Marker myMarker;
    public LatLng myLatLng;
    public LatLng myLatLng2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        final Button accidentButton = (Button) findViewById(R.id.accidentButton);
        final Button trafficjamButton = (Button) findViewById(R.id.trafficjamButton);
        final Button speedlimitButton = (Button) findViewById(R.id.speedlimitButton);
        //Initializing the buttons
        accidentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Place a marker with location and time to live
                setSituation(5000, myLatLng, myMarker, "accident");
            }
        });

        trafficjamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Sendtraffic icon
                setSituation(10000, myLatLng2, myMarker, "trafficjam");
            }
        });

        speedlimitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Sendtraffic icon
                setLocation(5000, myLatLng2, myMarker, "pink");
            }
        });

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        myLatLng = new LatLng(-34, 151);
        myLatLng2 = new LatLng(-34.05, 151);
        // Add a marker and move the camera
        //Create camera  position object
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(0, 0))
                .bearing(45)
                .tilt(90)
                .zoom(googleMap.getCameraPosition().zoom)
                .build();
        mMap.moveCamera(CameraUpdateFactory.newLatLng(myLatLng));
        // mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        ready = true;
    }

//SET USE CASES: Situation in the traffic or just location of the other cars
    public void setSituation(long duration, LatLng myLatLng, Marker marker, String myString) {
        final String code = myString;
        if (ready) {
                marker = mMap.addMarker(new MarkerOptions()
                        .position(myLatLng)
                        .icon(BitmapDescriptorFactory.fromBitmap(resizer(code, 70, 70))));
                fadeTime(duration, marker);
            }
        }

    public void setLocation (long duration, LatLng myLatLng, Marker marker, String cellColor){
        if (ready) {
            int id = getResources().getIdentifier(cellColor, "drawable", getPackageName());
            marker = mMap.addMarker(new MarkerOptions()
                    .position(myLatLng)
                    .icon(BitmapDescriptorFactory.fromResource(id)));
            fadeTime(duration, marker);
        }
    }




//Customize characteristics of the markers: Size and time to fade

    public Bitmap resizer(String iconName, int width, int height) {
        Bitmap imgBitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(iconName, "drawable", getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imgBitmap, width, height, false);
        return resizedBitmap;
    }

    public void fadeTime(long duration, Marker marker) {
        final Marker myMarker = marker;
        final LinearInterpolator inter = new LinearInterpolator();
        ValueAnimator myAnim = ValueAnimator.ofFloat(1, 0);
        myAnim.setDuration(duration);
        myAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                myMarker.setAlpha((float) animation.getAnimatedValue());
            }
        });
        myAnim.start();
    }
}

//http://stackoverflow.com/questions/28109597/gradually-fade-out-a-custom-map-marker