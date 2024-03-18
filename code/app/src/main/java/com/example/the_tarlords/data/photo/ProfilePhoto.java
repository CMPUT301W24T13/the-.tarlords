package com.example.the_tarlords.data.photo;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.View;

import com.example.the_tarlords.R;
import com.example.the_tarlords.data.users.User;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ProfilePhoto extends Photo {
    private String firstName;
    private String lastName;

    public ProfilePhoto(String fileName, Bitmap bitmap, String firstName, String lastName) {
        super(fileName, bitmap);
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public void autoGenerate() {
        //get profile info - using initials for photo
        String firstInitial = firstName.substring(0,1);
        String lastInitial = lastName.substring(0,1);
        String initials = firstInitial + lastInitial;

        Bitmap bitmap = Bitmap.createBitmap(400, 400, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        //get semi-random color
        int color = ColorGenerator.getRandomColor();
        canvas.drawColor(color);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTypeface(Typeface.create(String.valueOf(R.font.helvetica_bold), Typeface.BOLD ));
        paint.setTextSize(180);
        paint.setColor(Color.WHITE);

        int x = canvas.getWidth()/2;
        int y = (int) ((canvas.getHeight()/2) - ((paint.descent() + paint.ascent())/2));
        canvas.drawText(initials,x,y,paint);

        this.setBitmap(bitmap);

        //String path = "code/app/src/main/res/drawable";
        //OutputStream fOut = null;
        //File photoFile = new File(path, "profilePhoto.png");
        //fOut = new FileOutputStream(photoFile);
        //bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        //fOut.flush();
        //fOut.close();
    }
}
