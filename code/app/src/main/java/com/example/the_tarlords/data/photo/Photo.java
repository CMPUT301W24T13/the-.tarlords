package com.example.the_tarlords.data.photo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Represents all types of images in the app
 * Has bitmaps, bit strings, and the collection reference
 */

public class Photo {
    private String fileName;
    private Bitmap bitmap;
    private String imageData;
    private String collection;
    private String name;
    private String docId;

    public Photo(String fileName, Bitmap bitmap) {
        this.fileName = fileName;
        this.bitmap = bitmap;
    }
    public Photo(String imageData, String collection, String name, String docId) {
        this.imageData = imageData;
        this.collection = collection;
        this.name = name;
        this.docId = docId;
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
        int quality = 100/getPhotoCompressionFactor(bitmap,1);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            bitmap.compress(Bitmap.CompressFormat.WEBP_LOSSY,quality , baos);
        } else {
            bitmap.compress(Bitmap.CompressFormat.WEBP,quality , baos);
        }
        byte[] byteArray = baos.toByteArray();
        String photoB64 = Base64.encodeToString(byteArray,0,byteArray.length,Base64.URL_SAFE);
        return photoB64;
    }

    /**
     * Gets compression factor such that the image will fit in firebase.
     * @param bitmap
     * @param compressionFactor
     * @return
     */
    public int getPhotoCompressionFactor(Bitmap bitmap, int compressionFactor) {
        if (bitmap.getAllocationByteCount()/compressionFactor >= 500000){
            return getPhotoCompressionFactor(bitmap, compressionFactor*2);
        } else {
            return  compressionFactor;
        }
    }

    /**
     * Compresses photo to better manage fragment transaction size.
     */
    public void compressPhoto(){
        setImageData(getPhotoDataFromBitmap());
        setBitmapFromPhotoData(imageData);
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
    public String getImageData() {
        return imageData;
    }

    public void setImageData(String imageData) {
        this.imageData = imageData;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

}
