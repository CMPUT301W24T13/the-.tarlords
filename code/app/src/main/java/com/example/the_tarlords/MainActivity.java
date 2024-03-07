package com.example.the_tarlords;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.the_tarlords.data.users.User;
import com.example.the_tarlords.ui.profile.ProfileFragment;
import com.google.android.material.navigation.NavigationView;
import com.example.the_tarlords.data.QR.QRScanActivity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.example.the_tarlords.databinding.ActivityMainBinding;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;


import java.util.ArrayList;
import java.util.UUID;



public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    public static FirebaseFirestore db = FirebaseFirestore.getInstance();
    // Create a reference to the users collection
    CollectionReference usersRef = db.collection("Users");

    //TODO: shouldn't be hardcoded by end
    public static User user;
    private String userId;
    private View hView;


    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        setSupportActionBar(binding.appBarMain.toolbar);

        binding.appBarMain.scanQrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, QRScanActivity.class);
                startActivity(intent);
            }
        });
        /**
         * Please do not move these next 3 lines BELOW THE USER STUFF
         */
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        hView = navigationView.getHeaderView(0);

        /**
         * THIS IS THE USER STUFF
         * It uses the device id to check if a new user needs to be generated or not
         */
        // Check if the device id is already generated and stored
        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        //you can get the user id if the user already has used the app once before , do what you need with it
        //userId = preferences.getString("user_id", null);
        userId = null;
        if (userId == null) {
            // user has not used app before
            // Generate a new user ID (you can use any logic to generate a unique ID)
            userId = generateNewUserId();

            // Save the user ID locally
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("user_id", userId);
            editor.apply();
            // the profile fields are going to have to be filled with some default info the first time, but the ID is the one we generated
            user = new User(userId,"Khushi","Test","780-999-9999","khushi.test@ualberta.ca");
            //upload default user info , with our user ID to fire base
            addUserToFireStore(user);
            // Update UI with default user information
            updateNavigationDrawerHeader();
            // If it's the first launch, navigate to a different fragment
            navigateToYourFirstFragment();
        }else{
            //user has been here before
            String finalUserId = userId;
            Log.d("debug", userId);
            usersRef.whereEqualTo("userId", userId).get()
                    .addOnSuccessListener(querySnapshot -> {
                        if (!querySnapshot.isEmpty()) {
                            // User found, documentSnapshot contains user data
                            DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                            user = documentSnapshot.toObject(User.class);
                            // Now you can use 'user' object
                            // Update UI with default user information
                            updateNavigationDrawerHeader();
                        } else {
                            Log.d("debug", "didn't find a user");
                            // This is a case where user has used app on device but user info is not on firebase yet (my case, developer)
                            user = new User(finalUserId,"khushi","lad","780-111-1111","john.doe@ualberta.ca");
                            // Update UI with default user information
                            updateNavigationDrawerHeader();
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Handle failure
                        Log.e("debug", "failed to get the document",e);
                    });

        }



        /**
         * slide out nav bar set-up
         * **/


        //TextView name = hView.findViewById(R.id.profileName);
        //TextView phoneNum = hView.findViewById(R.id.phoneNumber);
        //TextView email = hView.findViewById(R.id.email);
        //TODO: implement profile picture
        //name.setText(MainActivity.user.getFirstName()+" "+MainActivity.user.getLastName());
        //phoneNum.setText(MainActivity.user.getPhoneNum());
        //email.setText(MainActivity.user.getEmail());

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.eventListFragment, R.id.eventOrganizerListFragment, R.id.profileFragment)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }
    
    // User id generator for the sharedPreferences stuff
    @SuppressLint("HardwareIds")
    private String generateNewUserId() {
        // Replace with your user logic to generate an ID
        return Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
    }
    private void navigateToYourFirstFragment() {
        // Replace 'YourFirstFragment' with the actual name of your first fragment
        ProfileFragment fragment= new ProfileFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment_content_main, fragment); // R.id.fragment_container is the ID of your fragment container
        fragmentTransaction.addToBackStack(null); // Optional: adds the transaction to the back stack
        fragmentTransaction.commit();
    }
    private void addUserToFireStore(User user){
        // Add the new user document to Firestore
        //MAJOR NOTE THIS AUTOMATICALLY SETS THE DOC ID TO USER ID AND I DONT KNOW IF THAT WOULD BE A PROBLEM
        usersRef.document(userId).set(user)
                .addOnSuccessListener(aVoid -> {
                    // Document successfully added
                    Log.d("debug", "User added successfully to Firestore");
                })
                .addOnFailureListener(e -> {
                    // Handle the failure
                    Log.e("debug", "Error adding user to Firestore", e);
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    /*@Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        switch (item.getItemId()){
            case R.id.action
        }
    }*/

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    private void updateNavigationDrawerHeader() {
        // Set navigation drawer header information based on the user object
        if (user != null) {
            TextView name = hView.findViewById(R.id.profileName);
            TextView phoneNum = hView.findViewById(R.id.phoneNumber);
            TextView email = hView.findViewById(R.id.email);

            name.setText(user.getFirstName() + " " + user.getLastName());
            phoneNum.setText(user.getPhoneNum());
            email.setText(user.getEmail());
        } else {
            Log.e("debug", "User object is null");
            // Handle the case where the User object is null
            user = new User(userId,"khushi","null","780-111-1111","john.doe@ualberta.ca");
        }
    }

    //Testing Version 1 -- Rimsha1111
}