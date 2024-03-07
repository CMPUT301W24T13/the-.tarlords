package com.example.the_tarlords.ui.event;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.the_tarlords.MainActivity;
import com.example.the_tarlords.R;
import com.example.the_tarlords.data.event.Event;
import com.example.the_tarlords.ui.attendance_page.AttendanceFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventEditFragment#newInstance} factory method to
 * create an instance of this fragment.
 * This inflates the event details and allows for user to edit
 * linked to fragment_event_edit.xml
 * Will also take in an event as a parameter
 */
public class EventEditFragment extends Fragment implements MenuProvider {

    // the fragment initialization parameters
    private static Event event;
    // The views that the fragment will inflate
    private TextView eventStartDateTextView;
    private TextView eventStartTimeTextView;
    private EditText eventLocationEditText;
    private EditText eventNameEditText;
    private TextView eventEndTimeTextView;
    //add the event poster to be able to edit the poster
    //event poster doenst need to be connected to event details. Make poster connected to event so
    //when QR -> event <- event detials


    public EventEditFragment() {
        // Required empty public constructor
    }

    /**
     * This is used to create a new instance of EditEventFragment
     * @param event , an object of event class
     * @return the fragment to parent
     */
    public static EventEditFragment newInstance(Event event) {
        EventEditFragment fragment = new EventEditFragment();
        Bundle args = new Bundle();
        args.putParcelable("event", (Parcelable) event); //When getting the event cast to Event again
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            event = getArguments().getParcelable("event");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_edit, container, false);
        //Event id is a textview because user should not be able to edit it, assigned when event object created
        eventNameEditText = view.findViewById(R.id.et_event_name);
        eventLocationEditText = view.findViewById(R.id.et_event_location);
        eventStartTimeTextView = view.findViewById(R.id.tv_edit_event_startTime);
        eventStartDateTextView = view.findViewById(R.id.tv_edit_event_startDate);
        eventEndTimeTextView = view.findViewById(R.id.tv_edit_event_endTime);

        //add more attributes

        // Populate UI elements with event details
        if (event != null) {
            eventNameEditText.setText(event.getName());
            eventLocationEditText.setText(event.getLocation());
            eventStartTimeTextView.setText(event.getStartTime());
            eventStartDateTextView.setText(event.getStartDate());
            eventEndTimeTextView.setText(event.getEndTime());
            // Populate more attributes
        }
        /**
         * A Text Change Listener updates the event attributes when the edit text field is changed
         */
        eventNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //not used would this be a problem ?
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Called to notify you that somewhere within charSequence, the text has been changed.
                // Update the eventName attribute
                if (event != null) {
                    event.setName(s.toString());
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                //not used , would this be a problem ?
            }

        });
        eventLocationEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //not used here
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Called to notify you that somewhere within charSequence, the text has been changed.
                // Update the eventName attribute
                if (event != null) {
                    event.setLocation(s.toString());
                }

//                if (event != null) {
//                    event.setLocation(s.toString(), aVoid -> {});
//                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //not used here
            }
        });
        // Set an OnClickListener for the eventStartDateTextView
        eventStartDateTextView.setOnClickListener(v -> showDatePickerDialog());

        // Set an OnClickListener for the eventStartTimeTextView
        eventStartTimeTextView.setOnClickListener(v -> showTimePickerDialog("start"));

        // Set an OnClickListener for the eventEndDateTextView
        eventEndTimeTextView.setOnClickListener(v -> showTimePickerDialog("end"));

        return view;
    }

    //For both of these dialogs you can change the theme using dialog theme in layout folder
    private void showDatePickerDialog() {
        // logic for showing a date picker dialog
        DatePickerDialog dialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
            /**
             * Sets the startDate attribute using a built-in date picker
             * @param view the picker associated with the dialog
             * @param year the selected year
             * @param month the selected month (0-11 for compatibility with
             *              {@link Calendar#MONTH})
             * @param dayOfMonth the selected day of the month (1-31, depending on
             *                   month)
             */
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // Array of month names
                String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

                // Format the date components into a string "YYYY.MonthName.DD"
                @SuppressLint("DefaultLocale")
                String formattedDate = String.format("%s %02d, %04d", monthNames[month],year, dayOfMonth);

                // Update the text view
                eventStartDateTextView.setText(formattedDate);

                // Update the event attribute
                event.setStartDate(formattedDate);
            }
        }, 2024, 0, 15);
        //show the dialog
        dialog.show();
    }
    private void showTimePickerDialog(String s){
        // logic for showing a time picker dialog
        TimePickerDialog dialog = new TimePickerDialog(requireContext(), new TimePickerDialog.OnTimeSetListener() {
            /**
             * Sets the time attribute using a built-in TimePicker Dialog
             * @param view the view associated with this listener
             * @param hourOfDay the hour that was set
             * @param minute the minute that was set
             */
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String amPm;
                // Check if the selected hour is in the AM or PM period
                if (hourOfDay < 12) {
                    amPm = "AM";
                } else {
                    amPm = "PM";
                    // Adjust the hour for PM
                    hourOfDay -= 12;
                }
                // Use the amPm and adjusted hour to display or process the time
                String formattedTime = String.format("%02d:%02d %s", hourOfDay, minute, amPm);
                if(s == "start"){
                    //update text view
                    eventStartTimeTextView.setText(formattedTime);
                    //update event attribute
                    event.setStartTime(formattedTime);
                }else{
                    //update text view
                    eventEndTimeTextView.setText(formattedTime);
                    //update event attribute
                    event.setEndTime(formattedTime);
                }

            }
        }, 7, 30, true);
        //show the dialog
        dialog.show();
    }
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        requireActivity().addMenuProvider(this);
    }

    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menu.clear();
        menuInflater.inflate(R.menu.options_menu, menu);
        menu.findItem(R.id.editOptionsMenu).setVisible(false);
        menu.findItem(R.id.attendanceOptionsMenu).setVisible(false);
        menu.findItem(R.id.saveOptionsMenu).setVisible(true);
        menu.findItem(R.id.cancelOptionsMenu).setVisible(true);
    }

    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.saveOptionsMenu || menuItem.getItemId() == R.id.cancelOptionsMenu){
            if (menuItem.getItemId() == R.id.saveOptionsMenu){

            }
            // Handle item click, switch to a new fragment using FragmentManager
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            // Create a new fragment and pass the selected Event as an argument
            EventDetailsFragment newFragment = EventDetailsFragment.newInstance(event, true);

            // Replace the current fragment with the new one
            transaction.replace(R.id.eventEditFragment, newFragment);

            // Add the transaction to the back stack (optional)
            transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();
            return true;
        }

        return false;
    }
}