package com.example.amoa1000.mapsthing;

import android.*;
import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private int switchtheviewofthemap = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

        // Add a marker in Sydney and move the camera
        LatLng NCarolina = new LatLng(36, -81);
        mMap.addMarker(new MarkerOptions().position(NCarolina).title("potentially born here"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(NCarolina));



        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            Log.d("MyMapApp", "failed permission check 1");
            Log.d("MyMapApp", Integer.toString(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)));
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},2);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            Log.d("MyMapApp", "failed permission check 2");
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},2);
        }

        mMap.setMyLocationEnabled(true);

    }

    public void switchViews(View v){
        if(switchtheviewofthemap == 0){
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            switchtheviewofthemap = 1;
        }
        else if(switchtheviewofthemap == 1){
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            switchtheviewofthemap = 2;

        }
        else if(switchtheviewofthemap == 2){
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            switchtheviewofthemap = 3;

        }
        else if(switchtheviewofthemap == 3) {
            mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            switchtheviewofthemap = 0;

        }
    }
}
