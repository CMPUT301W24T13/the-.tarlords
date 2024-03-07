package com.example.the_tarlords.ui.profile;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.the_tarlords.R;
import com.example.the_tarlords.data.users.Profile;

public class ProfileFragment extends Fragment {
    private Profile profile;
    private ProfileViewModel mViewModel;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        ImageView profilePhotoImageView = view.findViewById(R.id.image_view_profile);
        EditText firstNameEditText = view.findViewById(R.id.edit_text_first_name);
        EditText lastNameEditText = view.findViewById(R.id.edit_text_last_name)
        EditText phoneEditText = view.findViewById(R.id.exit_text_phone);
        EditText emailEditText = view.findViewById(R.id.edit_text_email);

        if (profile != null) {
            profilePhotoImageView.setImageBitmap(profile.getProfilePhoto());
            firstNameEditText.setText(profile.getFirstName());
            lastNameEditText.setText(profile.getLastName());
            phoneEditText.setText(profile.getPhoneNum());
            emailEditText.setText(profile.getEmail());
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        // TODO: Use the ViewModel
    }

}