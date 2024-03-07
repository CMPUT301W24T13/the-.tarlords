package com.example.the_tarlords.data.Alert;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.the_tarlords.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;

public class AlertFragment extends Fragment implements AddAlertDialogListener{

    private ArrayList<Alert> alertDataList;
    private AlertListAdapter alertListAdapter;
    public AlertFragment(ArrayList<Alert> alertDataList){
        this.alertDataList = alertDataList;
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
        View view = inflater.inflate(R.layout.fragment_alert, container, false);

        ListView listView = view.findViewById(R.id.alert_list);
        alertListAdapter = new AlertListAdapter(requireContext(), alertDataList,1);
        listView.setAdapter(alertListAdapter);
        //alertList = new AlertList(this, alertDataList, listView.findViewById(R.id.alert_list));


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
    public void addAlert(Alert alert) {
        //alertListAdapter.add(alert);
        alertDataList.add(alert);

        refreshList();
    }
    @Override
    public void deleteAlert(Alert alert) {
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
        Collections.sort(alertDataList);
        alertListAdapter.notifyDataSetChanged();


    }
}
