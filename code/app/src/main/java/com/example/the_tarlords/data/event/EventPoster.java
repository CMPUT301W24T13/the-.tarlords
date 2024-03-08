package com.example.the_tarlords.data.event;

import android.graphics.Bitmap;
import com.example.the_tarlords.data.photo.Photo;


/**
 * This class Handles event Poster
 * Private attribute that holds an image?
 */
public class EventPoster extends Photo {
    private Event event;

    public EventPoster(String fileName, Bitmap bitmap, Event event) {
        super(fileName, bitmap);
        this.event = event;
    }

}
