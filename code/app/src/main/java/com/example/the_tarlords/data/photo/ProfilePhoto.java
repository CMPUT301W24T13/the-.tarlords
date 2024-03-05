package com.example.the_tarlords.data.photo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;

import androidx.core.content.ContextCompat;

import com.example.the_tarlords.R;
import com.example.the_tarlords.data.users.Profile;

public class ProfilePhoto extends Photo {
    private Profile profile;

    public ProfilePhoto(String fileName, Profile profile) {
        super(fileName);
        this.profile = profile;
    }

    @Override
    public Photo autoGenerate() {
        //get profile info - using initials for photo
        String firstInitial = this.profile.getFirstName().substring(0,1);
        String lastInitial = this.profile.getLastName().substring(0,1);
        String initials = firstInitial + " " + lastInitial;
        //get semi-random color
        int color = ColorGenerator.getRandomColor();

        //ok nittygritty time ;-;
        Bitmap bitmap = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(color);
        Paint paint = new Paint();
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTypeface(Typeface.Builder builder
                .setWeight(700)
                .setItalic(false)
                .set
        )
        paint.setTextSize();
        paint.setColor(Color.WHITE);
        canvas.drawText
    }
}
