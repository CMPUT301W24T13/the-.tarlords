package com.example.the_tarlords.ui.profile;

import androidx.core.view.MenuProvider;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.the_tarlords.MainActivity;
import com.example.the_tarlords.R;
import com.example.the_tarlords.data.users.User;

public class ProfileFragment extends Fragment implements MenuProvider {
    private User user = MainActivity.user;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView profilePhotoImageView = view.findViewById(R.id.image_view_profile);
        EditText firstNameEditText = view.findViewById(R.id.edit_text_first_name);
        EditText lastNameEditText = view.findViewById(R.id.edit_text_last_name);
        EditText phoneEditText = view.findViewById(R.id.edit_text_phone);
        EditText emailEditText = view.findViewById(R.id.edit_text_email);

        if (user != null) {
            profilePhotoImageView.setImageBitmap(user.getProfilePhoto().getBitmap());
            firstNameEditText.setText(user.getFirstName());
            lastNameEditText.setText(user.getLastName());
            phoneEditText.setText(user.getPhoneNum());
            emailEditText.setText(user.getEmail());
        }

        Button addPhotoButton = view.findViewById(R.id.button_add_profile_photo);
        addPhotoButton.setOnClickListener(v -> {
            new AlertDialog.Builder(this.getContext())
                    .setTitle("Where would you like to upload a profile photo from?")
                    .setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            TakePhotoActivity takePhoto = new TakePhotoActivity();
                        }
                    })
                    .setNeutralButton("Gallery", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            UploadPhotoActivity uploadPhoto = new UploadPhotoActivity();
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .create()
                    .show();
        });
    }

    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menu.clear();
        menuInflater.inflate(R.menu.options_menu, menu);
        menu.findItem(R.id.editOptionsMenu).setVisible(true);
    }

    @Override
    public void onPrepareMenu(@NonNull Menu menu) {
        if(getView().findViewById(R.id.button_edit_profile).getVisibility() == getView().GONE) {
            menu.findItem(R.id.editOptionsMenu).setVisible(false);
            menu.findItem(R.id.saveOptionsMenu).setVisible(true);
            menu.findItem(R.id.cancelOptionsMenu).setVisible(true);
        } else {
            menu.findItem(R.id.editOptionsMenu).setVisible(true);
            menu.findItem(R.id.saveOptionsMenu).setVisible(false);
            menu.findItem(R.id.cancelOptionsMenu).setVisible(false);
        }
    }

    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        View view = getView();
        ImageView profilePhotoImageView = view.findViewById(R.id.image_view_profile);
        Button addProfilePhotoButton = view.findViewById(R.id.button_add_profile_photo);
        EditText firstNameEditText = view.findViewById(R.id.edit_text_first_name);
        EditText lastNameEditText = view.findViewById(R.id.edit_text_last_name);
        EditText phoneEditText = view.findViewById(R.id.edit_text_phone);
        EditText emailEditText = view.findViewById(R.id.edit_text_email);

        if (menuItem.getItemId() == R.id.editOptionsMenu) {
            profilePhotoImageView.setVisibility(View.INVISIBLE);
            addProfilePhotoButton.setVisibility(View.VISIBLE);
            firstNameEditText.setClickable(true);
            lastNameEditText.setClickable(true);
            phoneEditText.setClickable(true);
            emailEditText.setClickable(true);

            requireActivity().invalidateMenu();
            return true;
        } else {
            profilePhotoImageView.setVisibility(View.VISIBLE);
            addProfilePhotoButton.setVisibility(View.GONE);
            firstNameEditText.setClickable(false);
            lastNameEditText.setClickable(false);
            phoneEditText.setClickable(false);
            emailEditText.setClickable(false);

            requireActivity().invalidateMenu();

            if (menuItem.getItemId() == R.id.saveOptionsMenu) {
                user.setFirstName(firstNameEditText.getText().toString());
                user.setLastName(lastNameEditText.getText().toString());
                user.setPhoneNum(phoneEditText.getText().toString());
                user.setEmail(emailEditText.getText().toString());

                Bitmap bitmap = ((BitmapDrawable)profilePhotoImageView.getDrawable()).getBitmap();
                user.getProfilePhoto().setBitmap(bitmap);

                MainActivity.user.sendToFireStore();
                MainActivity.updateNavigationDrawerHeader();
            }
            return true;
        }
        //return false;
    }
}