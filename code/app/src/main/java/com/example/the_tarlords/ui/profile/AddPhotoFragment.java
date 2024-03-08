package com.example.the_tarlords.ui.profile;

import static com.example.the_tarlords.ui.profile.ProfileFragment.newInstance;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.the_tarlords.R;
import com.example.the_tarlords.data.photo.ProfilePhoto;

public class AddPhotoFragment extends DialogFragment {
    interface AddPhotoDialogListener {
        void removePhoto();
        void takePhoto();
        void uploadPhoto();
    }

    private AddPhotoDialogListener listener;
    private ProfilePhoto profilePhoto;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AddPhotoFragment.AddPhotoDialogListener) {
            listener = (AddPhotoDialogListener) context;
        } else {
            throw new RuntimeException(context + " must implement AddPhotoDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //AddPhotoFragment newFragment = (AddPhotoFragment) newInstance();
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_add_photo, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Camera", (dialog, which) ->{
                    listener.takePhoto();
                })
                .setNeutralButton("Gallery", (dialog, which) ->{
                    listener.uploadPhoto();
                })
                .create();
    }
}
