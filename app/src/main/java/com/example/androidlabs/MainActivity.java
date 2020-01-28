package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    SharedPreferences prefs = null;
    EditText typeField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        typeField = findViewById(R.id.emailMain);
        prefs = getSharedPreferences("FileName", Context.MODE_PRIVATE);
        String savedString = prefs.getString("ReserveName", typeField.getText().toString());
        typeField.setText(savedString);


        Button login = findViewById(R.id.loginButton);
        Intent goToProfile = new Intent(MainActivity.this, ProfileActivity.class);


        login.setOnClickListener( click -> {goToProfile.putExtra("EMAIL", typeField.getText().toString());
                                            startActivity( goToProfile );});
    }

    @Override
    protected void onPause() {
        saveSharedPrefs( typeField.getText().toString());
        super.onPause();
    }

    private void saveSharedPrefs(String stringToSave)
    {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("ReserveName", stringToSave);
        editor.commit();
    }
}