package com.example.the_tarlords.data.event;

import android.graphics.Bitmap;
import android.net.Uri;

import com.example.the_tarlords.data.photo.Photo;

import java.io.IOException;

/**
 * This class Handles event Poster
 * Private attribute that holds an image?
 */
public class EventPoster extends Photo {
    private Event event;

    public EventPoster(String fileName, Uri uri, Bitmap bitmap, Event event) {
        super(fileName, uri, bitmap);
        this.event = event;
    }

    @Override
    public void upload() {
        super.upload();
    }


}
