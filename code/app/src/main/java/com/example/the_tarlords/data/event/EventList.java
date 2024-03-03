package com.example.the_tarlords.data.event;

import java.util.ArrayList;
import java.util.UUID;

/**
 * This class has an List of events
 * Each List of events could have a name , 2 constructors can have one without a name
 * Handles adding an event to a List
 * These events also have a unique id
 */
public class EventList {
    private ArrayList<Event> events;
    private String name;
    private UUID id;

    public EventList(String name) {
        this.events = new ArrayList<>();
        this.name = name;
        this.id = UUID.randomUUID();
    }

    public EventList() {
        this.events = new ArrayList<>();
        this.name = "Event Name";
        this.id = UUID.randomUUID();

    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public Event get(int position) {
        return events.get(position);
    }

    public int size() {
        return events.size();
    }

    /**
     * Adds an event to the back of the list
     * @param event
     */
    public void addEvent(Event event) {
        this.events.add(event);

    }


}
