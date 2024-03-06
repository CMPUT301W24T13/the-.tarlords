package com.example.the_tarlords.data.users;

import com.example.the_tarlords.data.photo.Photo;
import com.example.the_tarlords.data.photo.ProfilePhoto;

import java.io.IOException;

public class Profile {
    private User user;
    private String firstName;
    private String lastName;
    private ProfilePhoto profilePhoto;
    private String phoneNum;
    private String email;

    public Profile(User user, String firstName, String lastName, String phoneNum, String email) {
        this.user = user;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNum = phoneNum;
        this.email = email;
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

    //perhaps this is an action that needs to happen in the controller...
    public void setAutoProfilePhoto() throws IOException {
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
