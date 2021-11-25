package com.example.ridingcompanion;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        speed = (TextView) findViewById(R.id.textView6);
        time = (TextView) findViewById(R.id.textView8);
        distance = (TextView) findViewById(R.id.textView9);
        calories = (TextView) findViewById(R.id.textView10);

        Intent intent = getIntent();

        speedVal = intent.getDoubleExtra("avg_speed", 0);
        speed.setText("Average Speed: " + speedVal + " m/s");
        timeVal = intent.getDoubleExtra("time", 0);
        int min = (int) timeVal / 60;
        int sec = (int) timeVal - min * 60;
        time.setText(String.format("Time: %d min %d sec", min, sec));
        distanceVal = intent.getDoubleExtra("distance", 0);
        distance.setText("Distance: " + distanceVal + " km");
        caloriesVal = intent.getDoubleExtra("calories", 0);
        calories.setText("Calories: " + caloriesVal + " cal");

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
            dbHelper.addHistory(username, String.valueOf(distanceVal), String.valueOf(timeVal), String.valueOf(speedVal), String.valueOf(caloriesVal), date, imageString);

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