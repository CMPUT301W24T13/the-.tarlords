package com.example.the_tarlords.data.Alert;


import android.widget.ListView;

import java.util.ArrayList;

public class AlertList implements AddAlertFragment.AddAlertDialogListener {
    private ArrayList<Alert> alertDataList;
    private ListView alertList;
    private AlertListAdapter alertListAdapter;

    public void addAlert(Alert alert) {
        alertListAdapter.add(alert);
        alertListAdapter.notifyDataSetChanged();
    }
    /* is this even right????
    public void deleteAlert(Alert alert) {
        alertListAdapter.delete(alert);
        alsertListAdapter.notifyDataSetChanged();
    }
    */

    @Override
    public void editAlert(Alert oldAlert, String newMessage) {
        oldAlert.setMessage(newMessage);
    }
}
