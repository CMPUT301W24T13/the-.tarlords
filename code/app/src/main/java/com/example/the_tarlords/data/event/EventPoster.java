package com.example.the_tarlords.data.event;

import com.example.the_tarlords.data.photo.Photo;

import java.io.IOException;

/**
 * This class Handles event Poster
 * Private attribute that holds an image?
 */
public class EventPoster extends Photo {
    private Event event;

    public EventPoster(String fileName) {
        super(fileName);
    }

    @Override
    public void upload() {
        super.upload();
    }


}
