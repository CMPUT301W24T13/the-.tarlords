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

import java.io.Serializable;

public class AddAlertFragment extends DialogFragment {
    interface AddAlertDialogListener {
        void addAlert(Alert alert);

        void editAlert(Alert oldAlert, String newMessage);
    }

    private AddAlertFragment.AddAlertDialogListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AddAlertFragment.AddAlertDialogListener) {
            listener = (AddAlertFragment.AddAlertDialogListener)context;
        } else {
            throw new RuntimeException(context + "must implement AddAlertListener");
        }
    }

    static AddAlertFragment newInstance(Alert alert, boolean isEditing) {
        Bundle args = new Bundle();
        args.putSerializable("alert", (Serializable) alert); //serializable cast might be a problem
        args.putBoolean("isEditing", isEditing);

        AddAlertFragment fragment = new AddAlertFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(fragment_add_alert, null);
        EditText editMessage = view.findViewById(R.id.edit_text_message);

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
                .setPositiveButton(positive_button_text, (dialog, which) -> {
                    String message = editMessage.getText().toString();
                    if (getArguments() != null && getArguments().containsKey("alert")) {
                        // Editing existing alert
                        Alert oldAlert = (Alert) getArguments().getSerializable("alert");
                        listener.editAlert(oldAlert, message);
                    } else {
                        listener.addAlert(new Alert(message, event));
                    }
                })
                .create();
    }

}
}
