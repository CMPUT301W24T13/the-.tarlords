package com.example.the_tarlords.data.Alert;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.the_tarlords.R;
import com.example.the_tarlords.data.event.Event;

public class AddAlertFragment extends DialogFragment {


    private String titleTemp;
    private String messageTemp;
    private String ldtTemp;
    private Alert oldAlert;
    private AddAlertDialogListener listener;

    /**
     * Constructor for the AddAlertFragment. used for receiving data from the array list
     * @param alert --> Alert object, put null if adding a new object
     */
    public AddAlertFragment(Alert alert) {
        if (alert != null) {
            this.titleTemp = alert.getTitle();
            this.messageTemp = alert.getMessage();
            this.ldtTemp = alert.getCurrentDateTime();
            this.oldAlert = alert;
        } else {
            this.titleTemp = "title";
            this.messageTemp = "message";
            this.ldtTemp = "ldt";

        }

    }

    /**
     * Sets the listener for the dialog listnener
     * @param listener
     */
    public void setAddAlertDialogListener(AddAlertDialogListener listener) {
        this.listener = listener;
    }
    /*
    @Override
    public void onAttach(@NonNull Context context) {
        context = getContext();
        super.onAttach(context);
        if (context instanceof AddAlertDialogListener) {
            listener = (AddAlertDialogListener)context;
        } else {
            throw new RuntimeException(context + "must implement AddAlertListener");
        }
    }

     */

    /**
     *
     * @param savedInstanceState The last saved instance state of the Fragment,
     * or null if this is a freshly created Fragment.
     *
     * @return the dialog after building
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_add_alert, null);
        EditText editTitle = view.findViewById(R.id.edit_text_alert_title);
        EditText editMessage = view.findViewById(R.id.edit_text_alert_message);

        editTitle.setText(titleTemp);
        editMessage.setText(messageTemp);
        Bundle args = getArguments();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        String positive_button_text = "Add";
        if (args != null) {
            Alert alert = (Alert) args.getSerializable("alert");
            boolean isEditing = args.getBoolean("isEditing", false);
            if (isEditing) {
                builder.setTitle("Edit Alert");
                positive_button_text = "Confirm";
            } else {
                builder.setTitle("Add Alert");
                positive_button_text = "Add";
            }
            if (alert != null) {
                // Populate EditText fields with existing message information for editing
                editMessage.setText(alert.getMessage());
            }
        }
        return builder
                .setView(view)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("add", (dialog, which) -> {
                    String title = editTitle.getText().toString();
                    String message = editMessage.getText().toString();

                    // placeholder event
                    //listener.addAlert(new Alert(title, message, new Event("placeholder event", "location", null, null)));

                })
                .create();
    }
}
