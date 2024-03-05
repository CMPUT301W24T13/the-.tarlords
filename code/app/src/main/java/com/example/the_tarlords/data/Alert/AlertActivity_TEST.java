package com.example.the_tarlords.data.Alert;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.the_tarlords.R;
import com.example.the_tarlords.data.announcement.Announcement;
import com.example.the_tarlords.data.event.Event;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class AlertActivity_TEST extends AppCompatActivity implements AddAlertDialogListener {
    AlertList alertList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);


        ArrayList<Alert> dataList = new ArrayList<>();
        dataList.add(new Alert("title1","message1",new Event("name1","location1",null,null)));
        dataList.add(new Announcement("title2","announcement",new Event("name1","location1",null,null)));
        alertList = new AlertList(this,dataList,findViewById(R.id.alert_list));

        FloatingActionButton fab = findViewById(R.id.button_add_alert);
        fab.setOnClickListener(v -> {
            // calls the add book fragment constructer with filler values
            new AddAlertFragment(null).show(getSupportFragmentManager(), "Add alert");
        });

    }


    @Override
    public void addAlert(Alert alert) {
        alertList.addAlert(alert);

    }

    @Override
    public void editAlert(Alert oldAlert, String newTitle, String newMessage) {
        alertList.editAlert(oldAlert,newTitle,newMessage);

    }

    @Override
    public void deleteAlert(Alert alert) {
        alertList.deleteAlert(alert);

    }

}