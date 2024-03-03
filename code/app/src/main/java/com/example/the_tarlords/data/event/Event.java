package com.example.the_tarlords.data.event;

import static androidx.fragment.app.FragmentManager.TAG;

import static com.example.the_tarlords.MainActivity.db;

import android.util.Log;

import com.example.the_tarlords.MainActivity;
import com.example.the_tarlords.data.QR.QRCode;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * This class defines an event
 * UUID type for event attribute makes sure that everytime an event object is created it has a unique id
 * Not sure how QRcode will work , is it initialized when the event is created, or can it be set after being created
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
    String endTime;
    String startDate;
    UUID id;

    public Event(String name, String location,QRCode checkInQR, QRCode promoQR) {
        this.name = name;
        this.location = location;
        this.id = UUID.randomUUID();
        this.promoQR = promoQR;
        this.checkInQR = checkInQR;
    }

    public UUID getId() {
        return id;
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

    public QRCode getCheckInQR() {
        return checkInQR;
    }

    public void setCheckInQR(QRCode checkInQR) {
        this.checkInQR = checkInQR;
    }

    public QRCode getPromoQR() {
        return promoQR;
    }

    public void setPromoQR(QRCode promoQR) {
        this.promoQR = promoQR;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}

