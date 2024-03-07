package com.example.the_tarlords.data.QR;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.the_tarlords.data.event.Event;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import com.journeyapps.barcodescanner.BarcodeEncoder;

/**
 * The QRCode class provides a method to generate a QR code from a given text and display it in an ImageView.
 */
public class QRCode {
    private FirebaseFirestore db;
    private CollectionReference eventsRef;
    //private String newDocID;

    //NEED TO JAVADOC
    //Generates a new doc id for the new event  //   Put into Event
    /*public String makeNewDocID() {
        db = FirebaseFirestore.getInstance();
        eventsRef = db.collection("Events");

        eventsRef.addSnapshotListener((querySnapshots, error) -> {
            if (error != null) {
                Log.e("Firestore", error.toString());
                return;
            }
            if (querySnapshots != null) {
                for (QueryDocumentSnapshot doc: querySnapshots) {
                    AggregateQuery countQuery = eventsRef.count();
                    countQuery.get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                // Count fetched successfully
                                AggregateQuerySnapshot snapshot = task.getResult();
                                newDocID = String.valueOf((int)snapshot.getCount() + 1);
                            } else {
                                throw new RuntimeException("Could not find number of documents in FireBase");
                            }
                        }
                    });
                }
            }
        });
        return newDocID;
    } */

    /**
     * Generates a QR code from the provided text and sets it to the specified ImageView.
     *
     * @param text      The text to be encoded into the QR code.
     * @param imageView The ImageView where the generated QR code will be displayed.
     */
    public void generateQR(String text, ImageView imageView) {
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
}

