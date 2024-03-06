package com.example.the_tarlords.ui.home;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.the_tarlords.MainActivity;
import com.example.the_tarlords.R;
import com.example.the_tarlords.databinding.FragmentEventListBinding;
import com.example.the_tarlords.placeholder.PlaceholderEventContent;
import com.example.the_tarlords.data.event.Event;
import com.example.the_tarlords.placeholder.PlaceholderContent;
import com.example.the_tarlords.ui.event.EventDetailsFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


/**
 * A fragment representing a list of Event items
 */
public class EventListFragment extends Fragment implements MenuProvider {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private FragmentEventListBinding binding;
    private CollectionReference eventsRef = MainActivity.db.collection("Events");
    Event event1 = new Event("test","home","1","9:00","12:00","Mar 6");
    ArrayList<Event> events = new ArrayList<>();

    /**
     * EventListFragment has a list of events called eventsList
     */
    //private EventRecyclerViewAdapter adapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public EventListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEventListBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        requireActivity().addMenuProvider(this);
        ListView eventListView = v.findViewById(R.id.eventListView);
        Log.d("events list", events.toString()+"hello");
        //events.add(event1);
        EventArrayAdapter adapter = new EventArrayAdapter(getContext(),events);
        eventListView.setAdapter(adapter);

        eventsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshots,
                                @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Firestore", error.toString());
                    return;
                }
                if (querySnapshots != null) {
                    events.clear();
                    //TODO: tried putting this in a different class but it wasn't working, maybe someone else will have better luck?
                    eventsRef
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            MainActivity.db.collection("Events/"+document.getId()+"/Attendance")
                                                    .whereEqualTo("user", MainActivity.user.getId()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                            if (task.isSuccessful()) {
                                                                for (QueryDocumentSnapshot doc : task.getResult()) {
                                                                    events.add(document.toObject(Event.class));
                                                                    adapter.notifyDataSetChanged();
                                                                }
                                                                Log.d("query events", document.getId() + " => " + document.getData());
                                                            }
                                                            else {
                                                                Log.d("query events", "Error getting documents: ", task.getException());
                                                            }
                                                        }
                                                    });
                                        }
                                    } else {
                                        Log.d("query events", "Error getting documents: ", task.getException());
                                    }
                                }
                            });
                }
            }
        });

        eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Event event = events.get(position);
                // Handle item click, switch to a new fragment using FragmentManager
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                // Create a new fragment and pass the selected Event as an argument
                EventDetailsFragment newFragment = EventDetailsFragment.newInstance(event,false);

                // Replace the current fragment with the new one
                transaction.replace(R.id.nav_host_fragment_content_main, newFragment);

                // Add the transaction to the back stack (optional)
                transaction.addToBackStack(null);
                // Commit the transaction
                transaction.commit();
            }
        });
    }
    /**
     * This method receives the event the user clicked on in the recycler view
     * Right now it automatically takes the user to the event details page
     * assumming the EventList is an attendee type of List
     * @param event
     */

    //@Override
    public void onItemClick(Event event) {
        // Handle item click, switch to a new fragment using FragmentManager
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // Create a new fragment and pass the selected Event as an argument
        EventDetailsFragment newFragment = EventDetailsFragment.newInstance(event, false);

        // Replace the current fragment with the new one
        transaction.replace(R.id.nav_host_fragment_content_main, newFragment);

        // Add the transaction to the back stack (optional)
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menu.clear();
    }

    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }
}