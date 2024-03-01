package com.example.the_tarlords.data.event;

import com.example.the_tarlords.data.QR.QRCode;

/**
 * This class defines an event
 */


/* NOTE FOR KHUSHI AND GRACE:

    2. Need to connect event location to the Map Class.
*/
public class Event {
    String name;
    String location;
    QRCode checkInQR;
    QRCode promoQR;
    EventPoster poster;
    String startTime;
    String startDate;
    Integer id;

    public Event(String name, String location, Integer id) {
        this.name = name;
        this.location = location;
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
}

