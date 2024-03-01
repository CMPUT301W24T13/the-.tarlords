package com.example.the_tarlords.data.announcement;

/**
 * This class represents announcements
 */
public class Announcement {
    private String header;
    private String content;

    /**
     * Constructor for the announcement object
     * @param header
     * @param content
     */
    public Announcement(String header, String content) {
        this.header = header;
        this.content = content;
    }

    /**
     * Getter for the announcement header
     * @return header
     */
    public String getHeader() {
        return header;
    }

    /**
     * Setter for the announcement header
     * @param header
     */
    public void setHeader(String header) {
        this.header = header;
    }

    /**
     * getter for the announcement content
     * @return content
     */
    public String getContent() {
        return content;
    }

    /**
     * setter for the announcement content
     * @param content
     */
    public void setContent(String content) {
        this.content = content;
    }
}
