package com.example.the_tarlords.ui.notification;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.example.the_tarlords.R;

public class NotificationSettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }
}