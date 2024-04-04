package com.example.the_tarlords.data.Alert;


import android.content.Context;
import android.widget.ListView;

import com.example.the_tarlords.R;

import java.util.ArrayList;
import java.util.Collections;

public class AlertList implements AddAlertDialogListener {
    private ArrayList<Alert> alertDataList;
    private ListView alertList;
    private AlertListAdapter alertListAdapter;
    private int layout;

    /**
     * contructor of an AlertList
     * @param context --> Context
     * @param alertDataList --> array list of alerts
     * @param listView --> list view to be used
     */
    public AlertList(Context context, ArrayList<Alert> alertDataList, ListView listView){
        this.alertList = listView;
        this.alertDataList = alertDataList;
        alertListAdapter = new AlertListAdapter(context, alertDataList, R.layout.fragment_alert_list);
        alertList.setAdapter(alertListAdapter);


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