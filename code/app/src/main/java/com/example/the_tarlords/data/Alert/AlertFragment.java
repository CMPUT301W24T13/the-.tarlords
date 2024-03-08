package com.example.the_tarlords.data.Alert;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.the_tarlords.MainActivity;
import com.example.the_tarlords.R;
import com.example.the_tarlords.data.event.Event;
import com.example.the_tarlords.ui.event.EventDetailsFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;

public class AlertFragment extends Fragment implements AddAlertDialogListener,MenuProvider {

    //private ArrayList<Alert> alertDataList;
    private AlertListAdapter alertListAdapter;
    public static Event event;
    private ArrayList<Alert> alertList = new ArrayList<>();

    public AlertFragment(ArrayList<Alert> alertDataList){
        Collections.sort(alertList);
        this.alertList = alertDataList;
    }
    public AlertFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            event = getArguments().getParcelable("event");
        }

    }

    /**
     *
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
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //requireActivity().addMenuProvider(this);
        View view = inflater.inflate(R.layout.fragment_alert, container, false);

        alertList = event.getAlertList(new AlertCallback() {
            @Override
            public void onAlertsLoaded(ArrayList<Alert> alertList) {
                ListView listView = view.findViewById(R.id.alert_list);
                alertListAdapter = new AlertListAdapter(requireContext(), alertList,1);
                listView.setAdapter(alertListAdapter);
            }
        });

        // unfinished fab
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.button_add_alert);
        fab.setOnClickListener(v -> {

            // calls the add book fragment constructer with filler values
            AddAlertFragment addAlertFragment = new AddAlertFragment(null);
            addAlertFragment.setAddAlertDialogListener(this);
            addAlertFragment.show(getChildFragmentManager(), "Add alert");

        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requireActivity().addMenuProvider(this);

    }
    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menu.clear();
        //menu.findItem(R.id.a)
    }

    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {

        return false;
    }

    @Override
    public void addAlert(Alert alert) {
        //alertListAdapter.add(alert);
        event.setAlert(alert);

        refreshList();
    }

    @Override
    public void deleteAlert(Alert alert) {
        //TODO update from firebase
        alertListAdapter.remove(alert);
        refreshList();
    }
    @Override
    public void editAlert(Alert oldAlert, String newTitle, String newMessage) {
        oldAlert.setTitle(newTitle);
        oldAlert.setMessage(newMessage);
        refreshList();
    }
    public void refreshList(){
        Object lock = new Object();
        synchronized (lock) {
            alertList = event.getAlertList(new AlertCallback() {
                @Override
                public void onAlertsLoaded(ArrayList<Alert> alertList) {
                    ListView listView = getView().findViewById(R.id.alert_list);
                    alertListAdapter = new AlertListAdapter(requireContext(), alertList, 1);
                    listView.setAdapter(alertListAdapter);
                    lock.notify();
                }
            });
        }
        synchronized (lock) {
            try {
                lock.wait(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            Collections.sort(alertList);
            alertListAdapter.notifyDataSetChanged();
        }


    }



}
