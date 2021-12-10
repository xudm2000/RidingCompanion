package com.example.ridingcompanion;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Base64;

public class HistoryPage extends AppCompatActivity {

    private Button back;
    private TextView dateView;
    private TextView speedView;
    private TextView distanceView;
    private TextView timeView;
    private TextView caloriesView;
    private ImageView imageView;

    private GoogleMap mMap;
    private MapView mapView;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_page);

        back = (Button) findViewById(R.id.backButton);
        dateView = (TextView) findViewById(R.id.dateInfo);
        speedView = (TextView) findViewById(R.id.speedInfo);
        timeView = (TextView) findViewById(R.id.timeInfo);
        distanceView = (TextView) findViewById(R.id.distanceInfo);
        caloriesView = (TextView) findViewById(R.id.caleriesInfo);
        imageView = (ImageView) findViewById(R.id.imageView3);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        imageView.setMinimumWidth(width/2);
        imageView.setMinimumHeight(height/3);

        Intent intent = getIntent();
        String date = intent.getStringExtra("date");
        String speed = intent.getStringExtra("speed");
        String distance = intent.getStringExtra("distance");
        String time = intent.getStringExtra("time");
        String calories = intent.getStringExtra("calories");
        String image = intent.getStringExtra("image");
        String route = intent.getStringExtra("route");

        ArrayList<Double> lats_positions = new ArrayList<>();
        ArrayList<Double> longs_positions = new ArrayList<>();

        String[] locations = route.split("\\|");
        for(String s : locations){
            if(!s.equals("")){
                lats_positions.add(Double.parseDouble(s.split(",")[0]));
                longs_positions.add(Double.parseDouble(s.split(",")[1]));
            }
        }

        mapView = (MapView) findViewById(R.id.fragment_map_riding3);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        try {
            MapsInitializer.initialize(this.getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mapView.getMapAsync(googleMap -> {
            mMap = googleMap;
            if(lats_positions != null && lats_positions.size() != 0 && longs_positions != null && longs_positions.size() != 0) {
                double prev_lat = lats_positions.get(0);
                double prev_long = longs_positions.get(0);

                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(new LatLng(lats_positions.get(0), longs_positions.get(0)));
                for (int i = 1; i < lats_positions.size(); i++) {
                    mMap.addPolyline(new PolylineOptions().add(new LatLng(lats_positions.get(i), longs_positions.get(i)), new LatLng(prev_lat, prev_long)).color(R.color.design_default_color_error));
                    builder.include(new LatLng(lats_positions.get(i), longs_positions.get(i)));
                    prev_lat = lats_positions.get(i);
                    prev_long = longs_positions.get(i);
                }
                LatLngBounds bounds = builder.build();
                int padding = 100; // offset from edges of the map in pixels
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

                mMap.moveCamera(cu);
            }
        });

        dateView.setText(date);
        speedView.setText(String.format("%.2f m/s", Double.parseDouble(speed)));
        distanceView.setText(String.format("%.2f km", Double.parseDouble(distance)));
        int sec = (int) Double.parseDouble(time);
        int min = sec / 60;
        sec = sec - min * 60;
        int hour = min / 60;
        min = min - hour * 60;

        if(hour > 0){
            timeView.setText(String.format("%d hr %d min %d sec", hour, min, sec));
        }else if(min > 0){
            timeView.setText(String.format("%d min %d sec", min, sec));
        }else{
            timeView.setText(String.format("%d sec", sec));
        }

        caloriesView.setText(String.format("%.2f cal", Double.parseDouble(calories)));

        if(!image.equals("")) {
            byte[] bytes_new = Base64.getDecoder().decode(image);
            Bitmap photo = BitmapFactory.decodeByteArray(bytes_new, 0, bytes_new.length);
            imageView.setImageBitmap(photo);
        }
    }

    public void goBack(View view){
        Intent intent = new Intent(this, MainPage.class);
        intent.putExtra("fragment", "checkin");
        startActivity(intent);
    }
}