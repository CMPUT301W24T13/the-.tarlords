package com.example.the_tarlords.ui.profile;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.the_tarlords.MainActivity;
import com.example.the_tarlords.R;

import java.io.IOException;
import java.net.URI;
import java.util.Objects;

public class UploadPhotoActivity extends AppCompatActivity {
    private static final int REQUEST_GALLERY_PERMISSION = 1;
    private static final int REQUEST_IMAGE_PICK = 1000;
    private Uri uploadPath;
    private ImageView photo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile);

        photo = findViewById(R.id.image_view_profile);

        if (checkSelfPermission(android.Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(UploadPhotoActivity.this, new String[]{android.Manifest.permission.READ_MEDIA_IMAGES}, REQUEST_GALLERY_PERMISSION);
        } else {
            uploadPicture();
        }
    }

    /**
     * Initiate device's gallery to upload a photo.
     */
    public void uploadPicture() {
        Intent open_gallery = new Intent(Intent.ACTION_PICK);
        open_gallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(open_gallery, REQUEST_IMAGE_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUEST_IMAGE_PICK && data != null) {
            uploadPath = data.getData();
            Bitmap bitmap = null;

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uploadPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            photo.setImageBitmap(bitmap);
        }

        //WHICH SCREEN DO WE START?? SHOULD JUST BE ABLE TO LINK BACK
        Intent intent = new Intent(UploadPhotoActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_GALLERY_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Retry uploadPicture() after receiving camera permission
                uploadPicture();
            } else {
                // Inform the user to enable camera permissions and finish the activity
                Toast.makeText(this, "Open Photo Library", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}
