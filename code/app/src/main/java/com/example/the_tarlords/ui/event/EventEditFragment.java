package com.example.the_tarlords.ui.event;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.the_tarlords.MainActivity;
import com.example.the_tarlords.R;
import com.example.the_tarlords.data.QR.QRCode;
import com.example.the_tarlords.data.event.Event;
import com.example.the_tarlords.databinding.FragmentEventEditBinding;

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
    private EditText maxAttendees;
    private ImageView checkInQR;
    private ImageView eventInfoQR;
    private FragmentEventEditBinding binding;
    //add the event poster to be able to edit the poster
    //event poster doenst need to be connected to event details. Make poster connected to event so
    //when QR -> event <- event detials


    public EventEditFragment() {
        // Required empty public constructor
    }

    /**
     * This is used to create a new instance of EditEventFragment
     *
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
        binding = FragmentEventEditBinding.inflate(inflater, container, false);

        return binding.getRoot();

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
                String formattedDate = String.format("%s %02d, %04d", monthNames[month], year, dayOfMonth);

                // Update the text view
                eventStartDateTextView.setText(formattedDate);

            }
        }, 2024, 0, 15);
        //show the dialog
        dialog.show();
    }

    private void showTimePickerDialog(String s) {
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
                if (s == "start") {
                    //update text view
                    eventStartTimeTextView.setText(formattedTime);
                } else {
                    //update text view
                    eventEndTimeTextView.setText(formattedTime);
                }

            }
        }, 7, 30, true);
        //show the dialog
        dialog.show();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        requireActivity().addMenuProvider(this);
        //Event id is a textview because user should not be able to edit it, assigned when event object created
        eventNameEditText = view.findViewById(R.id.et_event_name);
        eventLocationEditText = view.findViewById(R.id.et_event_location);
        eventStartTimeTextView = view.findViewById(R.id.tv_edit_event_startTime);
        eventStartDateTextView = view.findViewById(R.id.tv_edit_event_startDate);
        eventEndTimeTextView = view.findViewById(R.id.tv_edit_event_endTime);
        maxAttendees = view.findViewById(R.id.et_max_attendees);
        checkInQR = view.findViewById(R.id.iv_checkin);
        eventInfoQR = view.findViewById(R.id.iv_info);

        //add more attributes as desired

        //check event is not null
        if (event.getId() != null) {
            // Populate UI elements with event details
            eventNameEditText.setText(event.getName());
            eventLocationEditText.setText(event.getLocation());
            eventStartTimeTextView.setText(event.getStartTime());
            eventStartDateTextView.setText(event.getStartDate());
            eventEndTimeTextView.setText(event.getEndTime());
            maxAttendees.setText(event.getMaxSignUps().toString());
            // Populate more attributes as desired
        }
        //if event is null, create new event
        else {
            //set placeholder data
            eventNameEditText.setHint("Event Name");
            eventLocationEditText.setHint("Location");
            eventStartDateTextView.setText("Date");
            eventStartTimeTextView.setText("Start time");
            eventEndTimeTextView.setText("End Time");
        }
        //check if QR codes have already been generated
        if (event.getQrCodeCheckIns() == null) {
            //hide QR code placeholder views
            view.findViewById(R.id.tv_checkin).setVisibility(view.GONE);
            view.findViewById(R.id.tv_info).setVisibility(view.GONE);
            checkInQR.setVisibility(view.GONE);
            eventInfoQR.setVisibility(view.GONE);
        } else {
            //display QR codes
            QRCode.generateQR("CI" + event.getId(), checkInQR);
            QRCode.generateQR("EI" + event.getId(), eventInfoQR);
        }


        // Set an OnClickListener for the eventStartDateTextView
        eventStartDateTextView.setOnClickListener(v -> showDatePickerDialog());

        // Set an OnClickListener for the eventStartTimeTextView
        eventStartTimeTextView.setOnClickListener(v -> showTimePickerDialog("start"));

        // Set an OnClickListener for the eventEndDateTextView
        eventEndTimeTextView.setOnClickListener(v -> showTimePickerDialog("end"));
    }

    /**
     * Mandatory MenuProvider interface method.
     * Displays eventEditFragment options menu.
     * @param menu         the menu to inflate the new menu items into
     * @param menuInflater the inflater to be used to inflate the updated menu
     */
    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menu.clear();
        menuInflater.inflate(R.menu.options_menu, menu);
        menu.findItem(R.id.saveOptionsMenu).setVisible(true);
        menu.findItem(R.id.cancelOptionsMenu).setVisible(true);
    }

    /**
     * Mandatory MenuProvider interface method.
     * @param menuItem the menu item that was selected
     * @return
     */
    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.saveOptionsMenu || menuItem.getItemId() == R.id.cancelOptionsMenu) {
            //save changes to event details
            if (menuItem.getItemId() == R.id.saveOptionsMenu) {

                // Update the event startDate
                event.setStartDate(eventStartDateTextView.getText().toString());
                //update event startTime
                event.setStartTime(eventStartTimeTextView.getText().toString());
                //update event endTime
                event.setEndTime(eventEndTimeTextView.getText().toString());
                //update event name
                event.setName(eventNameEditText.getText().toString());
                //update event location
                event.setLocation(eventLocationEditText.getText().toString());
                //update event organizerId
                event.setOrganizerId(MainActivity.user.getUserId());
                //update event maxSignUps
                try {
                    //TODO: kinda buggy iirc
                    event.setMaxSignUps(Integer.valueOf(maxAttendees.getText().toString()));
                } catch (IllegalStateException e) {
                    //event.setMaxSignUps(0);
                }

                //if eventId is null, treat as new event and generate a new id
                if (event.getId() == null) {
                    //generate new event id
                    event.makeNewDocID();
                    //generate check in QR
                    event.setQrCodeCheckIns("CI" + event.getId());
                    //generate event info QR
                    event.setQrCodePromo("EI" + event.getId());

                }
                //upload event in firebase
                event.sendToFirebase();

                //TODO : check valid input
            }
            //navigate back to event details fragment
            //try/catch to prevent crashes
            Bundle args = new Bundle();
            args.putParcelable("event", event);
            args.putBoolean("isOrganizer", true);
            try {
                NavHostFragment.findNavController(EventEditFragment.this)
                        .navigate(R.id.action_eventEditFragment_pop, args);
            } catch (Exception ignored) {}
            return false; //required to prevent crashes
        }

        return false;
    }
}
