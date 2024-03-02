package com.example.the_tarlords.data.QR;

import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class QRCode {
    private static FragmentActivity activity;
    private String QRId;

    public QRCode(FragmentActivity activity) {
        QRCode.activity = activity;
    }

    public void scanQr() {
        IntentIntegrator intentIntegrator = new IntentIntegrator(activity);
        intentIntegrator.setPrompt("Scan QR code");
        intentIntegrator.setOrientationLocked(true); // Enable rotation
        intentIntegrator.initiateScan();
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

    public void setQRId(String newId) {
        QRId = newId;
    }
    public String getQRId() {
        return QRId;
    }
