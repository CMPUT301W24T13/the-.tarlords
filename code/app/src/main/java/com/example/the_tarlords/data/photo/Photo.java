package com.example.the_tarlords.data.photo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public abstract class Photo {
    private String fileName;
    private Bitmap bitmap;

    public Photo(String fileName, Bitmap bitmap) {
        this.fileName = fileName;
        this.bitmap = bitmap;
    }

    public String getFileName() {
        return fileName;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    /**
     * Gets string value of a photo's data from that photo's bitmap.
     * @return base64 string of photo data
     */
    public String getPhotoDataFromBitmap() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            bitmap.compress(Bitmap.CompressFormat.WEBP_LOSSY, 0, baos); //bitmap from user image file
        }
        byte[] byteArray = baos.toByteArray();
        String photoB64 = Base64.encodeToString(byteArray,0,byteArray.length,Base64.URL_SAFE);
        return photoB64;
    }

    /**
     * Sets a photo's bitmap from its base64 string data.
     * @param photoB64 base64 data of a photo in string format
     */
    public void setBitmapFromPhotoData(String photoB64) {
        byte[] byteArray = Base64.decode(photoB64, Base64.URL_SAFE);
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);
        this.bitmap = bitmap;
    }

}
