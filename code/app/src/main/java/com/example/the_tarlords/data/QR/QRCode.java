package com.example.the_tarlords.data.QR;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import com.example.the_tarlords.MainActivity;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * QRCode class provides functionalities for generating, sharing, and managing QR codes
 */
public class QRCode {
    private String qrID;
    private FirebaseFirestore db;
    private static CollectionReference QRRef = MainActivity.db.collection("QRCodes");
    private static CollectionReference EventsRef = MainActivity.db.collection("Events");

    /**
     * Generates new QR code for a given event ID and stores it in Firebase
     * @param eventID The ID of the event associated with the QR code
     * @return The generated QR code's ID
     */
    public void makeQR(String eventID) {
        //Generate new QR code ID and store its details in Firebase
        makeNewDocID();
        sendToFirebase(eventID);
    }

    /**
     * Generates a new unique document ID for the QR code
     */
    public void makeNewDocID() {
        DocumentReference ref = QRRef.document();
        qrID = ref.getId();
    }

    /**
     * Sends the QR code details to Firebase
     * @param eventID The ID of the event associated with the QR code
     */
    public void sendToFirebase(String eventID) {
        Map<String, Object> docData = new HashMap<>();
        docData.put("QRId", qrID);
        docData.put("EventId", eventID);

        //Add QRId document to Firestore
        QRRef.document(qrID).set(docData)
            .addOnSuccessListener(aVoid -> {
                //Document successfully added
                Log.d("debug", "QR added successfully to Firestore");
            })
            .addOnFailureListener(e -> {
                //Handle the failure
                Log.e("debug", "Error adding QR to Firestore", e);
            });
    }

    /**
     * Generates a QR code bitmap from the given text and sets it to the specified ImageView
     * @param text      The text to be encoded into the QR code
     * @param imageView The ImageView where the generated QR code will be displayed
     */
    public static void generateQR(String text, ImageView imageView) {
        // Initialize a MultiFormatWriter to encode the QR code
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

        try {
            // Encode the text into a BitMatrix using QR code format with specified dimensions
            BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE, 500, 500);

            // Initialize a BarcodeEncoder to create a Bitmap from the BitMatrix
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);

            // Set the generated QR code Bitmap to the ImageView
            imageView.setImageBitmap(bitmap);

        } catch (WriterException e) {
            // Handle exception if encoding fails
            e.printStackTrace();
        }
    }

    /**
     * Shares the QR code image stored in the specified ImageView
     * @param imageView The ImageView containing the QR code image
     * @param activity  The activity context for starting the share action
     */
    //NOTE FOR USE: In fragment, to get activity: Activity activity = getActivity();
    public void shareQR(ImageView imageView, Activity activity) {
        // Retrieve Bitmap from ImageView and get its URI
        BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();

        String bitmapPath = MediaStore.Images.Media.insertImage(activity.getContentResolver(), bitmap, "QRCode", "Share QR");
        Uri uri = Uri.parse(bitmapPath);

        //Create intent to share QR code image
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/png");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        activity.startActivity(Intent.createChooser(intent, "Share"));
    }

    /**
     * Reuses a QR code from one event to another
     * @param user         The organizer's user ID
     * @param sel_event    The name of the selected event
     * @param new_eventId  The ID of the new event to which the QR code will be linked
     */
    public void reuseQR(String user, String sel_event, String new_eventId) {
        db = FirebaseFirestore.getInstance();

        EventsRef.addSnapshotListener((querySnapshots, error) -> {
            if (error != null) {
                Log.e("Firestore", error.toString());
                return;
            }
            if (querySnapshots != null) {
                for (QueryDocumentSnapshot doc: querySnapshots) {
                    String eventID = doc.getId();
                    try {
                        if (Objects.equals(doc.getString("organizerId"), user) && Objects.equals(doc.getString("name"), sel_event)) {
                            //Found event
                            String QRId = doc.getString("qrCode");
                            EventsRef.document(eventID).update("qrCode", "NULL");
                            replaceQR(QRId, new_eventId);
                        }
                    } catch (Exception e) { }
                }
            }
        });
    }

    /**
     * Replaces the QR code's event ID with a new event ID, helper function to reuseQR
     * @param QRId     The ID of the QR code to be replaced
     * @param eventID  The ID of the new event to which the QR code will be linked
     */
    public void replaceQR(String QRId, String eventID) {
        QRRef.addSnapshotListener((querySnapshots, error) -> {
            if (error != null) {
                Log.e("Firestore", error.toString());
                return;
            }
            if (querySnapshots != null) {
                for (QueryDocumentSnapshot doc: querySnapshots) {
                    String DocCodeID = doc.getId();
                    try {
                        if (DocCodeID.equals(QRId)) {
                            //Replace and put in new EventID
                            QRRef.document(DocCodeID).update("EventId", eventID);
                            qrID = DocCodeID;
                        }
                    } catch (Exception e) { }
                }
            }
        });
    }

    /**
     * Finds past events organized by a specific user and returns them via a callback
     * @param user      The user ID of the organizer
     * @param callback  The callback to be invoked when events are loaded
     */
    /*To USE:
            QRCode qrCode = new QRCode();
            qrCode.findPastEvents("USERID", new QRCode.EventsCallback() {
                @Override
                public void onEventsLoaded(ArrayList<String> events) {
                    Log.e("Event", String.valueOf(events));
                    //list is loaded
                }
            });
     */
    public void findPastEvents(String user,  EventsCallback callback) {
        db = FirebaseFirestore.getInstance();
        ArrayList<String> events = new ArrayList<String>();

        EventsRef.addSnapshotListener((querySnapshots, error) -> {
            if (error != null) {
                Log.e("Firestore", error.toString());
                return;
            }
            if (querySnapshots != null) {
                for (QueryDocumentSnapshot doc: querySnapshots) {
                    String eventID = doc.getId();
                    try {
                        if (Objects.equals(doc.getString("organizerId"), user)  && pastEvent(doc.getString("startDate"), doc.getString("endTime"))) {
                            // Event has user = organizer AND event has passed
                            Log.e("Old Events", eventID + " " + doc.getString("name") + " added");
                            events.add(doc.getString("name"));
                        }
                    } catch (Exception e) {
                        Log.e("Event", "Incorrect formatting on event");
                    }
                }
            }
            callback.onEventsLoaded(events);
        });
    }

    /**
     * Checks if an event has already occurred based on its start date and end time
     * @param startDate  The start date of the event
     * @param endTime    The end time of the event
     * @return           True if the event has passed; otherwise, false
     */
    public boolean pastEvent(String startDate, String endTime) {
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH);
        LocalDate date = LocalDate.parse(startDate, formatter1);

        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH);
        LocalTime time = LocalTime.parse(endTime, formatter2);

        LocalDateTime eventDateTime = LocalDateTime.of(date, time);
        LocalDateTime currentDateTime = LocalDateTime.now();

        Log.e("Event Date", String.valueOf(eventDateTime));
        Log.e("My Date", String.valueOf(currentDateTime));
        Log.e("Has Event Passed?", String.valueOf(currentDateTime.isAfter(eventDateTime)));

        return currentDateTime.isAfter(eventDateTime);
    }

    /**
     * Callback interface for handling loaded events
     */
    public interface EventsCallback {
        void onEventsLoaded(ArrayList<String> events);
    }

    /**
     * Retrieves the QR code ID
     * @return The QR code ID
     */
    public String getQrID() {
        return qrID;
    }
}
