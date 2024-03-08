package com.example.the_tarlords.ui.profile;

import androidx.core.view.MenuProvider;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.the_tarlords.MainActivity;
import com.example.the_tarlords.R;

public class ProfileFragment extends Fragment implements MenuProvider {

    //private View view;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requireActivity().addMenuProvider(this);
        TextView nameTV = view.findViewById(R.id.nameTextView);
        TextView phoneTV = view.findViewById(R.id.phoneTextView);
        TextView emailTV = view.findViewById(R.id.emailTextView);

        nameTV.setText(MainActivity.user.getFirstName()+" "+MainActivity.user.getLastName());
        phoneTV.setText(MainActivity.user.getPhoneNum());
        emailTV.setText(MainActivity.user.getEmail());

        EditText nameET = view.findViewById(R.id.nameEditText);
        EditText phoneET = view.findViewById(R.id.phoneEditText);
        EditText emailET = view.findViewById(R.id.emailEditText);

        nameET.setText(MainActivity.user.getFirstName()+" "+MainActivity.user.getLastName());
        phoneET.setText(MainActivity.user.getPhoneNum());
        emailET.setText(MainActivity.user.getEmail());


    }

    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menu.clear();
        menuInflater.inflate(R.menu.options_menu, menu);
        menu.findItem(R.id.editOptionsMenu).setVisible(true);
    }
    @Override
    public void onPrepareMenu(@NonNull Menu menu){
        if (getView().findViewById(R.id.nameTextView).getVisibility()== getView().GONE){
            menu.findItem(R.id.editOptionsMenu).setVisible(false);
            menu.findItem(R.id.saveOptionsMenu).setVisible(true);
            menu.findItem(R.id.cancelOptionsMenu).setVisible(true);
        }
        else {
            menu.findItem(R.id.editOptionsMenu).setVisible(true);
            menu.findItem(R.id.saveOptionsMenu).setVisible(false);
            menu.findItem(R.id.cancelOptionsMenu).setVisible(false);
        }
    }
    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        View view = getView();
        TextView nameTV = view.findViewById(R.id.nameTextView);
        TextView phoneTV = view.findViewById(R.id.phoneTextView);
        TextView emailTV = view.findViewById(R.id.emailTextView);

        EditText nameET = view.findViewById(R.id.nameEditText);
        EditText phoneET = view.findViewById(R.id.phoneEditText);
        EditText emailET = view.findViewById(R.id.emailEditText);

        if (menuItem.getItemId() == R.id.editOptionsMenu){

            nameTV.setVisibility(view.GONE);
            phoneTV.setVisibility(view.GONE);
            emailTV.setVisibility(view.GONE);

            nameET.setVisibility(view.VISIBLE);
            phoneET.setVisibility(view.VISIBLE);
            emailET.setVisibility(view.VISIBLE);

            requireActivity().invalidateMenu();
            return true;
        }
        else if (menuItem.getItemId() == R.id.saveOptionsMenu || menuItem.getItemId()== R.id.cancelOptionsMenu){
            nameTV.setVisibility(view.VISIBLE);
            phoneTV.setVisibility(view.VISIBLE);
            emailTV.setVisibility(view.VISIBLE);

            nameET.setVisibility(view.GONE);
            phoneET.setVisibility(view.GONE);
            emailET.setVisibility(view.GONE);

            requireActivity().invalidateMenu();

            if (menuItem.getItemId() == R.id.saveOptionsMenu){
                MainActivity.user.setFirstName(nameET.getText().toString().split(" ")[0]);
                MainActivity.user.setLastName(nameET.getText().toString().split(" ")[1]);
                MainActivity.user.setPhoneNum(phoneET.getText().toString());
                MainActivity.user.setEmail(emailET.getText().toString());

                nameTV.setText(MainActivity.user.getFirstName()+" "+MainActivity.user.getLastName());
                phoneTV.setText(MainActivity.user.getPhoneNum());
                emailTV.setText(MainActivity.user.getEmail());

                MainActivity.user.sendToFireStore();

                MainActivity.updateNavigationDrawerHeader();

                //TODO: update firebase info

                //TODO: update navigation menu header, check for invalid input or name input with more than one space
            }
            return true;

        }

        return false;
    }
}