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
    String startTime;
    String endTime;
    String startDate;
    UUID id;
    private QRCode qrCodeCheckIns;
    private QRCode qrCodePromo;

    private EventPoster poster;

    private Integer maxSignUps;



    public Event(String name, String location) {
        this.name = name;
        this.location = location;
        this.id = UUID.randomUUID();
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


    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setQrCodeCheckIns(QRCode qrCode) {
        this.qrCodeCheckIns = qrCode;
    }

    public void setQrCodePromo(QRCode qrCode) {
        this.qrCodePromo = qrCode;
    }

    public QRCode getQrCodeCheckIns() {
        return qrCodeCheckIns;
    }

    public QRCode getQrCodePromo() {
        return qrCodePromo;
    }

    public EventPoster getPoster() {
        return poster;
    }

    public void setPoster(EventPoster poster) {
        this.poster = poster;
    }

    public Integer getMaxSignUps() {
        return maxSignUps;
    }

    public void setMaxSignUps(Integer maxSignUps) {
        this.maxSignUps = maxSignUps;
    }

    public boolean reachedMaxCap() {
        return true;
    }
}
