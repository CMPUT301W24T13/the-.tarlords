package com.example.the_tarlords.data.users;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.the_tarlords.MainActivity;
import com.example.the_tarlords.data.Alert.AlertList;
import com.example.the_tarlords.data.event.Event;
import com.example.the_tarlords.data.photo.Photo;
import com.example.the_tarlords.data.photo.ProfilePhoto;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.provider.Settings;

import androidx.annotation.NonNull;

import com.example.the_tarlords.MainActivity;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class User implements Profile {
    private String userId;
    private String firstName;
    private String lastName;
    private ProfilePhoto profilePhoto;
    private String profilePhotoData;
    private String phoneNum;
    private String email;
    private CollectionReference usersRef = MainActivity.db.collection("Users");


    /**
     * Mandatory empty constructor for firestore functionality
     */
    public User() {
    }

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String id) {this.userId = id;}

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

    public ProfilePhoto getProfilePhoto() {
        return profilePhoto;
    }
    public void setProfilePhoto(ProfilePhoto profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    /**
     * Sets profile photo from firestore by converting the base 64 string stored in firestore
     * to a bitmask, then setting the profile photo to have that bitmask.
     * @param photoB64 base64 string of photo data
     */
    public void setProfilePhotoFromData(String photoB64) {
        profilePhoto = new ProfilePhoto(firstName+lastName,null,firstName,lastName);
        profilePhoto.setBitmapFromPhotoData(photoB64);
        profilePhotoData=photoB64;
    }
    /**
     * Gets profile photo data firestore by converting the base 64 string stored in firestore
     * to a bitmask, then setting the profile photo to have that bitmask.
     * @return String base 64 profile photo data
     */
    public String getProfilePhotoData() {
        return profilePhotoData;
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

    /**
     * Uploads user data to fire store via a hash table.
     * Includes: userId, firstName, lastName, email, phoneNum and profilePhotoData (base64 string)
     */
    public void sendToFireStore() {
        // Add the new user document to Firestore
        Map<String, Object> docData = new HashMap<>();
        docData.put("userId", userId);
        docData.put("firstName", firstName);
        docData.put("lastName", lastName);
        docData.put("email", email);
        docData.put("phoneNum", phoneNum);
        docData.put("profilePhotoData", profilePhoto.getPhotoDataFromBitmap()); //stores profile photo data as base 64 string
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


    /**
     * Deletes a user from firestore Users collection, along with any documents associated with the user in
     * the Attendance collection, and any event where the deleted user is specified as the organizer.
     */
    public void removeFromFirestore() {
        CollectionReference attendanceRef = MainActivity.db.collection("Attendance");
        CollectionReference eventsRef = MainActivity.db.collection("Events");

        //delete user doc
        usersRef.document(userId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Firestore", "Document successfully removed!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Firestore", e.getMessage());
                    }
                });

        //delete user's attendance docs
        attendanceRef.whereEqualTo("user",userId).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot attendeeDoc : queryDocumentSnapshots) {
                            attendeeDoc.getReference().delete();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Firestore", e.getMessage());
                    }
                });

        //delete events created by user
        eventsRef.whereEqualTo("organizerId", userId).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot eventDoc : queryDocumentSnapshots) {
                            eventDoc.toObject(Event.class).removeFromFirestore();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Firestore", e.getMessage());
                    }
                });
    }

}

