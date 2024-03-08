package com.example.the_tarlords.data.users;

import com.example.the_tarlords.data.Alert.AlertList;
import com.example.the_tarlords.data.event.Event;
import com.example.the_tarlords.data.photo.Photo;
import com.example.the_tarlords.data.photo.ProfilePhoto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class User implements Profile{
    private String userId;
    private String firstName;
    private String lastName;
    private Photo profilePhoto;
    private String phoneNum;
    private String email;
    //private Profile profile;
    //private ArrayList<Event> events;
    //private AlertList alerts;
    //TODO : need UID generator

    public User(String userId, String firstName, String lastName, String phoneNum, String email) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNum = phoneNum;
        this.email = email;

        //TODO: add firebase integration for new users and for all update data methods
    }

    boolean isAdmin() {
        return false;
    }

    public String getId() {
        return userId;
    }
    public void setId(String id) {
        this.userId = id;
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Photo getProfilePhoto() {
        return profilePhoto;
    }
    public void setAutoProfilePhoto() throws IOException {
        //needs to be implemented
        ProfilePhoto profilePhoto = null;
        profilePhoto.autoGenerate();
    }
    public void setProfilePhoto(ProfilePhoto profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getPhoneNum() {
        return phoneNum;
    }
    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}
