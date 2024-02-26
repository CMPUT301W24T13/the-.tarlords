package com.example.the_tarlords.data.event;


import static com.example.the_tarlords.R.id.edit_text_location;
import static com.example.the_tarlords.R.layout.fragment_add_event;

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

import java.io.Serializable;

public class AddEventFragment extends DialogFragment {
    interface AddEventDialogListener {
        void addEvent(Event event);

        void editEvent(Event oldEvent, Event newEvent);
    }

    private AddEventDialogListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AddEventDialogListener) {
            listener = (AddEventDialogListener)context;
        } else {
            throw new RuntimeException(context + "must implement AddEventListener");
        }
    }

    static AddEventFragment newInstance(Event event, boolean isEditing) {
        Bundle args = new Bundle();
        args.putSerializable("event", (Serializable) event); //serializable cast might be a problem
        args.putBoolean("isEditing", isEditing);

        AddEventFragment fragment = new AddEventFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(fragment_add_event, null);
        EditText editName = view.findViewById(R.id.edit_text_name);
        EditText editLocation = view.findViewById(R.id.edit_text_location);

        Bundle args = getArguments();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        String positive_button_text = "Add";
        if (args != null) {
            Event event = (Event) args.getSerializable("event");
            boolean isEditing = args.getBoolean("isEditing", false);
            if (isEditing) {
                builder.setTitle("Edit Event");
                positive_button_text = "Confirm";
            } else {
                builder.setTitle("Add Event");
                positive_button_text = "Add";
            }
            if (event != null) {
                // Populate EditText fields with existing book information for editing
                editName.setText(event.getName());
                editLocation.setText(event.getLocation());
            }
        }
        return builder
                .setView(view)
                .setNegativeButton("Cancel", null)
                .setPositiveButton(positive_button_text, (dialog, which) -> {
                    String name = editName.getText().toString();
                    String location = editLocation.getText().toString();
                    if (getArguments() != null && getArguments().containsKey("event")) {
                        // Editing existing event
                        Event oldEvent = (Event) getArguments().getSerializable("event");
                        listener.editEvent(oldEvent, new Event(name,location));
                    } else {
                        listener.addEvent(new Event(name,location));
                    }
                })
                .create();
    }

}

