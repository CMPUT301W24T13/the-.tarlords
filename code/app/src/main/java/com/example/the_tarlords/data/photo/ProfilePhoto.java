package com.example.the_tarlords.data.photo;

import android.app.Presentation;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.Image;
import android.view.View;


import androidx.camera.core.ImageCapture;

import com.example.the_tarlords.data.users.Profile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ProfilePhoto extends Photo {
    private Profile profile;

    public ProfilePhoto(String fileName, Profile profile) {
        super(fileName);
        this.profile = profile;
    }

    public void takePhoto() {
        //don't try me i'll literally end it all
        View view;
        ImageCapture imageCapture = new ImageCapture.Builder()
                .setTargetRotation(view.getDisplay().getRotation())
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                .build();
    }

    @Override
    public void upload() {
        super.upload();
    }

    @Override
    public void autoGenerate() throws IOException {
        //get profile info - using initials for photo
        String firstInitial = this.profile.getFirstName().substring(0,1);
        String lastInitial = this.profile.getLastName().substring(0,1);
        String initials = firstInitial + lastInitial;
        //get semi-random color
        int color = ColorGenerator.getRandomColor();

        //ok nittygritty time ;-;
        Bitmap bitmap = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(color);
        Paint paint = new Paint();
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTypeface(Typeface.createFromFile("code/app/src/main/res/font/helvetica_bold.ttf"));
        paint.setTextSize(90);
        paint.setColor(Color.WHITE);
        int x = canvas.getWidth()/2;
        int y = (int) ((canvas.getHeight()/2) - ((paint.descent() + paint.ascent())/2));
        canvas.drawText(initials,x,y,paint);

        String path = "code/app/src/main/res/drawable";
        OutputStream fOut = null;
        File photoFile = new File(path, "profilePhoto.png");
        fOut = new FileOutputStream(photoFile);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        fOut.flush();
        fOut.close();
    }
}
