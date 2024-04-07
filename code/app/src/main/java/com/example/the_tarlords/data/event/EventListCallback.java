package com.example.the_tarlords.data.event;

import java.util.ArrayList;

/**
 * Call-back interface for event classes
 */

public interface EventListCallback {
    public void onEventListLoaded(ArrayList<Event> eventList);
}
