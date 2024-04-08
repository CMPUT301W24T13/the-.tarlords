package com.example.the_tarlords.data.photo;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import com.example.the_tarlords.R;

/**
 * Specifically represents Profile photos
 * Extends Photo class
 * @see Photo
 */

public class ProfilePhoto extends Photo {
    private String photoFirstName;
    private String photoLastName;

    public ProfilePhoto(String fileName, Bitmap bitmap, String firstName, String lastName) {
        super(fileName, bitmap);
        this.photoFirstName = firstName;
        this.photoLastName = lastName;
    }

    public ProfilePhoto(String fileName, Bitmap bitmap){
        super(fileName,bitmap);
    }

    public void autoGenerate() {
        String firstInitial = "";
        String lastInitial = "";
        //get profile info - using initials for photo
        if (photoFirstName != null&&photoFirstName.length()==0) {
            firstInitial = photoFirstName.substring(0, 1);
        } if (photoLastName != null&&photoLastName.length()==0) {
            lastInitial = photoLastName.substring(0,1);
        }
        String initials = firstInitial + lastInitial;
        if (initials.length()==0){
            initials = "\uD83D\uDC64";
        }

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

    }

    public String getPhotoFirstName() {
        return photoFirstName;
    }

    public void setPhotoFirstName(String firstName) {
        this.photoFirstName = firstName;
    }

    public String getPhotoLastName() {
        return photoLastName;
    }
    public void setPhotoLastName(String lastName) {
        this.photoLastName = lastName;
    }
}
