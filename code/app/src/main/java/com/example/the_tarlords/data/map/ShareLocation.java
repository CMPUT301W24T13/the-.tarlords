package com.example.the_tarlords.data.map;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import com.example.the_tarlords.R;

public class ShareLocation extends DialogFragment {

    private String eventId;
    private String eventName;

    public ShareLocation(String eventId, String eventName) {
        this.eventId = eventId;
        this.eventName = eventName;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction.
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Would you like to share your location with " + eventName + "?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // calling location helper
                        LocationHelper locationHelper = new LocationHelper(getActivity()); // Assuming getActivity() provides the context of your activity
                        locationHelper.getMyLocation(eventId);
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancels the dialog. Nothing happens
                    }
                });
        // Create the AlertDialog object and return it.
        return builder.create();
    }
}
