package com.example.the_tarlords.data.QR;

//https://github.com/journeyapps/zxing-android-embedded

import android.app.Activity;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class ScanQR {

    /*public void getQR(Activity activity) {
        //open the camera and take a picture of a qr code

        ScanOptions scanOptions = new ScanOptions();
        barcodeLauncher.launch(scanOptions);
    }

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(
            new ScanContract(),
            result -> {
                if (result.getContents() != null) {
                    Toast.makeText(getApplicationContext(), "scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "cancelled", Toast.LENGTH_SHORT).show();
                }
            }
    );*/

    public String readQR() {
        //possible to compare images? possible to upload images to firebase?
        return "1";
    }
}
