package com.example.recommendationletters;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Requirements extends AppCompatActivity implements LocationListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requirements);

        // change the action bar's color
        ActionBar actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#284b63"));
        assert actionBar != null;
        actionBar.setBackgroundDrawable(colorDrawable);
    }

    // redirect to activity with map showing current location
    public void show_location(View view) {
        startActivity(new Intent(this, MapsActivity.class));
    }

    // redirect to activity displaying the luminance change
    public void display_luminance(View view) {startActivity(new Intent(this, LuminanceActivity.class));}

    // redirect to activity displaying the temperature
    public void display_temperature(View view) {startActivity(new Intent(this, TemperatureActivity.class));}

    @Override
    public void onLocationChanged(@NonNull Location location) {}
}
