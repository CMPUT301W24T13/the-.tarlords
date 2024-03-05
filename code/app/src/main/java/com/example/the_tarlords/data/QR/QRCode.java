package com.example.the_tarlords.data.QR;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import com.journeyapps.barcodescanner.BarcodeEncoder;

/**
 * The QRCode class provides a method to generate a QR code from a given text and display it in an ImageView.
 */
public class QRCode {

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
