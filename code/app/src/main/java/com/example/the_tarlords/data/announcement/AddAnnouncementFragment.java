package com.example.the_tarlords.data.announcement;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AddAnnouncementFragment extends DialogFragment {
    interface AddAnnouncementDialogListener{
        void addAnnouncement(Announcement announcement);
        void editAnnouncement(Announcement announcement,int i);
        void deleteAnnouncement(int i);
    }

    private AddAnnouncementDialogListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof AddAnnouncementDialogListener){
            listener = (AddAnnouncementDialogListener) context;

        }else{
            throw new RuntimeException(context + " must implement AddAnnouncementDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        // todo: create textviews
        // todo: create dialog

        // temp
        return super.onCreateDialog(savedInstanceState);
    }
}