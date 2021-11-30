package com.example.ridingcompanion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.LinkedList;

public class Riding extends AppCompatActivity {

    private LocationManager locationManager;
    private LocationListener locationListener;
    private FusedLocationProviderClient fusedLocationProviderClient;

    private GoogleMap mMap;
    private MapView mapView;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 12;
    private TextView speed;
    private TextView time;
    private TextView distance;
    private TextView calories;
    private boolean isCancel = false;
    private double startLat;
    private double startLong;
    private double totalDistance;
    private double totalCalories;
    private final double COEFFICIENT = 62.15;
    private boolean isStop = true;

    private Switch musicSwitch;
    private MediaPlayer mediaPlayer;

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
                            time.setText(String.valueOf(finalT)+" s");
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

    public class MusicPlay implements Runnable{
        @Override
        public void run() {
            String url = "https://win-web-rh01-sycdn.kuwo.cn/ffb45d001a94a6aa42b95131582e98b3/619ece5c/resource/n1/32/98/4007603884.mp3";
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

            try {
                mediaPlayer.setDataSource(url);
                mediaPlayer.prepare(); // might take long! (for buffering, etc)
            } catch (IOException e) {
                e.printStackTrace();
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    musicSwitch.setEnabled(true);
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riding);

        mapView = (MapView) findViewById(R.id.fragment_map_riding);
        time = (TextView) findViewById(R.id.timeShow);
        distance = (TextView) findViewById(R.id.total_distance);
        calories = (TextView) findViewById(R.id.calories);

        mediaPlayer = new MediaPlayer();

        musicSwitch = (Switch) findViewById(R.id.switch1);
        musicSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(musicSwitch.isChecked()){
                    musicSwitch.setText("Music On");
                    mediaPlayer.start();
                }else{
                    musicSwitch.setText("Music Off");
                    mediaPlayer.pause();
                }
            }
        });
        musicSwitch.setEnabled(false);

        MusicPlay musicRunnable = new MusicPlay();
        new Thread(musicRunnable).start();

        totalDistance = 0;
        totalCalories = 0;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                updateLocationInfo(location);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {

            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {

            }
        };

        if(Build.VERSION.SDK_INT < 23){
            startListening();
        }else{
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }else{
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if(location != null){
                    updateLocationInfo(location);
                    totalDistance = 0.0;
                    distance.setText(String.valueOf(totalDistance));
                }
            }
        }

        time.setText("0 s");
        speed.setText("0 m/s");
        int permission = ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION);

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
        intent.putExtra("distance", totalDistance);
        intent.putExtra("calories", totalCalories);
        double totalTime = Double.parseDouble(time.getText().toString().substring(0, time.getText().toString().length()-2));
        intent.putExtra("time", totalTime);
        intent.putExtra("avg_speed", totalDistance*1000/totalTime);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startListening();
        }
    }

    public void updateLocationInfo(Location location){
        float speed_location = location.getSpeed();

        float[] results = new float[3];
        Location.distanceBetween(startLat, startLong, location.getLatitude(), location.getLongitude(), results);
        if(results[0]/1000.0 < 0.00009){
            if(isStop) {
                speed_location = 0;
            }
            isStop = true;
        }else{
            isStop = false;
        }
        totalDistance += results[0]/1000.0;
        distance.setText(String.format("%.2f km", totalDistance));
        startLat = location.getLatitude();
        startLong = location.getLongitude();
        totalCalories = totalDistance*COEFFICIENT;
        String cal = String.format("%.2f cal", totalDistance*COEFFICIENT);
        calories.setText(cal);
        speed = (TextView) findViewById(R.id.speed);
        speed.setText(String.format("%.2f m/s", speed_location));

        int permission = ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION);
        mapView.getMapAsync(googleMap -> {
            mMap = googleMap;
            if (permission == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            } else {
                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
                mMap.moveCamera(CameraUpdateFactory.zoomTo(15.0f));
            }
        });
    }

    public void startListening(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        }
    }
}