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

    public abstract Photo autoGenerate() throws IOException;

}
