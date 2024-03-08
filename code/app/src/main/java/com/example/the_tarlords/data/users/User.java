package com.example.the_tarlords.data.users;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.the_tarlords.MainActivity;
import com.example.the_tarlords.data.Alert.AlertList;
import com.example.the_tarlords.data.event.Event;
import com.example.the_tarlords.data.photo.Photo;
import com.example.the_tarlords.data.photo.ProfilePhoto;
import com.google.firebase.firestore.CollectionReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.provider.Settings;
import com.example.the_tarlords.MainActivity;
import java.util.Map;

public class User implements Profile {
    private String userId;
    private String firstName;
    private String lastName;
    private ProfilePhoto profilePhoto;
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
        this.profilePhoto = new ProfilePhoto(firstName+lastName, null, firstName, lastName);
        this.profilePhoto.autoGenerate();
    }

    boolean isAdmin() {
        return false;
    }

    public void (String userId) {
        this.userId = userId;
    }
    public String getUserId() {
        return userId;
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
