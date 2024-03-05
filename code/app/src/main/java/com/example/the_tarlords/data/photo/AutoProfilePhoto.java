package com.example.the_tarlords.data.photo;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AutoProfilePhoto extends Drawable {
    private Integer color;
    private final Paint paintColor;

    public AutoProfilePhoto(Integer color) {
        this.color = color;
        paintColor = new Paint();
        paintColor.setColor(color);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        int width = 300;
        int height = 300;
        canvas.drawColor(this.color);
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return 0;
    }
}
