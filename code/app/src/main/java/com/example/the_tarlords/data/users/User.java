package com.example.the_tarlords.data.users;

import android.annotation.SuppressLint;
import android.provider.Settings;
import android.util.Log;

import com.example.the_tarlords.MainActivity;
import com.example.the_tarlords.placeholder.Photo;
import com.google.firebase.firestore.CollectionReference;

import java.util.HashMap;
import java.util.Map;

public class User implements Profile {
    private String userId;
    private String firstName;
    private String lastName;
    private Photo profilePhoto;
    private String phoneNum;
    private String email;
    private CollectionReference usersRef = MainActivity.db.collection("Users");
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

    public User(String userId) { //prev public User(Integer userId, Profile profile, ArrayList<Event> events, AlertList alerts)
        this.userId = userId;
        //this.profile = profile;
        //this.events = events;
        //this.alerts = alerts;
    }

    public User() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String id) {
        this.userId = id;
    }

    /*public Profile getProfile() {
        return profile;
    }
    public void setProfile(Profile profile) {
        this.profile = profile;
    }*/
    /*
    public void editProfile() {
        //to be implemented
    }
    */

    /*public ArrayList<Event> getEvents() {
        return events;
    }
    public void setAttendingEvents(ArrayList<Event> events) {
        this.events = events;
    }

    public AlertList getAlerts() {
        return alerts;
    }
    public void setAlerts(AlertList alerts) {
        this.alerts = alerts;
    }
*/
    boolean isAdmin() {
        return false;
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

    public void setAutoProfilePhoto() {
        //needs to be implemented
        this.profilePhoto = Photo.generateAutoProfilePhoto();
    }

    public void setProfilePhoto(Photo profilePhoto) {
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

    public void sendToFireStore() {
        // Add the new user document to Firestore
        //MAJOR NOTE THIS AUTOMATICALLY SETS THE DOC ID TO USER ID AND I DONT KNOW IF THAT WOULD BE A PROBLEM
        Map<String, Object> docData = new HashMap<>();
        docData.put("userId", userId);
        docData.put("firstName", firstName);
        docData.put("lastName", lastName);
        docData.put("email", email);
        docData.put("phoneNum", phoneNum);
        usersRef.document(userId).set(docData)
                .addOnSuccessListener(aVoid -> {
                    // Document successfully added
                    Log.d("debug", "User added successfully to Firestore");
                })
                .addOnFailureListener(e -> {
                    // Handle the failure
                    Log.e("debug", "Error adding user to Firestore", e);
                });
    }

}
