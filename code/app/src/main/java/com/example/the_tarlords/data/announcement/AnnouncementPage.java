package com.example.the_tarlords.data.announcement;

import android.widget.ListView;

import java.util.ArrayList;

public class AnnouncementPage implements AddAnnouncementFragment.AddAnnouncementDialogListener{

    private ArrayList<Announcement> datalist;
    private ListView announcementList;
    private AnnouncementArrayAdapter announcementAdapter;


    @Override
    public void addAnnouncement(Announcement announcement) {
        announcementAdapter.add(announcement);
        update();

    }

    @Override
    public void editAnnouncement(Announcement announcement, int i) {
        Announcement prev = datalist.get(i);
        prev.setHeader(announcement.getHeader());
        prev.setContent(announcement.getContent());

        update();

    }

    @Override
    public void deleteAnnouncement(int i) {
        datalist.remove(i);
        update();

    }

    private void update(){
        announcementAdapter.notifyDataSetChanged();
    }
}