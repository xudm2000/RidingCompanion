package com.example.ridingcompanion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;

public class Riding extends AppCompatActivity {

    private GoogleMap mMap;
    private MapView mapView;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riding);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        mapView = (MapView) findViewById(R.id.fragment_map_riding);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        try {
            MapsInitializer.initialize(this.getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mapView.getMapAsync(googleMap -> {
            mMap = googleMap;
            int permission = ActivityCompat.checkSelfPermission(this.getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION);
            if (permission == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            } else {
                mMap.setMyLocationEnabled(true);
            }
        });
    }

//    public void moreInfo(View view){
//        Intent intent = new Intent(this, RidingInformation.class);
//        startActivity(intent);
//    }

    public void stop(View view){
        Intent intent = new Intent(this, Result.class);
        startActivity(intent);
    }
}