package com.example.the_tarlords.data.map;

import static com.example.the_tarlords.MainActivity.db;
import static com.example.the_tarlords.MainActivity.user;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.the_tarlords.MainActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.HashMap;

/**
 * Responsible for getting device location permissions
 * Checks if permissions are granted then gets device location
 * Sends information to firebase
 */

public class LocationHelper {
    public static final int REQUEST_LOCATION_PERMISSION = 123;
    private Context context;
    private String eventId;
    private FusedLocationProviderClient client;

    // Constructor to initialize the context
    public LocationHelper(Context context) {
        this.context = context;
        client = LocationServices.getFusedLocationProviderClient(context); // Initialize FusedLocationProviderClient
    }

    public void getMyLocation(String eventId) {
        this.eventId = eventId;
        if (checkLocationPermission()) {
            // Permission already granted, proceed to get location
            Task<Location> task = client.getLastLocation();
            task.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(android.location.Location location) {
                    if (location != null) {
                        Double latitude = location.getLatitude();
                        Double longitude = location.getLongitude();
                        checkInLocation(latitude, longitude, eventId);
                    } else {
                        Log.d("maps", "users location during check-in was null");
                    }
                }
            });
        }
    }

    public boolean checkLocationPermission() {
        if ( ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }
    public void requestLocationPermission() {
        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_PERMISSION);
    }



    public void checkInLocation(Double lat, Double lon, String eventId) {
        java.util.Map<String, Object> checkInData = new HashMap<>();
        // Assuming user is a member variable of MainActivity, adjust this accordingly if it's not
        if (MainActivity.user != null) {
            checkInData.put("name", MainActivity.user.getFirstName());
        } else {
            checkInData.put("name", "attendee");
        }
        checkInData.put("latitude", lat);
        checkInData.put("longitude", lon);

        db.collection("Events").document(eventId).collection("Attendance")
                .document(MainActivity.user.getUserId()).update(checkInData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("maps", "Check-in added with ID: " + user.getUserId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("maps", "Error adding check-in", e);
                    }
                });
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                MainActivity.locationGranted = true;
            } else {
                MainActivity.locationGranted = false;
            }
        }

    }
}

