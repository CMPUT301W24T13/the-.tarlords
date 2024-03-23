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
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.HashMap;
import java.util.Map;

/**
 * The QRCode class provides a method to generate a QR code from a given text and display it in an ImageView.
 */
public class QRCode {
    private String qrID;
    private static CollectionReference QRRef = MainActivity.db.collection("QRCodes");

    public String makeQR(String eventID) {
        //randomly generate a new QRid and add to firebase
        makeNewDocID();
        sendToFirebase(eventID);
        return qrID;
    }

    public void makeNewDocID() {
        DocumentReference ref = QRRef.document(); //get new document from firestore
        qrID = ref.getId(); //assign event id to doc id
    }

    public void sendToFirebase(String eventID) {
        // Add the new user document to Firestore
        Map<String, Object> docData = new HashMap<>();
        docData.put("QRId", qrID);
        docData.put("EventId", eventID);

        QRRef.document(qrID).set(docData)
                .addOnSuccessListener(aVoid -> {
                    // Document successfully added
                    Log.d("debug", "User added successfully to Firestore");
                })
                .addOnFailureListener(e -> {
                    // Handle the failure
                    Log.e("debug", "Error adding user to Firestore", e);
                });
    }


    /**
     * Generates a QR code from the provided text and sets it to the specified ImageView.
     *
     * @param text      The text to be encoded into the QR code.
     * @param imageView The ImageView where the generated QR code will be displayed.
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


    //NOTE FOR USE: In fragment, to get activity: Activity activity = getActivity();
    public void shareQR(ImageView imageView, Activity activity) {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();

        String bitmapPath = MediaStore.Images.Media.insertImage(activity.getContentResolver(), bitmap, "QRCode", "Share QR");
        Uri uri = Uri.parse(bitmapPath);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/png");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        activity.startActivity(Intent.createChooser(intent, "Share"));
    }

    public String getQrID() {
        return qrID;
    }
}
