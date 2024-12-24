package com.example.exercise_1_mobilesecurity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionUtils {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    private static final int WIFI_PERMISSION_REQUEST_CODE = 101;

    private final Context context;

    public PermissionUtils(Context context) {
        this.context = context;
    }

    public boolean isPermissionGranted(String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermission(Activity activity, String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }

    public void requestPermissionsIfNeeded(Activity activity) {
        if (!isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            requestPermission(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }

        if (!isPermissionGranted(Manifest.permission.ACCESS_WIFI_STATE)) {
            requestPermission(activity, new String[]{Manifest.permission.ACCESS_WIFI_STATE}, WIFI_PERMISSION_REQUEST_CODE);
        }
    }

    public void handlePermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                case LOCATION_PERMISSION_REQUEST_CODE:
                    Log.d("PermissionUtils", "Location permission granted.");
                    break;
                case WIFI_PERMISSION_REQUEST_CODE:
                    Log.d("PermissionUtils", "Wi-Fi permission granted.");
                    break;
            }
        } else {
            String message = (requestCode == LOCATION_PERMISSION_REQUEST_CODE)
                    ? "Location permission is required."
                    : "Wi-Fi permission is required.";
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }
}
