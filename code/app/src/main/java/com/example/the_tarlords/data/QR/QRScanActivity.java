package com.example.the_tarlords.data.QR;

import static java.lang.Boolean.TRUE;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.the_tarlords.MainActivity;
import com.example.the_tarlords.data.attendance.Attendance;
import com.example.the_tarlords.data.event.Event;
import com.example.the_tarlords.data.users.Attendee;
import com.example.the_tarlords.ui.event.EventDetailsFragment;
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
     * Additionally, determines the type of QR code (CheckIn or EventInfo) for further differentiation into functions
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

                            String eventName = doc.getString("name");
                            String eventLocation = doc.getString("location");
                            Event event = new Event(eventName, eventLocation);

                            if (QrID.equals("CI" + eventID)) {
                                //This is a CheckIn QR
                                //Attendee attendee = new Attendee(user, profile, event);
                                //attendee.setCheckInStatus(TRUE);
                                Intent intent = new Intent(QRScanActivity.this, MainActivity.class);
                                startActivity(intent);

                            } else {
                                //This is a EventInfo QR
                                Intent intent = new Intent(QRScanActivity.this, EventDetailsFragment.class);
                                startActivity(intent);
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
                // Retry scanQr() after receiving camera permission
                scanQr();
            } else {
                // Inform user to enable camera permissions and finish the activity
                Toast.makeText(this, "Enable Camera", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}

