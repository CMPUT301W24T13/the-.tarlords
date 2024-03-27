package com.example.the_tarlords.ui.profile;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.the_tarlords.R;

import java.net.URI;
import java.util.Objects;

public class UploadPhotoActivity extends AppCompatActivity {
    private static final int REQUEST_GALLERY_PERMISSION = 1;
    private static final int RESULT_GALLERY_UPLOAD = 1000;
    private ImageView imageView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile);

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
        startActivityForResult(open_gallery, RESULT_GALLERY_UPLOAD);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == RESULT_GALLERY_UPLOAD && data != null) {
            Uri newUpload = data.getData();
            imageView.setImageURI(newUpload);
        }
        // Retrieve the captured photo from the camera intent
        //Bitmap capturedPhoto = BitmapFactory.decodeFile(data.getData().toString());
        //ImageView imageView = findViewById(R.id.profilePic);
        //imageView.setImageBitmap(capturedPhoto);
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
