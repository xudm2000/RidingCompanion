package com.example.ridingcompanion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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
    private TextView time;
    private TextView distance;
    private TextView calories;
    private TextView speed;
    private boolean isCancel = false;

    private class RidingRunnable implements Runnable{
        @Override
        public void run() {
            try {
                int t = 0;
                while(!isCancel){
                    int finalT = t;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            time.setText(String.valueOf(finalT));

                        }
                    });
                    Thread.sleep(1000);
                    t++;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riding);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        mapView = (MapView) findViewById(R.id.fragment_map_riding);
        time = (TextView) findViewById(R.id.timeShow);
        speed = (TextView) findViewById(R.id.speed);
        distance = (TextView) findViewById(R.id.distance);
        calories = (TextView) findViewById(R.id.calories);

        time.setText("0");

        int permission = ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }else{
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(this, task -> {
                Location myLocation = task.getResult();
                if(task.isSuccessful() && myLocation != null){
                    float speed_location = myLocation.getSpeed();
                    speed.setText(String.valueOf(speed_location));
                }
            });
        }

        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        try {
            MapsInitializer.initialize(this.getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mapView.getMapAsync(googleMap -> {
            mMap = googleMap;
            if (permission == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            } else {
                mMap.setMyLocationEnabled(true);
            }
        });

        RidingRunnable runnable = new RidingRunnable();
        new Thread(runnable).start();
    }

    public void stop(View view){
        Intent intent = new Intent(this, Result.class);
        startActivity(intent);
    }
}