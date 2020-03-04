package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class ProfileActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageButton mImageButton;
    public static final String ACTIVITY_NAME = "PROFILE_ACTIVITY";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(ACTIVITY_NAME,"In function:" + "onCreate()");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mImageButton = findViewById(R.id.imageButton);
        mImageButton.setOnClickListener( bt -> dispatchTakePictureIntent());

        EditText email = findViewById(R.id.textEmailProfileAct);
        Intent fromMain = getIntent();
        email.setText(fromMain.getStringExtra("EMAIL"));

        Button chat = findViewById(R.id.buttonGoToChat);
        Intent goToChat = new Intent(ProfileActivity.this, ChatRoomActivity.class);
        chat.setOnClickListener(click -> startActivity(goToChat));


        Button wheaterForecast = findViewById(R.id.buttonWeatherForecast);
        Intent goToWeatherForecast = new Intent(ProfileActivity.this, WeatherForecast.class);
        wheaterForecast.setOnClickListener(click -> startActivity(goToWeatherForecast));

    }

    @Override
    protected void onStart() {
        Log.e(ACTIVITY_NAME,"In function:" + "onStart()");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.e(ACTIVITY_NAME,"In function:" + "onResume()");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.e(ACTIVITY_NAME,"In function:" + "onPause()");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.e(ACTIVITY_NAME,"In function:" + "onStop()");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.e(ACTIVITY_NAME,"In function:" + "onDestroy()");
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(ACTIVITY_NAME,"In function:" + "onActivityResult()");
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageButton.setImageBitmap(imageBitmap);
        }
    }

    private void dispatchTakePictureIntent() {
        Log.e(ACTIVITY_NAME,"In function:" + "dispatchTakePictureIntent()");
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
}