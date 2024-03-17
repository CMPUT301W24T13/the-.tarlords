package com.example.the_tarlords.ui.event;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.the_tarlords.R;
import com.example.the_tarlords.data.QR.QRCode;
import com.example.the_tarlords.data.event.Event;
import com.example.the_tarlords.databinding.FragmentEventDetailsBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 * This fragment inflates the event details when an event is clicked on
 * from the attendee/homepage list view.
 * The nav bar should handle going back to the listview????
 */
public class EventDetailsFragment extends Fragment implements MenuProvider {

    private static Event event;
    private boolean isOrganizer;
    private FragmentEventDetailsBinding binding;

    /**
     * Required empty public constructor.
     */
    public EventDetailsFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param event event to be displayed.
     * @param isOrganizer whether or not the user has organizer permissions for this event.
     * @return A new instance of fragment EventDetailsFragment.
     */
    public static EventDetailsFragment newInstance(Event event, boolean isOrganizer) {
        EventDetailsFragment fragment = new EventDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable("event", event); //When getting the event cast to Event again
        args.putBoolean("isOrganizer", isOrganizer);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            event = getArguments().getParcelable("event");
            isOrganizer = getArguments().getBoolean("isOrganizer");
        }
    }

    /**
     * This is connected to fragment_event_details , need to add more details to the fragment layout
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEventDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        //MANDATORY: required for MenuProvider options menu
        requireActivity().addMenuProvider(this);

        //set fragment views
        TextView eventNameTextView = view.findViewById(R.id.tv_event_name);
        TextView eventLocationTextView = view.findViewById(R.id.tv_event_location);
        TextView eventStartDateTextView = view.findViewById(R.id.tv_event_startDate);
        TextView eventStartTimeTextView = view.findViewById(R.id.tv_event_startTime);
        TextView eventEndTimeTextView = view.findViewById(R.id.tv_event_endTime);
        TextView eventMaxAttendees = view.findViewById(R.id.tv_max_attendees);
        //add additional views here as desired

        // Check if event is not null before accessing its attributes
        if (event != null) {
            eventNameTextView.setText(event.getName());
            eventLocationTextView.setText(event.getLocation());
            eventStartTimeTextView.setText(event.getStartTime());
            eventStartDateTextView.setText(event.getStartDate());
            eventEndTimeTextView.setText(event.getEndTime());
            // set additional fields here as desired

            try {
                //TODO: this is kinda broken
                eventMaxAttendees.setText(event.getMaxSignUps().toString());
            } catch (Exception ignored) {
            }
        }

        //display event QR codes if user has organizer perms, this is extra code now , organizer will never touch this fragment
        if (isOrganizer == true) {
            if (event.getQrCodeCheckIns()!=null){
                view.findViewById(R.id.tv_checkin_details).setVisibility(view.VISIBLE);
                view.findViewById(R.id.tv_info_details).setVisibility(view.VISIBLE);
                ImageView checkInQr = view.findViewById(R.id.iv_checkin_details);
                ImageView eventInfoQr = view.findViewById(R.id.iv_info_details);
                checkInQr.setVisibility(view.VISIBLE);
                eventInfoQr.setVisibility(view.VISIBLE);
                QRCode.generateQR("CI"+event.getId(),checkInQr);
                QRCode.generateQR("EI"+event.getId(),eventInfoQr);
            }
        }
    }

    /**
     * Mandatory MenuProvider interface method.
     * Displays options menu for details fragment dependant on user status (organizer or attendee)
     * @param menu         the menu to inflate the new menu items into
     * @param menuInflater the inflater to be used to inflate the updated menu
     */
    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menu.clear();

        //link options menu xml
        menuInflater.inflate(R.menu.options_menu, menu);

        //if user is the organizer, display edit and attendance icons
        if (isOrganizer) {
            menu.findItem(R.id.editOptionsMenu).setVisible(true);
            menu.findItem(R.id.attendanceOptionsMenu).setVisible(true);
        }

        //display announcement icon for all users
        menu.findItem(R.id.anouncementsOptionsMenu).setVisible(true);
    }

    /**
     * Mandatory MenuProvider interface method.
     * Handles fragment navigation depending on which menu item was pressed.
     * @param menuItem the menu item that was selected
     * @return
     */
    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        //try/catch around all navigation actions in order to prevent crashes

        //navigate to edit fragment
        if (menuItem.getItemId() == R.id.editOptionsMenu) {
            Bundle args = new Bundle();
            args.putParcelable("event",event);
            try {
                NavHostFragment.findNavController(EventDetailsFragment.this)
                        .navigate(R.id.action_eventDetailsFragment_to_eventEditFragment, args);
            } catch (Exception ignored) {}
        }
        //navigate to attendance fragment
        else if (menuItem.getItemId()==R.id.attendanceOptionsMenu) {
            Bundle args = new Bundle();
            args.putParcelable("event",event);
            try {
                NavHostFragment.findNavController(EventDetailsFragment.this)
                        .navigate(R.id.action_eventDetailsFragment_to_attendanceFragment, args);
            } catch (Exception ignored) {}
        }
        //navigate to announcements fragment
        else if (menuItem.getItemId()==R.id.anouncementsOptionsMenu){
            Bundle args = new Bundle();
            args.putParcelable("event",event);
            try {
                NavHostFragment.findNavController(EventDetailsFragment.this)
                        .navigate(R.id.action_eventDetailsFragment_to_alertFragment, args);
            } catch (Exception ignored) {}

        }
        //should return false to prevent crashing
        return false;

    }

}