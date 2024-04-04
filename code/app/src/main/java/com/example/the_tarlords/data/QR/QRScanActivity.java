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

import com.example.the_tarlords.MainActivity;
import com.example.the_tarlords.data.attendance.AttendanceDBHelper;
import com.example.the_tarlords.data.attendance.AttendanceQueryCallback;
import com.example.the_tarlords.data.event.Event;
import com.example.the_tarlords.data.map.ShareLocation;
import com.example.the_tarlords.data.users.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

/**
 * QRScanActivity class handles QR code scanning functionality and processing the scanned QR code
 */
public class QRScanActivity extends AppCompatActivity {
    private String userId;
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private FirebaseFirestore db;
    private CollectionReference QRRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        intentIntegrator.setPrompt("Scan QR code");
        intentIntegrator.setOrientationLocked(true); // Enable rotation
        intentIntegrator.initiateScan();
    }

    /**
     * Links the scanned QR code to the corresponding event ID and processes it
     *
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
                for (QueryDocumentSnapshot doc : querySnapshots) {
                    String DocCodeID = doc.getId();
                    if (DocCodeID.equals(QrString)) {
                        //Found QRcode ID, now get eventID
                        String eventID = doc.getString("EventId");
                        getEvent(QRtype, eventID);
                    }
                }
            }
        });

    }

    /**
     * Retrieves the event details corresponding to the given event ID and QR code type
     *
     * @param QRtype  The type of QR code (CheckIn or EventInfo)
     * @param eventID The ID of the event associated with the QR code
     */
    public void getEvent(String QRtype, String eventID) {
        db = FirebaseFirestore.getInstance();
        DocumentReference eventRef = db.collection("Events").document(eventID);

        eventRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Event event = task.getResult().toObject(Event.class);
                if (QRtype.equals("CI")) {
                    //This is a CheckIn QR
                    Log.e("QrCode", "In CI" + event.getId());
                    Log.e("QrCode", "EventName is " + event.getName());

                    User user = new User();
                    user.setUserId(userId);
                    AttendanceDBHelper.setCheckIn(event, user, true, new AttendanceQueryCallback() {
                        @Override
                        public void onQueryComplete(int result) {
                            if (result == AttendanceDBHelper.SUCCESSFUL) {
                                // Separate from check-In so its fine here
                                ShareLocation shareLocationDialog = new ShareLocation(event.getId(), event.getName());
                                shareLocationDialog.show(getSupportFragmentManager(), "ShareLocationDialog");
                                Toast.makeText(MainActivity.context, "Check in successful!", Toast.LENGTH_SHORT).show();
                            } else if (result == AttendanceDBHelper.ALREADY_CHECKED_IN) {
                                // Separate from check-In so its fine here
                                ShareLocation shareLocationDialog = new ShareLocation(event.getId(), event.getName());
                                shareLocationDialog.show(getSupportFragmentManager(), "ShareLocationDialog");
                                Toast.makeText(MainActivity.context, "Already checked in!", Toast.LENGTH_SHORT).show();
                            } else if (result == AttendanceDBHelper.EVENT_FULL) {
                                finish();
                                Toast.makeText(MainActivity.context, "Unable to check-in. Max capacity reached.", Toast.LENGTH_SHORT).show();
                            } else {
                                finish();
                                Toast.makeText(MainActivity.context, "Error. Check in failed.", Toast.LENGTH_SHORT).show();
                            }
                        }

                    });

                    Log.e("QrCode", "In CI" + event.getId());
                    Log.e("QrCode", "EventName is " + event.getName());


                } else if (QRtype.equals("EI")) {
                    //This is a EventInfo QR
                    Log.e("QrCode", "In EI" + event.getId());
                    Log.e("QrCode", "EventName is " + event.getName());

                    //Go to EventDetails Fragment through main
                    Intent intent = new Intent(QRScanActivity.this, MainActivity.class);
                    intent.putExtra("event", event);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.context, "Invalid QR", Toast.LENGTH_SHORT).show();
                    finish();
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
                linkQRtoEventID(qrValue);
            } else {
                Toast.makeText(this, "Unable to scan", Toast.LENGTH_SHORT).show();
                finish();
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
}
