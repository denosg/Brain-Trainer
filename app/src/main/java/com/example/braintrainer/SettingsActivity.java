package com.example.braintrainer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity {

    static boolean inmultire;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.braintrainer", Context.MODE_PRIVATE);

        Switch switch1 = (Switch) findViewById(R.id.switch1);

        Intent intent = getIntent();

        inmultire = intent.getBooleanExtra("tipEx",false);

        switch1.setChecked(inmultire);

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                inmultire = b;

                sharedPreferences.edit().putBoolean("rezultat",inmultire).apply();
            }
        });

        /*if(switch1.isChecked()){
            inmultire = true;

            sharedPreferences.edit().putBoolean("tipEx",inmultire);

            Log.i("switchCase",String.valueOf(inmultire));
        }
        */
    }
}