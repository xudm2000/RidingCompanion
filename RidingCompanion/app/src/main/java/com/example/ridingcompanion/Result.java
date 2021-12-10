package com.example.ridingcompanion;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

public class Result extends AppCompatActivity {

    private ImageView selfie_image;
    private static final int pic_id = 24;
    private boolean hasPhoto;
    private TextView speed;
    private TextView distance;
    private TextView time;
    private TextView calories;

    private double speedVal;
    private double distanceVal;
    private double timeVal;
    private double caloriesVal;
    private String imageString;

    private GoogleMap mMap;
    private MapView mapView;

    private String route = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Intent intent = getIntent();
        double[] lats_positions = intent.getDoubleArrayExtra("lats");
        double[] longs_positions = intent.getDoubleArrayExtra("longs");

        mapView = (MapView) findViewById(R.id.fragment_map_riding2);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        try {
            MapsInitializer.initialize(this.getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mapView.getMapAsync(googleMap -> {
            mMap = googleMap;
            if(lats_positions != null && lats_positions.length != 0 && longs_positions != null && longs_positions.length != 0) {
                double prev_lat = lats_positions[0];
                double prev_long = longs_positions[0];

                route += (String.valueOf(prev_lat) + "," + String.valueOf(prev_long) + "|");
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(new LatLng(lats_positions[0], longs_positions[0]));
                for (int i = 1; i < lats_positions.length; i++) {
                    mMap.addPolyline(new PolylineOptions().add(new LatLng(lats_positions[i], longs_positions[i]), new LatLng(prev_lat, prev_long)).color(R.color.design_default_color_error));
                    builder.include(new LatLng(lats_positions[i], longs_positions[i]));
                    prev_lat = lats_positions[i];
                    prev_long = longs_positions[i];
                    route += (String.valueOf(prev_lat) + "," + String.valueOf(prev_long) + "|");
                }
                LatLngBounds bounds = builder.build();
                int padding = 100; // offset from edges of the map in pixels
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

                mMap.moveCamera(cu);
            }
        });

        speed = (TextView) findViewById(R.id.textView6);
        time = (TextView) findViewById(R.id.textView8);
        distance = (TextView) findViewById(R.id.textView9);
        calories = (TextView) findViewById(R.id.textView10);

        speedVal = intent.getDoubleExtra("avg_speed", 0);
        speed.setText(String.format("%.2f m/s", speedVal));
        timeVal = intent.getDoubleExtra("time", 0);

        int min = (int) timeVal / 60;
        int sec = (int) timeVal - min * 60;
        int hour = min / 60;
        min = min - hour * 60;

        if(hour > 0){
            time.setText(String.format("%d hr %d min %d sec", hour, min, sec));
        }else if(min > 0){
            time.setText(String.format("%d min %d sec", min, sec));
        }else{
            time.setText(String.format("%d sec", sec));
        }

        distanceVal = intent.getDoubleExtra("distance", 0);
        distance.setText(String.format("%.2f km", distanceVal));
        caloriesVal = intent.getDoubleExtra("calories", 0);
        calories.setText(String.format("%.2f cal", caloriesVal));

        selfie_image = (ImageView) findViewById(R.id.imageView);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        selfie_image.setMinimumWidth(width/2);
        selfie_image.setMinimumHeight(height/3);

        selfie_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(camera_intent, pic_id);
            }
        });

        hasPhoto = false;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == pic_id) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            selfie_image.setImageBitmap(photo);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] imageByte = stream.toByteArray();
            imageString = Base64.getEncoder().encodeToString(imageByte);

            hasPhoto = true;
        }
    }


    public void checkin(View view){
        Intent intent = new Intent(this, MainPage.class);
        intent.putExtra("fragment", "checkin");

        if(!hasPhoto){
            imageString = "";
        }

        SharedPreferences sharedPreferences = getSharedPreferences("com.example.ridingcompanion", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("current", "");
        if(!username.equals("")){
            Context context = getApplicationContext();
            SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("users", Context.MODE_PRIVATE,null);
            DBHelper dbHelper = new DBHelper(sqLiteDatabase);
            String date = "";
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm");
            Date d = new Date();
            date = formatter.format(d);
            dbHelper.addHistory(username, String.valueOf(distanceVal), String.valueOf(timeVal), String.valueOf(speedVal), String.valueOf(caloriesVal), date, imageString, route);

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            RankUser rankUser = dbHelper.getRankUser(username);
            if(rankUser == null) {
                db.collection("users").document(username).set(new RankUser(username, 1));
            }else{
                DocumentReference docRef = db.collection("users").document(username);
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                int num = document.getLong("numOfDay").intValue() + 1;
                                db.collection("users").document(username).set(new RankUser(username, num));
                            } else { }
                        } else { }
                    }
                });
            }
        }
        startActivity(intent);
    }
}