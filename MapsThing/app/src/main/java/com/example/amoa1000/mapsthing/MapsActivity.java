package com.example.amoa1000.mapsthing;

import android.*;
import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.identity.intents.Address;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private int switchtheviewofthemap = 0;
    private int track = 0;
    private LocationManager locationManager;
    private boolean isGPSenabled = false;
    private boolean isNetworkEnabled = false;
    private boolean canGetLocation;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 15 * 1;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 5;
    private Location myLocation;
    private LatLng userlocation = null;
    private static final int MY_LOC_ZOOM_FACTOR = 15;
    EditText Search;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        Search = (EditText) findViewById(R.id.editText_search);


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


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("myMaps", "failed permission check 1");
            Log.d("myMaps", Integer.toString(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)));
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 2);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("myMaps", "failed permission check 2");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 2);
        }

        mMap.setMyLocationEnabled(true);

    }

    public void switchViews(View v) {
        if (switchtheviewofthemap == 0) {
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            switchtheviewofthemap = 1;
        } else if (switchtheviewofthemap == 1) {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            switchtheviewofthemap = 0;

        }
    }

    public void search(View v){
        Log.d("search","atleast the button works");
        Geocoder POI = new Geocoder(this, Locale.getDefault());
        Log.d("search","innitiated the geocoder thing");
        String whatAmIsearchingFor = Search.getText().toString();
        Log.d("search","made the text from the search to a string");
        try {

            List<android.location.Address> life = POI.getFromLocationName(whatAmIsearchingFor,10);

            for(android.location.Address locations: life){

                LatLng NewLocation = new LatLng(locations.getLongitude(),locations.getLatitude());

                Marker marker = mMap.addMarker(new MarkerOptions().position(NewLocation).title(whatAmIsearchingFor));

                Log.d("myMaps","added a location for what you searched for");
            }


        } catch (IOException e) {
            Log.d("myMaps","problem with the search function");
        }


    }





    public void toggleTracking(View v){
        if(track == 0){
            getLocation();
            track = 1;
        }else if(track == 1){
            StopGettingLocation();
            track = 0;
        }
    }

    public void clearMarkers(View v){
        mMap.clear();
    }

    public void StopGettingLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.removeUpdates(locationListenerGps);
        locationManager.removeUpdates(locationListenerNetwork);
        locationManager = null;

    }

    public void getLocation() {

        try {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            //get gps status
            isGPSenabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (isGPSenabled) Log.d("myMaps", "gps is enabled, probably ");

            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (isNetworkEnabled) Log.d("myMaps", "network is enabled, probably ");

            if (!isGPSenabled && !isNetworkEnabled) {
                Log.d("myMaps", "no provider mate");
            } else {
                this.canGetLocation = true;


                if (isNetworkEnabled) {
                    Log.d("myMaps", "getLocation for network is enabled bro, you should go request location updates");
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES,
                            locationListenerNetwork);

                    Log.d("myMaps", "the getlocation thing has done stuff. specifically the network update request was successful");
                    Toast.makeText(this, "using Network", Toast.LENGTH_SHORT).show();
                }
                if (isGPSenabled) {
                    Log.d("myMaps", "getLocation for GPS is enabled bro, you should go request location updates");
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES,
                            locationListenerGps);

                    Log.d("myMaps", "the getlocation thing has done stuff. specifically the GPS update request was successful");
                    Toast.makeText(this, "using GPS", Toast.LENGTH_SHORT).show();

                }


            }


        } catch (Exception e) {
            Log.d("myMaps", "god danmit getLocation");
            e.printStackTrace();
        }
    }

    android.location.LocationListener locationListenerNetwork = new android.location.LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            dropNetworkMarker(location.getProvider());
            try{
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListenerNetwork);
            }
            catch(SecurityException e) {

            }

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d("Status","GPS is enabled and working");
        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };
    android.location.LocationListener locationListenerGps = new android.location.LocationListener() {

        @Override
        public void onLocationChanged(Location location) {

            dropGPSMarker(location.getProvider());

            try{
                locationManager.removeUpdates(locationListenerNetwork);
            } catch(SecurityException e){

            }

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

            switch(status){
                case LocationProvider.AVAILABLE:
                    Log.d("Status", "Location Provider is Available");
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    Log.d("Status", "Location Provider isn't available");
                    try {
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListenerNetwork);
                    }catch(SecurityException e){

                    }
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.d("Status","Location Provider is temporarily unavailable");
                    break;
                default:
                    try{
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListenerNetwork);
                    }catch(SecurityException e){

                    }
            }
        }
        @Override
        public void onProviderEnabled(String provider) {
        }
        @Override
        public void onProviderDisabled(String provider) {
        }
    };

    public void dropGPSMarker(String provider){
        if(locationManager!=null){
            try{
                myLocation = locationManager.getLastKnownLocation(provider);
            }catch(SecurityException e){
            }

        }
        if(myLocation == null){
            //display a message
        }
        else{
            userlocation = new LatLng(myLocation.getLatitude(),myLocation.getLongitude());
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(userlocation, MY_LOC_ZOOM_FACTOR);
            Circle marker = mMap.addCircle((new CircleOptions().center(userlocation)).radius(1).strokeColor(Color.GREEN).fillColor(Color.GREEN));
            mMap.animateCamera(update);
        }

    }

    public void dropNetworkMarker(String provider) {
        if (locationManager != null) {
            try {
                myLocation = locationManager.getLastKnownLocation(provider);
            } catch (SecurityException e) {
            }

        }
        if (myLocation == null) {
            //display a message
        } else {
            userlocation = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(userlocation, MY_LOC_ZOOM_FACTOR);
            Circle marker = mMap.addCircle((new CircleOptions().center(userlocation)).radius(1).strokeColor(Color.RED).fillColor(Color.RED));
            mMap.animateCamera(update);
        }
    }











    /*public void dropMarker(String provider) {

        Log.d("message", provider);

        if (locationManager != null) {


        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        myLocation = locationManager.getLastKnownLocation(provider);

        if(myLocation == null) {
            //display message
        } else {

            //get user location
            userlocation = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());

            //add marker
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(userlocation, MY_LOC_ZOOM_FACTOR);

            //drop the marker
            Circle circle = mMap.addCircle(new CircleOptions().center(userlocation).radius(1).strokeColor(Color.GREEN).strokeWidth(2).fillColor(Color.MAGENTA));

            mMap.animateCamera(update);

        }

    }*/

}
