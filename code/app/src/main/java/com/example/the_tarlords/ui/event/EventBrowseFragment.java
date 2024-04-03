package com.example.the_tarlords.ui.event;

import static android.view.View.GONE;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.the_tarlords.MainActivity;
import com.example.the_tarlords.R;
import com.example.the_tarlords.data.event.Event;
import com.example.the_tarlords.databinding.FragmentEventListBinding;
import com.example.the_tarlords.ui.home.EventArrayAdapter;
import com.example.the_tarlords.ui.image.ImageBrowseFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class EventBrowseFragment extends Fragment implements MenuProvider {


    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private FragmentEventListBinding binding;
    private CollectionReference eventsRef = MainActivity.db.collection("Events");
    ArrayList<Event> events = new ArrayList<>();

    /**
     * EventListFragment has a list of events called eventsList
     */
    //private EventRecyclerViewAdapter adapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public EventBrowseFragment() {
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

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requireActivity().addMenuProvider(this);

        ListView eventListView = view.findViewById(R.id.eventListView);
        Log.d("events list", events.toString()+"hello");

        //events.add(event1);
        EventArrayAdapter adapter = new EventArrayAdapter(getContext(),events);
        eventListView.setAdapter(adapter);

        //EventPosterArrayAdapter adapter = new EventPosterArrayAdapter(getContext(),events);
        //eventListView.setAdapter(adapter);

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

                                            events.add(document.toObject(Event.class));
                                            adapter.notifyDataSetChanged();
                                            Log.d("query events", document.getId() + " => " + document.getData());
                                        }
                                    }
                                    else {
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
                Bundle args = new Bundle();

                //*******************************************
                //is this necessary if you're parcelling the entire event?
                //args.putParcelable("poster", event.getPosterData());
                //*******************************************

                args.putParcelable("event",event);
                args.putBoolean("isOrganizer", false);
                args.putBoolean("browse", true);
                NavHostFragment.findNavController(EventBrowseFragment.this)
                        .navigate(R.id.action_eventBrowseFragment_to_eventDetailsFragment,args);

                //*******************************************
                /* //k i'm really not sure here but we have to get to the selected item and either
                // switch the item out for the fragment_image_list_item_back
                // or have all the text views on the same card and make the image view gone
                // and set the text views to visible
                ListView imageListView = view.findViewById(R.id.imageListView);
                View itemFragment = imageListView.getSelectedItem();

                // long click listener my proposed solution for clicking same list item redirection
                // if this is super awful and bad i can easily put a button instead
                imageListView.setOnItemLongClickListener();

                NavHostFragment.findNavController(ImageBrowseFragment.this)
                        .navigate(R.id.action_imageBrowseFragment_to_eventDetailsFragment,args);
                 */
                //*******************************************
            }
        });
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
