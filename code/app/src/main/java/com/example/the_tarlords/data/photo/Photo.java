package com.example.the_tarlords.data.photo;

import android.graphics.Color;
import android.os.Build;

import java.io.IOException;
import java.util.Random;

public abstract class Photo {
    private String fileName;

    public Photo(String fileName) {
        this.fileName = fileName;
    }

    public void autoGenerate() throws IOException {
        //tee hee
    }

    public void upload() {
        //upload this ass
    }
}
