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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.the_tarlords.MainActivity;
import com.example.the_tarlords.R;
import com.example.the_tarlords.data.event.Event;
import com.example.the_tarlords.ui.event.EventDetailsFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import com.google.firebase.database.DatabaseReference;


public class AlertFragment extends Fragment implements AddAlertDialogListener,MenuProvider {

    //private ArrayList<Alert> alertDataList;
    private AlertListAdapter alertListAdapter;
    private static Event event;
    private boolean isOrganizer;
    private ArrayList<Alert> alertList = new ArrayList<>();
    public AlertFragment(ArrayList<Alert> alertDataList){
        Collections.sort(alertList);
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
        View view = inflater.inflate(R.layout.fragment_alert, container, false);

        alertList = event.getAlertList(new AlertCallback() {
            @Override
            public void onAlertsLoaded(ArrayList<Alert> alertList) {
                ListView listView = view.findViewById(R.id.alert_list);
                alertListAdapter = new AlertListAdapter(requireContext(), alertList,1);
                listView.setAdapter(alertListAdapter);
                refreshList();



            }
        });
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.button_add_alert);
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

        ListView alertListView = (ListView) view.findViewById(R.id.alert_list);
        if(isOrganizer){
            alertListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    new AddAlertFragment(alertList.get(position)).show(getChildFragmentManager(),"Edit / delete alert" );
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
     *
     * @param alert --> alert object
     */
    @Override
    public void addAlert(Alert alert) {
        event.setAlert(alert);
        sendAnnouncementNotification(event,"New Announcement");
        refreshList();
    }

    /**
     *
     * @param alert
     */
    @Override
    public void deleteAlert(Alert alert) {
        //TODO update from firebase
        alertListAdapter.remove(alert);
        refreshList();
    }

    /**
     *
     * @param oldAlert --> alert to be editted
     * @param newTitle
     * @param newMessage
     */
    @Override
    public void editAlert(Alert oldAlert, String newTitle, String newMessage) {
        oldAlert.setTitle(newTitle);
        oldAlert.setMessage(newMessage);
        refreshList();
    }

    /**
     * refreshes the list
     */
    public void refreshList(){
        alertList = event.getAlertList(new AlertCallback() {
            @Override
            public void onAlertsLoaded(ArrayList<Alert> alertList) {
                ListView listView = getView().findViewById(R.id.alert_list);
                alertListAdapter = new AlertListAdapter(requireContext(), alertList, 1);
                listView.setAdapter(alertListAdapter);
            }
        });
            // todo: sorting not working correctly, newest alerts should be at the top
            Collections.sort(alertList);
            alertListAdapter.notifyDataSetChanged();



    }

    /**
     * sends a notification for the corresponding event
     * @param event
     */
    void sendAnnouncementNotification(Event event,String text){

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
                                            JSONObject jsonObject = getJsonObject(fcmToken,text);
                                            callApi(jsonObject);
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

    /**
     * creates a JSONObject representation of a notification
     * @param fcmToken
     * @param text
     * @return JSONObject representation
     * @throws JSONException
     */
    @NonNull
    private static JSONObject getJsonObject(String fcmToken,String text) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        JSONObject notificationObject = new JSONObject();
        JSONObject dataObject = new JSONObject();
        notificationObject.put("title", event.getName());
        notificationObject.put("body",text);
        dataObject.put("event",event.getId());
        jsonObject.put("notification",notificationObject);
        jsonObject.put("data",dataObject);
        jsonObject.put("to", fcmToken);
        return jsonObject;
    }

    /**
     * sends the notification using OkHttpClient
     * @param jsonObject object containing all necessary information of an event
     */
    void callApi(JSONObject jsonObject){
        MediaType JSON = MediaType.get("application/json");

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://fcm.googleapis.com/fcm/send")
                .post(RequestBody.create(jsonObject.toString(),JSON))
                .header("Authorization", "Bearer AAAA9JmSg9Q:APA91bG_VZRBkbQa1whOowc_R2F1P8M_RUcDERhZa-YRM-EgSiAaoHBxSV4UO0bETyAvHh7d7P9fPjgIlfPqZcU-_xRKrIW71swZCu-uLSzdf6cravZN6zhs1HvtDt28afiwDevDnJ7b")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

            }
        });
    }


}
