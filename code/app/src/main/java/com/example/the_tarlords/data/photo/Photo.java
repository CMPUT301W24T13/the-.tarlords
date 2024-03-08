package com.example.the_tarlords.data.photo;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;

import java.io.IOException;
import java.util.Random;

public abstract class Photo {
    private String fileName;
    private Uri uri;
    private Bitmap bitmap;

    public Photo(String fileName, Uri uri, Bitmap bitmap) {
        this.fileName = fileName;
        this.uri = uri;
        this.bitmap = bitmap;
    }

    public String getFileName() {
        return fileName;
    }

    public Uri getUri() {
        return uri;
    }
    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void autoGenerate() throws IOException {
        //tee hee
    }

    public void upload() {
        //upload this ass
    }
}
