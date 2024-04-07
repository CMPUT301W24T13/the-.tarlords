package com.example.the_tarlords.data.users;

import com.example.the_tarlords.data.photo.Photo;
import com.example.the_tarlords.data.photo.ProfilePhoto;

import java.io.IOException;

/**
 * Profiles implement setters and getters for name, data, and personal information
 */

public interface Profile {
    public String getFirstName();
    public void setFirstName(String firstName);

    public String getLastName();
    public void setLastName(String lastName);


    public ProfilePhoto getProfilePhoto();
    public void setProfilePhoto(ProfilePhoto profilePhoto);

    public String getPhoneNum();
    public void setPhoneNum(String phoneNum);

    public String getEmail();
    public void setEmail(String email);
}