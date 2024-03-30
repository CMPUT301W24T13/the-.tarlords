package com.example.the_tarlords.data.QR;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.the_tarlords.MainActivity;
import com.example.the_tarlords.R;
import com.example.the_tarlords.data.event.Event;
import com.example.the_tarlords.data.users.User;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

/**
 * QRScanActivity class handles QR code scanning functionality and processing the scanned QR code
 */
public class QRScanActivity extends AppCompatActivity {
    private String userId;
    private String firstName;
    private String lastName;
    private String phoneNum;
    private String email;
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private FirebaseFirestore db;
    private CollectionReference eventsRef;
    private CollectionReference QRRef;
    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.content_main);

        userId = getIntent().getStringExtra("userId");

        // Check camera permission and initiate QR code scanning if permission is granted
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(QRScanActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        } else {
            scanQr();
        }
    }

    /**
     * Initiates QR code scanning process using the ZXing library
     */
    public void scanQr() {
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setPrompt("Scan QR code");
        intentIntegrator.setOrientationLocked(true); // Enable rotation
        intentIntegrator.initiateScan();
    }

    /**
     * Links the scanned QR code to the corresponding event ID and processes it
     * @param QrID The QR code value to be linked to an event ID
     */
    public void linkQRtoEventID(String QrID) {
        db = FirebaseFirestore.getInstance();
        QRRef = db.collection("QRCodes");
        String QRtype = QrID.substring(0, 2);
        String QrString = QrID.substring(2);

        QRRef.addSnapshotListener((querySnapshots, error) -> {
            if (error != null) {
                Log.e("Firestore", error.toString());
                return;
            }
            if (querySnapshots != null) {
                for (QueryDocumentSnapshot doc: querySnapshots) {
                    String DocCodeID = doc.getId();
                    try {
                        if (DocCodeID.equals(QrString)) {
                            //Found QRcode ID, now get eventID
                            String eventID = doc.getString("EventId");
                            getEvent(QRtype, eventID);
                        }
                    } catch (Exception e) {
                        Toast.makeText(this, "Invalid QR", Toast.LENGTH_SHORT).show();
                        finish();
                        //throw new RuntimeException("This is not a valid QR code for this app");
                    }
                }
            }
        });
    }

    /**
     * Retrieves the event details corresponding to the given event ID and QR code type
     * @param QRtype  The type of QR code (CheckIn or EventInfo)
     * @param eventID The ID of the event associated with the QR code
     */
    public void getEvent(String QRtype, String eventID) {
        db = FirebaseFirestore.getInstance();
        eventsRef = db.collection("Events");

        eventsRef.addSnapshotListener((querySnapshots, error) -> {
            if (error != null) {
                Log.e("Firestore", error.toString());
                return;
            }
            if (querySnapshots != null) {
                for (QueryDocumentSnapshot doc: querySnapshots) {
                    String docEventID = doc.getId();
                    try {
                        if (docEventID.equals(eventID)) {
                            Event event = doc.toObject(Event.class);

                            if (QRtype.equals("CI")) {
                                //This is a CheckIn QR
                                Log.e("QrCode", "In CI" + docEventID);
                                Log.e("QrCode", "EventName is " + doc.getString("name"));

                                User user = new User();
                                user.setUserId(userId);
                                event.setCheckIn(user, true);
                                finish();

                            } else {
                                //This is a EventInfo QR
                                Log.e("QrCode", "In EI" + docEventID);
                                Log.e("QrCode", "EventName is " + doc.getString("name"));

                                //Go to EventDetails Fragment through main
                                Intent intent = new Intent(QRScanActivity.this, MainActivity.class);
                                intent.putExtra("event", event);
                                startActivity(intent);
                            }
                        }
                    } catch (Exception e) {
                        Toast.makeText(this, "Invalid QR", Toast.LENGTH_SHORT).show();
                        finish();
                        //throw new RuntimeException("This is not a valid QR code for this app");
                    }
                }
            }
        });
        Toast.makeText(this, "Invalid QR", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                String qrValue = result.getContents();
                linkQRtoEventID(qrValue);
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
                Toast.makeText(this, "Enable Camera to Scan QR", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    public static void showCheckInMessage(Boolean success) {
        if (success) {
            Toast.makeText(context, "Check In Successful!", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, "Unable to check-in. Max capacity reached.", Toast.LENGTH_SHORT).show();
        }
    }
}
