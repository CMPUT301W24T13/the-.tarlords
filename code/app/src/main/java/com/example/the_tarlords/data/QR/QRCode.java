package com.example.the_tarlords.data.QR;

import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.fragment.app.FragmentActivity;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class QRCode {
    private final FragmentActivity activity;
    private ActivityResultLauncher<ScanOptions> barcodeLauncher;

    public QRCode(FragmentActivity activity) {
        this.activity = activity;
        initializeBarcodeLauncher();
    }

    private void initializeBarcodeLauncher() {
        if (activity != null) {
            try {
                barcodeLauncher = activity.registerForActivityResult(
                        new ScanContract(),
                        result -> {
                            if (result.getContents() != null) {
                                Toast.makeText(activity.getApplicationContext(), "scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(activity.getApplicationContext(), "cancelled", Toast.LENGTH_SHORT).show();
                            }
                        }
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void scanQR() {
        ScanOptions scanOptions = new ScanOptions();
        barcodeLauncher.launch(scanOptions);
    }
}
