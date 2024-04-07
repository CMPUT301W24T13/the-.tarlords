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

import java.util.ArrayList;

/**
 * List adapter for ListView in AlertFragment
 */

public class AlertListAdapter extends ArrayAdapter<Alert> {

    private int layoutResource;

    /**
     * constructor for the AlertListAdapter
     * @param context
     * @param alerts
     * @param layoutResource
     */
    public AlertListAdapter(Context context, ArrayList<Alert> alerts, int layoutResource) { // layout resource is something like R.layout....
        super(context, 0, alerts);
        this.layoutResource = layoutResource; // may or may not need
    }
    // may or may not need
    public AlertListAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    /**
     *
     * @param position The position of the item within the adapter's data set of the item whose view
     *        we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *        is non-null and of an appropriate type before using. If it is not possible to convert
     *        this view to display the correct data, this method can create a new view.
     *        Heterogeneous lists can specify their number of view types, so that this View is
     *        always of the right type (see {@link #getViewTypeCount()} and
     *        {@link #getItemViewType(int)}).
     * @param parent The parent that this view will eventually be attached to
     * @return
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(super.getContext()).inflate(R.layout.fragment_alert_list_item, parent, false);
        } else {
            view = convertView;
        }
        Alert alert = super.getItem(position);

        TextView message = view.findViewById(R.id.tv_alert_message);
        TextView title = view.findViewById(R.id.tv_alert_title);
        TextView ldt = view.findViewById(R.id.tv_alert_timestamp);

        message.setText(alert.getMessage());
        title.setText(alert.getTitle());
        ldt.setText(alert.getCurrentDateTime());

        return view;
    }
}