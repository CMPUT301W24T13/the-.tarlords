package com.example.the_tarlords.data.photo;

import android.graphics.Color;
import java.util.Random;

public abstract class Photo {
    public abstract Photo autoGenerate();

    public Color randomColor() {
        Random rand = new Random();
        float r = (float) (rand.nextFloat() / 2f +0.5);
        float g = (float) (rand.nextFloat() / 2f +0.5);
        float b = (float) (rand.nextFloat() / 2f +0.5);
        Color randomColor = Color.valueOf(r, g, b);
    }
}
