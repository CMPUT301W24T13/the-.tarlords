package com.example.the_tarlords.data.Alert;

import android.widget.ListView;

import com.example.the_tarlords.data.event.AddEventFragment;
import com.example.the_tarlords.data.event.Event;

import java.util.ArrayList;

public class AlertList implements AddAlertFragment.AddAlertDialogListener {
    private ArrayList<Alert> alertDataList;
    private ListView alertList;
    private AlertListAdapter alertListAdapter;

    public void addAlert(Alert alert) {
        alertListAdapter.add(alert);
        alertListAdapter.notifyDataSetChanged();
    }

    @Override
    public void editAlert(Alert oldAlert, Alert newAlert) {
        int index = alertDataList.indexOf(oldAlert);
        if (index != -1) {
            alertDataList.set(index , newAlert);
            alertListAdapter.notifyDataSetChanged();
        }
    }
}
