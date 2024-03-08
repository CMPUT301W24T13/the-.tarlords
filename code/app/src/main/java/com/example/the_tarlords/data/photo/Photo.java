package com.example.the_tarlords.data.photo;

import android.graphics.Bitmap;
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
}
