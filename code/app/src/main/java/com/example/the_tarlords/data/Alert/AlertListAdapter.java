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

    private int layoutResource;
    public AlertListAdapter(Context context, ArrayList<Alert> alerts, int layoutResource) { // layout resource is something like R.layout....
        super(context, 0, alerts);
        this.layoutResource = layoutResource;
    }

    public AlertListAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(super.getContext()).inflate(layoutResource, parent, false);
        } else {
            view = convertView;
        }
        Alert alert = super.getItem(position);
        TextView event = view.findViewById(R.id.event_text);
        TextView message = view.findViewById(R.id.message_text);
        TextView title = view.findViewById(R.id.title_text);
        TextView ldt = view.findViewById(R.id.ldt_text);

        event.setText(alert.getEvent().getName());
        message.setText(alert.getMessage());
        title.setText(alert.getTitle());
        ldt.setText(alert.getCurrentDateTime());


        return view;
    }
}
