package com.example.the_tarlords.data.map;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

/**
 * Dialog that asks for location permissions for each event a user checks-in to
 */
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
                        LocationHelper locationHelper = new LocationHelper(getActivity());
                        locationHelper.getMyLocation(eventId);
                        getActivity().finish();
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancels the dialog. Nothing happens
                        getActivity().finish();
                    }
                });
        // Create the AlertDialog object and return it.
        return builder.create();
    }
}
