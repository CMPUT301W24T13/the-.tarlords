package com.example.the_tarlords.ui.profile;

import static com.example.the_tarlords.MainActivity.context;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.the_tarlords.MainActivity;
import com.example.the_tarlords.data.event.Event;
import com.example.the_tarlords.data.photo.ProfilePhoto;

import java.io.IOException;

/**
 * Activity that allows you to upload profile photos or event posters
 */

public class UploadPhotoActivity extends AppCompatActivity {
    private static final int REQUEST_GALLERY_PERMISSION = 1;
    private static final int REQUEST_IMAGE_PICK = 1000;
    private Uri uploadPath;
    private Event event;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Event event = getIntent().getParcelableExtra("event");
        if (checkSelfPermission(android.Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.READ_MEDIA_IMAGES}, REQUEST_GALLERY_PERMISSION);
        } else {
            uploadPicture();
        }
    }
    private boolean checkGalleryPermission() {
        if ( ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
            // Permission not granted, request it
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, REQUEST_GALLERY_PERMISSION);
            return false;
        }
        return true;
    }


    /**
     * Initiate device's gallery to upload a photo.
     */
    public void uploadPicture() {
        if (checkGalleryPermission()) {
            Intent open_gallery = new Intent(Intent.ACTION_PICK);
            open_gallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(open_gallery, REQUEST_IMAGE_PICK);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Event event = (Event) data.getParcelableExtra("event");

        if (resultCode == RESULT_OK && requestCode == REQUEST_IMAGE_PICK && data != null) {
            Bitmap photoUpload;
            uploadPath = data.getData();

            try {
                photoUpload = MediaStore.Images.Media.getBitmap(getContentResolver(),uploadPath);
                if (event == null) {
                    MainActivity.user.setProfilePhoto(new ProfilePhoto(MainActivity.user.getUserId(),photoUpload));
                    MainActivity.user.setPhotoIsDefault(false);
                    MainActivity.updateNavigationDrawerHeader();
                } else {
                    event.getPoster().setBitmap(photoUpload);
                    event.setPosterIsDefault(false);
                    data.putExtra("data", photoUpload);
                    setResult(RESULT_OK,data);
                }
                finish();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            finish();
        }

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
                Toast.makeText(this, "Gallery Permission Denied", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}
