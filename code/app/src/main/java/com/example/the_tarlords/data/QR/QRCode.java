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

    public void shareQR(ImageView imageView) {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();

        String bitmapPath = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "QRCode", "Share QR");
        Uri uri = Uri.parse(bitmapPath);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/png");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(intent, "Share"));
    }
}
