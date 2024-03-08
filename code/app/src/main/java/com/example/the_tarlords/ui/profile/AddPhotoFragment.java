package com.example.the_tarlords.ui.profile;

import android.graphics.Bitmap;

public class AddPhotoFragment {
    interface AddPhotoDialogListener {
        void addPhoto();
        void removePhoto();
    }

    private AddPhotoDialogListener listener;
    private Bitmap bitmap;
    public AddPhotoFragment(Bitmap bitmap) {
        this.bitmap = bitamp;
    }
}
