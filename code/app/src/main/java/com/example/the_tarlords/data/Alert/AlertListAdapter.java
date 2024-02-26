package com.example.the_tarlords.data.Alert;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.the_tarlords.R;
import com.example.the_tarlords.data.event.Event;

import java.util.ArrayList;

public class AlertListAdapter extends ArrayAdapter<Alert> {
    public AlertListAdapter(Context context, ArrayList<Alert> alerts) {
        super(context, 0, alerts);
    }

    public AlertListAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(super.getContext()).inflate(R.layout.content_alerts, parent, false);
        } else {
            view = convertView;
        }
        Alert alert = super.getItem(position);
        TextView event = view.findViewById(R.id.event_text);
        TextView message = view.findViewById(R.id.message_text);

        event.setText(alert.getEvent().getName());
        message.setText(alert.getMessage());

        return view;
    }
}
