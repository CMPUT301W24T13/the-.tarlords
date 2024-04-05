package com.example.the_tarlords.ui.event;

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
import com.example.the_tarlords.data.event.EventListCallback;
import com.example.the_tarlords.data.event.EventListDBHelper;
import com.example.the_tarlords.databinding.FragmentEventListBinding;
import com.example.the_tarlords.ui.home.EventPosterBrowseAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class EventBrowseFragment extends Fragment implements MenuProvider {

    private FragmentEventListBinding binding;
    private CollectionReference eventsRef = MainActivity.db.collection("Events");
    ListView eventListView;
    EventPosterBrowseAdapter adapter;
    ArrayList<Event> events = new ArrayList<>();


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

        eventListView = view.findViewById(R.id.eventListView);
        adapter = new com.example.the_tarlords.ui.home.EventPosterBrowseAdapter(getContext(),events);
        eventListView.setAdapter(adapter);

        TextView pageTitle = view.findViewById(R.id.tv_event_list_header);
        pageTitle.setText(getResources().getString(R.string.poster_browse_header));

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
                    refreshList();
                }
            }
        });

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

    public void refreshList(){
        EventListDBHelper.getEventsList( new EventListCallback() {
            @Override
            public void onEventListLoaded(ArrayList<Event> eventList) {
                events.clear();
                events.addAll(eventList);
                adapter.notifyDataSetChanged();

            }
        });
    }

    public void navigateToDetails(Event event){
        Bundle args = new Bundle();
        args.putParcelable("event",event);
        args.putBoolean("browse", true);
        NavHostFragment.findNavController(EventBrowseFragment.this)
                .navigate(R.id.action_eventBrowseFragment_to_eventDetailsFragment,args);
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
