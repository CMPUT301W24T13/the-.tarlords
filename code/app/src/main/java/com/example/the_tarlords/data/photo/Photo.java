package com.example.the_tarlords.data.photo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

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

    /**
     * Gets photo's bitmap.
     * @return Bitmap bitmap
     */
    public Bitmap getBitmap() {
        return bitmap;
    }

    /**
     * sets photo's bitmap
     * @param bitmap Bitmap object to assign to Photo
     */
    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    /**
     * Gets string value of a photo's data from that photo's bitmap.
     * @return base64 string of photo data
     */
    public String getPhotoDataFromBitmap() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //bitmap from user image file
        //too much increase in quality may result in firebase errors (ie string too long)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, baos);
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
