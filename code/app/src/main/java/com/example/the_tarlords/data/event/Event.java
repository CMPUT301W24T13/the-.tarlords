package com.example.the_tarlords.data.event;

/**
 * This class defines an event
 */
public class Event {
    String name;
    String location;

    public Event(String name, String location) {
        this.name = name;
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

