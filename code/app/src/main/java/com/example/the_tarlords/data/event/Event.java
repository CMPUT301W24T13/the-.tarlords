package com.example.the_tarlords.data.event;

/**
 * This class defines an event
 */


/* NOTE FOR KHUSHI AND GRACE:
    1. Can you make it so two types of QRCodes are made when a new event is created. So can you attach these two QRCodes
    to an event. One QR code for "attendee check ins" and another QR code for "unique promotion QR
    code that links to the event description and event poster in the app."

    This is whats required:
    "As an organizer, I want to create a new event and generate a unique QR code for attendee check-ins."
    "As an organizer, I want to create a new event and generate a unique promotion QR code that links to the
    event description and event poster in the app."
    I am assuming its like whenever org wants to create a new it should give the org two options to generate two unique QR codes
    for that specific event, one for attendee checkins and one for the other.

    2. You don't have to do this right now but I am gonna leave it here for future reference. Need to connect event location to the Map Class.
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

