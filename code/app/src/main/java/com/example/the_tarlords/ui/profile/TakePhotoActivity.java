package com.example.the_tarlords.ui.profile;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.the_tarlords.MainActivity;
import com.example.the_tarlords.R;

import java.io.File;
import java.util.Objects;

/**
 * The TakePhotoActivity class facilitates capturing photos using the device's camera.
 * It handles camera permissions, initiates the camera intent, and processes the captured photo.
 */
public class TakePhotoActivity extends AppCompatActivity {
    private static final int REQUEST_CAMERA_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile);

        // Check camera permission and initiate photo capture if permission is granted
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(TakePhotoActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        } else {
            takePicture();
        }
    }

    /**
     * Initiate device's camera to capture a photo.
     */
    public void takePicture() {
        Intent open_camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ////File f = new File(Environment.getExternalStorageDirectory(), "profile_photo.png");
        //Uri cameraImageUri = getOutputMediaFileUri(1);
        //getIntent().putExtra(MediaStore.EXTRA_OUTPUT, cameraImageUri);
        startActivityForResult(open_camera, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            /*
            File f = new File(Environment.getExternalStorageDirectory().toString());
            for (File temp : f.listFiles()) {
                if (temp.getName().equals("profile_photo.png")) {
                    f = temp;
                    String imagePath = f.getAbsolutePath();
                    Bitmap capturedPhoto = BitmapFactory.decodeFile(f.getAbsolutePath(), options);
                }
            }
            */
            // Retrieve the captured photo from the camera intent
            //Log.e("image name", cameraImageUri.getPath());
            //Bitmap capturedPhoto = BitmapFactory.decodeFile(cameraImageUri.getPath());
            Bitmap capturedPhoto = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
            ImageView imageView = findViewById(R.id.profilePic);
            imageView.setImageBitmap(capturedPhoto);
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
                // Retry takePicture() after receiving camera permission
                takePicture();
            } else {
                // Inform the user to enable camera permissions and finish the activity
                Toast.makeText(this, "Enable Camera", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    /*
    private static Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }
    private static File getOutputMediaFile(int type) {
        File mediaStore = new File(Environment.getExternalStoragePublicDirectory(), Environment.DIRECTORY_PICTURES);

    }
    */
}
