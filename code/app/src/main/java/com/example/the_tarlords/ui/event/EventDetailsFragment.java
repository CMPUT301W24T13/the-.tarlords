package com.example.the_tarlords.ui.event;


import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.the_tarlords.R;
import com.example.the_tarlords.data.event.Event;
import com.example.the_tarlords.databinding.FragmentAttendanceListBinding;
import com.example.the_tarlords.databinding.FragmentEventDetailsBinding;
import com.example.the_tarlords.ui.attendance_page.AttendanceFragment;
import com.example.the_tarlords.ui.home.EventListFragment;

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

    public EventDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EventDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
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
        //TODO: If organizer status == true, setMenuVisibility(true)
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
        requireActivity().addMenuProvider(this);
        TextView eventNameTextView = view.findViewById(R.id.tv_event_name);
        TextView eventLocationTextView = view.findViewById(R.id.tv_event_location);
        TextView eventStartDateTextView = view.findViewById(R.id.tv_event_startDate);
        TextView eventStartTimeTextView = view.findViewById(R.id.tv_event_startTime);
        TextView eventEndTimeTextView = view.findViewById(R.id.tv_event_endTime);

        // Check if event is not null before accessing its attributes
        if (event != null) {
            eventNameTextView.setText(event.getName());
            eventLocationTextView.setText(event.getLocation());
            eventStartTimeTextView.setText(event.getStartTime());
            eventStartDateTextView.setText(event.getStartDate());
            eventEndTimeTextView.setText(event.getEndTime());
            // Set other attributes similarly
        }
    }

    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menu.clear();
        if (isOrganizer) {
            menuInflater.inflate(R.menu.options_menu, menu);
            menu.findItem(R.id.editOptionsMenu).setVisible(true);
            menu.findItem(R.id.attendanceOptionsMenu).setVisible(true);
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.editOptionsMenu) {
            Bundle args = new Bundle();
            args.putParcelable("event",event);
            NavHostFragment.findNavController(EventDetailsFragment.this)
                    .navigate(R.id.action_eventDetailsFragment_to_eventEditFragment,args);
        }
        else if (menuItem.getItemId()==R.id.attendanceOptionsMenu) {
            Bundle args = new Bundle();
            args.putParcelable("event",event);
            NavHostFragment.findNavController(EventDetailsFragment.this)
                    .navigate(R.id.action_eventDetailsFragment_to_attendanceFragment,args);
        }
        return false;

    }

}