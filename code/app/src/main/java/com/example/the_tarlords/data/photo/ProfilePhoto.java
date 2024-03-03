package com.example.the_tarlords.data.photo;

import com.example.the_tarlords.data.users.Profile;

import java.util.Random;
import java.util.HexFormat;

public class ProfilePhoto extends Photo {
    private Profile profile;
    private String firstName;
    private String lastName;

    public ProfilePhoto(Profile profile) {
        this.profile = profile;
        this.firstName = profile.getFirstName();
        this.lastName = profile.getLastName();
    }

    @Override
    public Photo autoGenerate() {
        String firstInitial = this.firstName.substring(0,1);
        String lastInitial = this.lastName.substring(0,1);
        Random rand = new Random()

    }
}
