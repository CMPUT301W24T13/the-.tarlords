package com.example.the_tarlords.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.the_tarlords.MainActivity;
import com.example.the_tarlords.R;
import com.example.the_tarlords.data.QR.QRScanActivity;
import com.example.the_tarlords.data.event.Event;

import com.example.the_tarlords.databinding.FragmentEventListBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.example.the_tarlords.data.event.EventListCallback;
import com.example.the_tarlords.data.event.EventListDBHelper;
import com.example.the_tarlords.databinding.FragmentEventListBinding;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


/**
 * A fragment representing a list of Event items
 * @see Event
 */
public class EventListFragment extends Fragment implements MenuProvider {

    private FragmentEventListBinding binding;
    private CollectionReference eventsRef = MainActivity.db.collection("Events");
    private ListView eventListView;
    protected EventArrayAdapter adapter;
    protected ArrayList<Event> events;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public EventListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //currently not relevant
        if (getArguments() != null) {
            //parse any args here
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEventListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //MANDATORY: this enables the options menu
        requireActivity().addMenuProvider(this);
        events = new ArrayList<>();
        eventListView = view.findViewById(R.id.eventListView);
        adapter = new EventArrayAdapter(getContext(),events);
        eventListView.setAdapter(adapter);

        TextView pageTitle = view.findViewById(R.id.tv_event_list_header);
        pageTitle.setText(getResources().getString(R.string.event_list_header));

        // This updates the displayed list on an event
        eventsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshots,
                                @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Firestore", error.toString());
                    return;
                }
                if (querySnapshots != null) {
                    refreshList();
                }
            }
        });

        //listens for user to click on an event, could maybe be its own method outside onCreate?
        eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                navigateToDetails(events.get(position));
            }
        });

        //QR code scanner button set up
        FloatingActionButton scanQrButton = view.findViewById(R.id.scan_qr_button);
        scanQrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //passes in user info in case of check-in QR scan
                Intent intent = new Intent(MainActivity.context, QRScanActivity.class);
                intent.putExtra("userId", MainActivity.user.getUserId());

                startActivity(intent);
            }
        });

    }

    /**
     * Updates list real-time
     * eg. new event created when viewing list
     */

    public void refreshList(){
        EventListDBHelper.getEventsAttendingList(MainActivity.user, new EventListCallback() {
            @Override
            public void onEventListLoaded(ArrayList<Event> eventList) {
                events.clear();
                events.addAll(eventList);
                adapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * Nagvigates to eventDeatilsFragment to display the details
     * of the selected Event object in list
     * @param event
     */

    public void navigateToDetails(Event event){
        Bundle args = new Bundle();
        args.putParcelable("event",event);
        args.putBoolean("isOrganizer", false);
        NavHostFragment.findNavController(EventListFragment.this)
                .navigate(R.id.action_eventFragment_to_eventDetailsFragment,args);
    }


    /**
     * Mandatory MenuProvider interface method.
     * Clear icons from options menu.
     * @param menu         the menu to inflate the new menu items into
     * @param menuInflater the inflater to be used to inflate the updated menu
     */
    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menu.clear();
    }

    /**
     * Mandatory MenuProvider interface method.
     * @param menuItem the menu item that was selected
     * @return
     */
    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }
}