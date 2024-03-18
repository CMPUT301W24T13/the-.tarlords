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
    private Uri imageUri;
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

    public void uploadPicture() {
        //from android studio official references site:
        //using PickVisualMedia opens photo picker in half-screen mode.
        // Registers a photo picker activity launcher in single-select mode.
        ActivityResultLauncher<PickVisualMediaRequest> pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
            // Callback is invoked after the user selects a media item or closes the photo picker
            if (uri != null) {
                Log.d("PhotoPicker", "Selected URI: " + uri);
                imageUri = (Uri) uri;
            } else {
                Log.d("PhotoPicker", "No image selected");
                imageUri = null;
            }
        });

        // Launch the photo picker and let the user choose only images (no video).
        pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build());

        // Retrieve the captured photo from the camera intent
        Log.e("image name", imageUri.getPath());
        Bitmap capturedPhoto = BitmapFactory.decodeFile(imageUri.getPath());;
        ImageView imageView = findViewById(R.id.profilePic);
        imageView.setImageBitmap(capturedPhoto);
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
