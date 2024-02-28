package com.example.the_tarlords.data.event;

import android.widget.ListView;

import java.util.ArrayList;

/**
 * This class keeps track of a list of event objects
 */
public class EventList implements AddEventFragment.AddEventDialogListener {


    private ArrayList<Event> datalist;
    private ListView eventList;
    private EventListAdapter eventListAdapter;


    @Override
    public void addEvent(Event event) {
        eventListAdapter.add(event);
        eventListAdapter.notifyDataSetChanged();
    }

    @Override
    public void editEvent(Event oldEvent, Event newEvent) {
        int index = datalist.indexOf(oldEvent);
        if (index != -1) {
            datalist.set(index , newEvent);
            eventListAdapter.notifyDataSetChanged();
        }
    }

}

