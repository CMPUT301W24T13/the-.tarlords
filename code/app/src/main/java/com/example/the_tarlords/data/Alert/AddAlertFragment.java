package com.example.the_tarlords.data.Alert;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.the_tarlords.R;

/**
 * Dialog Fragment for Organizers to create and send alerts
 * Alerts have titles and a message
 */

public class AddAlertFragment extends DialogFragment {


    private String titleTemp;
    private String messageTemp;
    private String ldtTemp;
    private Alert oldAlert;
    private AddAlertDialogListener listener;
    private Alert alert;

    /**
     * Constructor for the AddAlertFragment. used for receiving data from the array list
     * @param alert --> Alert object, put null if adding a new object
     */
    public AddAlertFragment(Alert alert) {
        this.alert = alert;
        if (alert != null) {
            this.titleTemp = alert.getTitle();
            this.messageTemp = alert.getMessage();
            this.ldtTemp = alert.getCurrentDateTime();
        } else {
            //this.titleTemp = "title";
            //this.messageTemp = "message";
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

    /**
     *  Creates a dialog for adding, editing or deleting alerts
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        if (alert == null) {
            return builder
                    .setView(view)
                    .setTitle("Send Alert")
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("add", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String title = editTitle.getText().toString();
                            String message = editMessage.getText().toString();
                            if(title.length()>0 && message.length()>0){
                                listener.addAlert(new Alert(title, message, null));

                            }else{
                                Toast.makeText(getContext(), "Must fill all fields",Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .create();
        } else {
            return builder
                    .setView(view)
                    .setTitle("Edit / Delete Alert")
                    .setNegativeButton("Cancel", null)

                    .setPositiveButton("Delete",(dialog, which) -> {
                        listener.deleteAlert(alert);


                    })

                    .setNeutralButton("Edit",(dialog, which) -> {
                        String title = editTitle.getText().toString();
                        String message = editMessage.getText().toString();
                        if(title.length()>0 && message.length()>0){
                            listener.editAlert(alert,title,message);

                        }else{
                            Toast.makeText(getContext(), "Must fill all fields",Toast.LENGTH_SHORT).show();
                        }
                    })
                    .create();
        }
    }


}
