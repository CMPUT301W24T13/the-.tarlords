package com.example.the_tarlords.data.Alert;



import com.example.the_tarlords.data.event.Event;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * Represents an alert
 * has title, message, event its related to, and date
 */
public class Alert{


    private String title;
    private String message;
    private Event event;
    private String currentDateTime;
    private LocalDateTime ldt;
    private String id;

    /**
     * Constructor for the Alert object
     * @param title --> string of title
     * @param message --> string of message
     */
    public Alert(String title, String message,String currentDateTime) {
        this.title = title;
        this.message = message;
        //this.event = event;
        // gets the local date time and formats it into a string
        if(currentDateTime == null) {
            ldt = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            this.currentDateTime = ldt.format(formatter);
        }else{
            this.currentDateTime = currentDateTime;
        }
    }

    public Alert(){}

    /**
     * get ldt
     * @return ldt
     */
    public LocalDateTime getLdt() {
        return ldt;
    }

    /**
     * sets ldt
     * @param ldt --> LocalDateTime object
     */
    public void setLdt(LocalDateTime ldt) {
        this.ldt = ldt;
    }

    /**
     * gets the title
     * @return string of title
     */
    public String getTitle() {
        return title;
    }

    /**
     * sets the title
     * @param title --> string of title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * gets the message
     * @return string of message
     */
    public String getMessage() {
        return message;
    }

    /**
     * gets the string of current date time
     * @return string of current date time
     */
    public String getCurrentDateTime() {
        return currentDateTime;
    }

    /**
     * sets the string of current date time
     * @param currentDateTime --> string of current date time
     */
    public void setCurrentDateTime(String currentDateTime) {
        this.currentDateTime = currentDateTime;
    }

    /**
     * sets the message
     * @param message --> string of message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * gets the event
     * @return the correspondng event
     */
    public Event getEvent() {
        return event;
    }

    /**
     * sets the event
     * @param event --> Event object
     */
    public void setEvent(Event event) {
        this.event = event;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}