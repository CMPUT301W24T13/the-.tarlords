package com.example.the_tarlords.data.photo;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import com.example.the_tarlords.R;
import com.example.the_tarlords.data.event.Event;

/**
 * This class Handles event Poster
 * Private attribute that holds an image?
 */
public class EventPoster extends Photo{
    private Event event;

    public EventPoster(String fileName, Bitmap bitmap, Event event) {
        super(fileName, bitmap);
        this.event = event;
    }
    public void autoGenerate() {
        Bitmap bitmap = Bitmap.createBitmap(600, 800, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        //get semi-random color
        int color = ColorGenerator.getRandomColor();
        canvas.drawColor(color);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTypeface(Typeface.create(String.valueOf(R.font.helvetica_bold), Typeface.BOLD ));
        paint.setTextSize(90);
        paint.setColor(Color.BLACK);

        int x = canvas.getWidth()/2;
        int y = (int) ((canvas.getHeight()/8) - ((paint.descent() + paint.ascent())/2));
        canvas.drawText(event.getName(),x,y,paint);
        y = (int) (canvas.getHeight()+ ((paint.descent() + paint.ascent())/2));
        paint.setTextSize(50);
        canvas.drawText(event.getStartDate(),x,y,paint);
        this.setBitmap(bitmap);

    }
}
