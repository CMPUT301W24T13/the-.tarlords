package com.example.the_tarlords.data.Alert;

import java.util.ArrayList;

/**
 * For classes that have Callback implementation for alerts
 */

public interface AlertCallback {
    void onAlertsLoaded(ArrayList<Alert> alertList);
}
