package com.example.the_tarlords.data.QR;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.the_tarlords.MainActivity;

import java.util.Objects;

public class TakePhotoActivity extends AppCompatActivity {
    private static final int REQUEST_CAMERA_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_qr);

        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(TakePhotoActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        } else {
            takePicture();
        }
    }

    public void takePicture() {
        Intent open_camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(open_camera, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            Bitmap photo = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");

            //ImageView imageView = findViewById(R.id.img);
            //imageView.setImageBitmap(photo);
        } catch (Exception e) {
            //WHICH SCREEN DO WE START?? SHOULD JUST BE ABLE TO LINK BACK
            Intent intent = new Intent(TakePhotoActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePicture();
            } else {
                Toast.makeText(this, "Enable Camera", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}
