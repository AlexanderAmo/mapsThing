package com.example.amoa1000.mapsthing;

import android.*;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private int switchtheviewofthemap = 0;
    private LocationManager locationManager;
    private boolean isGPSenabled = false;
    private  boolean isNetworkEnabled = false;
    private boolean canGetLocation;
    private static final long MIN_TIME_BW_UPDATES = 1000*15*1;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 5;

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
            Log.d("myMaps", "failed permission check 1");
            Log.d("myMaps", Integer.toString(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)));
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},2);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            Log.d("myMaps", "failed permission check 2");
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},2);
        }

        mMap.setMyLocationEnabled(true);

    }

    public void switchViews(View v){
        if(switchtheviewofthemap == 0){
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            switchtheviewofthemap = 1;
        }
        else if(switchtheviewofthemap == 1) {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            switchtheviewofthemap = 0;

        }
    }
    public void trackMeBB(View v){

    }

    public void getLocation(){

        try {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            //get gps status
            isGPSenabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if(isGPSenabled) Log.d("myMaps", "gps is enabled, probably ");

            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if(isNetworkEnabled) Log.d("myMaps", "network is enabled, probably ");

            if(!isGPSenabled && !isNetworkEnabled){
                Log.d("myMaps","no provider mate");
            } else{
                this.canGetLocation = true;
                if(isNetworkEnabled){
                    Log.d("myMaps","getLocation for network is enabled bro, you should go request location updates");
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES,
                            locationListenerNetwork);

                    Log.d("myMaps", "the getlocation thing has done stuff. specifically the network update request was successful");
                    Toast.makeText(this,"using Network",Toast.LENGTH_SHORT);
                }
                if(isGPSenabled){
                    Log.d("myMaps","getLocation for GPS is enabled bro, you should go request location updates");
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES,
                            locationListenerGPS);

                    Log.d("myMaps", "the getlocation thing has done stuff. specifically the GPS update request was successful");
                    Toast.makeText(this,"using GPS",Toast.LENGTH_SHORT);
                }


            }



        }catch (Exception e){
            Log.d("myMaps", "god danmit getLocation");
            e.printStackTrace();
        }
    }

}
