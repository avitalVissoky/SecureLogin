package com.example.exercise_1_mobilesecurity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;

public class LocationHelper {

    private static final double EMULATOR_LATITUDE = 37.422094;
    private static final double EMULATOR_LONGITUDE = -122.083922;
    private static final double RISHON_LATITUDE = 31.9641;
    private static final double RISHON_LONGITUDE = 34.8044;
    private static final double RANGE = 0.1;
    private boolean isLocationValid;

    private final FusedLocationProviderClient fusedLocationClient;
    private final Context context;

    public interface LocationCallback {
        void onLocationAvailable(boolean isLocationOk);
    }

    public LocationHelper(Context context) {
        this.fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        this.context = context;
    }

    public boolean isLocationValid() {
        return isLocationValid;
    }


    public void isLocationOkAsync(LocationCallback callback) {
        if (!hasLocationPermissions()) {
            Log.e("LocationHelper", "Missing location permissions.");
            callback.onLocationAvailable(false);
            return;
        }

        fusedLocationClient.getLastLocation().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                Location location = task.getResult();
                    isLocationValid = isWithinRange(location, RISHON_LATITUDE,RISHON_LONGITUDE);
                callback.onLocationAvailable(isLocationValid);
            } else {
                Log.e("LocationHelper", "Failed to fetch location.", task.getException());
                callback.onLocationAvailable(false);
            }
        });
    }

    private boolean isWithinRange(Location location, double targetLat, double targetLng) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        return latitude > targetLat - RANGE && latitude < targetLat + RANGE
                && longitude > targetLng - RANGE && longitude < targetLng + RANGE;
    }

    private boolean hasLocationPermissions() {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }
}
