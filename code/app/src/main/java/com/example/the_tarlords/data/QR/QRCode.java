package com.example.the_tarlords.data.QR;

import android.graphics.Bitmap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.fragment.app.FragmentActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class QRCode {
    private final FragmentActivity activity;
    private ActivityResultLauncher<ScanOptions> barcodeLauncher;
    private String QRid;

    public QRCode(FragmentActivity activity, boolean scan) {
        this.activity = activity;
        if (scan) {
            initializeBarcodeLauncher();
        }
    }

    private void initializeBarcodeLauncher() {
        if (activity != null) {
            try {
                barcodeLauncher = activity.registerForActivityResult(
                    new ScanContract(),
                    result -> {
                        if (result.getContents() != null) {
                            Toast.makeText(activity.getApplicationContext(), "scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                            QRid = result.getContents();
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

    public void generateQR(String text, ImageView imageView) {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE, 500, 500);

            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);

            imageView.setImageBitmap(bitmap);

        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    public String getQRid() {
        return QRid;
    }
