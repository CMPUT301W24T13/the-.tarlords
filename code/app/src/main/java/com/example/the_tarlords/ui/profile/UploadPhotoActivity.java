package com.example.the_tarlords.ui.profile;

import static androidx.core.app.ActivityCompat.startActivityForResult;

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
import com.example.the_tarlords.R;

import java.util.Objects;

/**
 * The UploadPhotoActivity class facilitates uploading photos from the device's photo library.
 * It handles photo library permissions, initiates the photoAlbumFragment, and processes the
 * chosen photo.
 */
public class UploadPhotoActivity {
    private static final int REQUEST_ALBUM_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_qr);

        // Check camera permission and initiate photo capture if permission is granted
        if (checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(com.example.the_tarlords.ui.profile.UploadPhotoActivity.this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, REQUEST_ALBUM_PERMISSION);
        } else {
            uploadPicture();
        }
    }

    /**
     * Initiate device's camera to capture a photo.
     */
    public void UploadPicture() {
        Intent open_album = new Intent(MediaStore.ACTION_PICK_IMAGES);
        startActivityForResult(open_album, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // Retrieve the chosen photo from the album intent
            Bitmap photo = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
            ImageView imageView = findViewById(R.id.editProfilePic);
            imageView.setImageBitmap(photo);
        } catch (Exception e) {
            //WHICH SCREEN DO WE START?? SHOULD JUST BE ABLE TO LINK BACK
            Intent intent = new Intent(com.example.the_tarlords.ui.profile.UploadPhotoActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_ALBUM_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Retry takePicture() after receiving camera permission
                uploadPicture();
            } else {
                // Inform the user to enable camera permissions and finish the activity
                Toast.makeText(this, "Open Photo Library", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}
