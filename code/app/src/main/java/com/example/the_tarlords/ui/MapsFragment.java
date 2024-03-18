package com.example.the_tarlords.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.the_tarlords.MainActivity;
import com.example.the_tarlords.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.CollectionReference;

public class MapsFragment extends Fragment implements MenuProvider {

    //TODO dont hardcode the event id

    CollectionReference checkInsRef = MainActivity.db.collection("Events/"+ "1" +"/checkIns");


    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            //call a method to add markers from locations stored in Firebase
            addMarkers(googleMap);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
    // TODO: not disabling the menu options, should not should any of them
    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menu.clear();

        //link options menu xml
        menuInflater.inflate(R.menu.options_menu, menu);

        //disable all menu options
        menu.findItem(R.id.editOptionsMenu).setVisible(false);
        menu.findItem(R.id.attendanceOptionsMenu).setVisible(false);
        menu.findItem(R.id.deleteOptionsMenu).setVisible(false);
        menu.findItem(R.id.mapOptionsMenu).setVisible(false);
        menu.findItem(R.id.anouncementsOptionsMenu).setVisible(false);
    }

    /**
     * Mandatory empty method because fragment
     * implement menu provider
     * @param menuItem the menu item that was selected
     * @return
     */
    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }

    /**
     * Adds location markers of all the check-in attendees
     * at that event
     * @param googleMap
     */
    public void addMarkers(GoogleMap googleMap) {
        LatLng sydney = new LatLng(-34, 151);
        googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        // get one from firebase

    }


}