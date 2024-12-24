package com.example.exercise_1_mobilesecurity;

import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {


    private Button loginButton;
    private EditText passwordField;

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor magnetometer;
    private DeviceOrientationHelper deviceOrientationHelper;
    private LocationHelper locationHelper;
    private PermissionUtils permissionUtils;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        deviceOrientationHelper = new DeviceOrientationHelper();
        locationHelper = new LocationHelper(this);
        permissionUtils = new PermissionUtils(this);

        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        permissionUtils.requestPermissionsIfNeeded(this);

        loginButton.setOnClickListener(v -> {
            locationHelper.isLocationOkAsync(isLocationOk -> {
                if (isLocationOk && allConditionsMet()) {
                    Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, HomeActivity.class));
                } else {
                    showMissingConditionsDialog(getMissingConditions());
                }
            });
        });

    }

    private void initViews() {
        loginButton = findViewById(R.id.loginButton);
        passwordField = findViewById(R.id.passwordField);
    }

    private boolean allConditionsMet() {
        return deviceOrientationHelper.isFacingNorth()
                && isBatteryOk()
                && isDeviceTypeValid()
                && isWiFiConnected();
    }

    private String[] getMissingConditions() {
        List<String> missingConditions = new ArrayList<>();

        if (!locationHelper.isLocationValid()) {
            missingConditions.add("Location is not within the allowed range.");
        }
        if (!deviceOrientationHelper.isFacingNorth()) {
            missingConditions.add("Device is not facing North.");
        }
        if (!isBatteryOk()) {
            missingConditions.add("Battery level is below 50%.");
        }
        if (!isDeviceTypeValid()) {
            missingConditions.add("Device is not a Google Pixel.");
        }
        if (!isWiFiConnected()) {
            missingConditions.add("Wi-Fi is not connected.");
        }

        return missingConditions.toArray(new String[0]);
    }

    private void showMissingConditionsDialog(String[] missingConditions) {
        if (missingConditions.length > 0) {
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Access Denied")
                    .setMessage("The following conditions are not met:\n\n" + String.join("\n", missingConditions))
                    .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                    .show();
        }
    }

    private boolean isBatteryOk() {
        Intent batteryStatus = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        if (batteryStatus != null) {
            int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            return (level / (double) scale * 100) > 50;
        }
        return false;
    }

    private boolean isDeviceTypeValid() {

//I added this to the condition to test the emulator: || Build.MODEL.contains("gphone")
        return Build.MANUFACTURER.equalsIgnoreCase("Google")
                && (Build.MODEL.contains("Pixel") );
    }
    private boolean isWiFiConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkCapabilities nc = cm.getNetworkCapabilities(cm.getActiveNetwork());
        return nc != null && nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        deviceOrientationHelper.onSensorChanged(event);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // No action needed
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionUtils.handlePermissionResult(requestCode, permissions, grantResults);
    }
}
