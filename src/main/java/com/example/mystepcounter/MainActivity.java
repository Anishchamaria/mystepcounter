package com.example.mystepcounter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView tvvar;
    private double magprev = 0;
    private Integer totcount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvvar = findViewById(R.id.textview);
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        SensorEventListener stepDetector = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if (sensorEvent!= null){
                    float xaxis = sensorEvent.values[0];
                    float yaxis = sensorEvent.values[1];
                    float zaxis = sensorEvent.values[2];

                    double mag = Math.sqrt(xaxis*xaxis + yaxis*yaxis + zaxis*zaxis);
                    double magd = mag - magprev;
                    magprev = mag;



                    if (magd > 6){
                        totcount++;
                    }
                    tvvar.setText(totcount.toString());
                }
            }



            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };

        sensorManager.registerListener(stepDetector, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();

        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.putInt("stepCount", totcount);
        editor.apply();
    }

    protected void onStop() {
        super.onStop();

        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.putInt("stepCount", totcount);
        editor.apply();
    }

    protected void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        totcount = sharedPreferences.getInt("stepCount", 0);
    }
}