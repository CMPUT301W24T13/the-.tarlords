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
import com.example.the_tarlords.data.users.Attendee;
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
    public void scanQr(Attendee attendee, Event event, QRCode generatedQRCode) {
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setPrompt("Scan QR code");
        intentIntegrator.setOrientationLocked(true); // Enable rotation
        intentIntegrator.initiateScan();

        /*
        I use QRCode class to generate the two QR codes required in Organizer class and then I
        attached each of them to their corresponding method so like for CI1 qrcode I did
        event.setQrCodeCheckIns(CI1);  dont worry about that part, it works its just linking
        qrcode to event details/checkin.

        For the "checkin" functin you asked me to do, I tried the thing below but Idk. Like idk
        how you check if attendee has scanned the qrcode. I also added the attendee, event,
        generatedQRCode parameters in this function (you didnt have them originally).
        Once you figure out how to check if attendee has scanned, you just need to set attendee's
        checkinstatus to true and the Attendance list automatically gets updated.
         */

        if (attendee "scans" event.getQrCodeCheckIns())
        attendee.setCheckInStatus(true);

        /*
        I guess one way you can check if attendee has "scanned" the qrcode is checking if the
        generatedQRCode for the event matches the Attendee's QRCode for which you would have to
        create a getQRCode() and setQRCode() methods in Attendee Class. I am not sure how your
        scan works ;-;
         */
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

