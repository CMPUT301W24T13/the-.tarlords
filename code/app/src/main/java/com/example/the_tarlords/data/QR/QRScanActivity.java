package com.example.the_tarlords.data.QR;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.the_tarlords.data.attendance.Attendance;
import com.example.the_tarlords.data.event.Event;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

/**
 * The QRScanActivity class handles QR code scanning functionality and processing the scanned QR code.
 */
public class QRScanActivity extends AppCompatActivity {
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private FirebaseFirestore db;
    private CollectionReference eventsRef;
    private Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_qr);

        // Check camera permission and initiate QR code scanning if permission is granted
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(QRScanActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        } else {
            scanQr();
        }
    }

    /**
     * Initiates the QR code scanning process using the ZXing library.
     */
    public void scanQr() {
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setPrompt("Scan QR code");
        intentIntegrator.setOrientationLocked(true); // Enable rotation
        intentIntegrator.initiateScan();
    }

    /**
     * Converts a scanned QR code value to an Event object by querying the Firestore database.
     *
     * @param QrID The QR code string value to be converted to an Event.
     */
    public void QrtoEvent(String QrID) {
        db = FirebaseFirestore.getInstance();
        eventsRef = db.collection("Events");

        eventsRef.addSnapshotListener((querySnapshots, error) -> {
            if (error != null) {
                Log.e("Firestore", error.toString());
                return;
            }
            if (querySnapshots != null) {
                for (QueryDocumentSnapshot doc: querySnapshots) {
                    String eventID = doc.getId();
                    try {

                        if (eventID.equals(QrID.substring(2))) {

                            String EventName = doc.getString("name");
                            String EventLocation = doc.getString("location");
                            Event event = new Event(EventName, EventLocation);

                            if (QrID.equals("CI" + eventID)) {
                                //This is a CheckIn QR

                            } else {
                                //This is a EventInfo QR
                            }

                        }
                    } catch (Exception e) {
                        throw new RuntimeException("This is not a valid QR code for this app");
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                String qrValue = result.getContents();
                QrtoEvent(qrValue);
                //LINK TO SOME SORT OF EVENT PAGE
            } else {
                Toast.makeText(this, "Unable to scan", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                scanQr();
            } else {
                Toast.makeText(this, "Enable Camera", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}