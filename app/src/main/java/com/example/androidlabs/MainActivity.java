package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import static android.widget.Toast.LENGTH_LONG;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION_WRITE = 1001;
    private boolean permissionGranted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_grid);

        Button btn = (Button)findViewById(R.id.button);
        btn.setOnClickListener(c -> Toast.makeText(MainActivity.this, getResources().getString(R.string.toast_message), LENGTH_LONG).show());

        CheckBox cb = (CheckBox)findViewById(R.id.checkBox);
        cb.setOnCheckedChangeListener( (compoundButton, b) ->    {
            Snackbar.make(cb, (b == true ? getResources().getString(R.string.checkbox_ON_message) : getResources().getString(R.string.checkbox_OFF_message)), Snackbar.LENGTH_LONG)
                    .setAction("Undo", click-> compoundButton.setChecked( !b ))
                    .show();
        });

        Switch sw = (Switch)findViewById(R.id.switch1);
        sw.setOnCheckedChangeListener( (compoundButton, b) ->    {
                Snackbar.make(sw, (b == true ? getResources().getString(R.string.switch_ON_message) : getResources().getString(R.string.switch_OFF_message)), Snackbar.LENGTH_LONG)
                        .setAction("Undo", click-> compoundButton.setChecked( !b ))
                        .show();
        });

    }
 }