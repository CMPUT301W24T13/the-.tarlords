package com.example.the_tarlords.data.QR;

//https://developers.google.com/ml-kit/vision/barcode-scanning/code-scanner

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner;
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning;

public class ScanQR {
    GmsBarcodeScannerOptions options = new GmsBarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .enableAutoZoom()
            .build();

    public void getQR(Activity activity) {
        //open the camera and take a picture of a qr code
        GmsBarcodeScanner scanner = GmsBarcodeScanning.getClient(activity, options);

        scanner
            .startScan()
            .addOnSuccessListener(
                barcode -> {
                    // Task completed successfully
                })
            .addOnCanceledListener(
                () -> {
                    // Task canceled
                })
            .addOnFailureListener(
                e -> {
                    Toast.makeText(activity, "Error with Scan: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("ScanQR", "Error with Scan", e);
                });

        //String rawValue = barcode.getRawValue();
    }

    public String readQR() {
        //possible to compare images? possible to upload images to firebase?
        return "1";
    }
}
