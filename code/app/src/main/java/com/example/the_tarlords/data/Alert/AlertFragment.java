package com.example.the_tarlords.data.Alert;

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

import com.example.the_tarlords.MainActivity;
import com.example.the_tarlords.R;
import com.example.the_tarlords.data.Notification.Notification;
import com.example.the_tarlords.data.event.Event;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * Represents a list of alerts that the Organizer sent out
 * Organizers can see these
 * @see Alert
 */

public class AlertFragment extends Fragment implements AddAlertDialogListener,MenuProvider {

    //private ArrayList<Alert> alertDataList;
    private AlertListAdapter alertListAdapter;
    private static Event event;
    private boolean isOrganizer;
    private ArrayList<Alert> alertList = new ArrayList<>();
    private Notification notification = new Notification();
    public AlertFragment(ArrayList<Alert> alertDataList){
        this.alertList = alertDataList;
    }
    public AlertFragment() {
        // Required empty public constructor
    }

    /**
     * on creation of the fragment
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            event = getArguments().getParcelable("event");
            isOrganizer =  getArguments().getBoolean("isOrganizer");
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
        View view = inflater.inflate(R.layout.fragment_alert_list, container, false);

        alertList = event.getAlertList(new AlertCallback() {
            @Override
            public void onAlertsLoaded(ArrayList<Alert> alertList) {
                ListView listView = view.findViewById(R.id.alert_listView);
                alertListAdapter = new AlertListAdapter(requireContext(), alertList,1);
                listView.setAdapter(alertListAdapter);
                refreshList();
            }
        });

        CollectionReference alertRef = MainActivity.db.collection("Events/"+event.getId()+"/alerts");
        alertRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Firestore", error.toString());
                    return;
                }
                if (value != null) {
                    refreshList();
                }
            }
        });

        FloatingActionButton fab = view.findViewById(R.id.button_add_alert);
        if (!isOrganizer){
            fab.setVisibility(View.GONE);
        }else{
            fab.setVisibility(View.VISIBLE);
        }
        fab.setOnClickListener(v -> {

            AddAlertFragment addAlertFragment = new AddAlertFragment(null);
            addAlertFragment.setAddAlertDialogListener(this);
            addAlertFragment.show(getChildFragmentManager(), "Add alert");

        });

        ListView alertListView = view.findViewById(R.id.alert_listView);
        if(isOrganizer){
            alertListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    AddAlertFragment addAlertFragment = new AddAlertFragment(alertList.get(position));
                    addAlertFragment.setAddAlertDialogListener(AlertFragment.this);
                    addAlertFragment.show(getChildFragmentManager(), "Add alert");

                }
            });
        }

        return view;
    }

    /**
     *
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requireActivity().addMenuProvider(this);

    }

    /**
     *
     * @param menu         the menu to inflate the new menu items into
     * @param menuInflater the inflater to be used to inflate the updated menu
     */
    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menu.clear();
        //menu.findItem(R.id.a)
    }

    /**
     *
     * @param menuItem the menu item that was selected
     * @return
     */
    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {

        return false;
    }

    /**
     * adds a new alert
     * @param alert --> alert object
     */
    @Override
    public void addAlert(Alert alert) {
        event.setAlert(alert);
        sendAnnouncementNotification();
        refreshList();
    }

    /**
     * deletes the given alert
     * @param alert
     */
    @Override
    public void deleteAlert(Alert alert) {

        DocumentReference alertRef = MainActivity.db.collection("Events/"+event.getId()+"/alerts").document(alert.getId());
        alertRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("Firestore", "Document successfully removed!");
                    //alertListAdapter.remove(alert);
                    refreshList();
                }else {
                    Log.e("Firestore", "Error removing document: " + task.getException());
                    // Handle error accordingly
                }

            }
        });

    }

    /**
     * edits an alert with the new message and title
     * @param oldAlert --> alert to be editted
     * @param newTitle
     * @param newMessage
     */
    @Override
    public void editAlert(Alert oldAlert, String newTitle, String newMessage) {
        DocumentReference alertRef = MainActivity.db.collection("Events/"+event.getId()+"/alerts").document(oldAlert.getId());
        alertRef.update("title",newTitle);
        alertRef.update("message",newMessage);
        refreshList();
    }

    /**
     * refreshes the list
     */
    public void refreshList(){
        alertList = event.getAlertList(new AlertCallback() {
            @Override
            public void onAlertsLoaded(ArrayList<Alert> alertList) {
                ListView listView = getView().findViewById(R.id.alert_listView);
                alertListAdapter = new AlertListAdapter(requireContext(), alertList, 1);
                listView.setAdapter(alertListAdapter);
                TextView noAnnouncementTextView = getView().findViewById(R.id.no_announcement_textview);
                // todo: sorting not working correctly, newest alerts should be at the top
                //Collections.sort(alertList);
                alertListAdapter.notifyDataSetChanged();
                if(alertList.isEmpty()){
                    noAnnouncementTextView.setVisibility(View.VISIBLE);
                }else{
                    noAnnouncementTextView.setVisibility(View.GONE);

                }
            }
        });

    }

    /**
     * sends a notification to all attendees for the corresponding event
     *
     */
    void sendAnnouncementNotification(){

        CollectionReference attendanceRef = MainActivity.db.collection("Events/"+event.getId()+"/Attendance");
        attendanceRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot attendeeDoc : task.getResult()){
                        MainActivity.db.collection("Users").document(attendeeDoc.getId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> t) {
                                if(t.isSuccessful()){
                                    try{
                                        DocumentSnapshot userDoc = t.getResult();
                                        String fcmToken = userDoc.getString("FCM");
                                        if(fcmToken != null){
                                            Log.d("fcm Token? ",fcmToken);
                                            notification.sendNotification(fcmToken, "New Announcement",event);
                                        }

                                    }catch (JSONException e){
                                        throw new RuntimeException(e);

                                    }
                                }
                            }
                        });
                    }
                }
            }
        });

    }

}
