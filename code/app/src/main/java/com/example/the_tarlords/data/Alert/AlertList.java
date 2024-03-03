package com.example.the_tarlords.data.Alert;


import android.widget.ListView;

import java.util.ArrayList;

public class AlertList implements AddAlertDialogListener {
    private ArrayList<Alert> alertDataList;
    private ListView alertList;
    private AlertListAdapter alertListAdapter;

    @Override
    public void addAlert(Alert alert) {
        alertListAdapter.add(alert);
        alertListAdapter.notifyDataSetChanged();
    }
    @Override
    public void deleteAlert(Alert alert) {
        alertListAdapter.remove(alert);
        alertListAdapter.notifyDataSetChanged();
    }

    @Override
    public void editAlert(Alert oldAlert, String newTitle, String newMessage) {
        oldAlert.setTitle(newTitle);
        oldAlert.setMessage(newMessage);
    }
}
