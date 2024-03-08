package com.example.the_tarlords;

import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.the_tarlords.data.users.User;
import com.google.android.material.navigation.NavigationView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.example.the_tarlords.databinding.ActivityMainBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.Calendar;


public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    public static FirebaseFirestore db = FirebaseFirestore.getInstance();

    //TODO: shouldn't be hardcoded by end
    public static User user = new User("1","john","doe","780-111-1111","john.doe@ualberta.ca");
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
                ScanOptions scanOptions = new ScanOptions();
                barcodeLauncher.launch(scanOptions);
            }

            private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(
                    new ScanContract(),
                    result -> {
                        if (result.getContents() != null) {
                            Toast.makeText(getApplicationContext(), "scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "cancelled", Toast.LENGTH_SHORT).show();
                        }
                    }
            );
        });

        /**
         * slide out nav bar set-up
         * **/

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        View hView = navigationView.getHeaderView(0);
        TextView name = hView.findViewById(R.id.profileName);
        TextView phoneNum = hView.findViewById(R.id.phoneNumber);
        TextView email = hView.findViewById(R.id.email);
        //TODO: implement profile picture
        name.setText(MainActivity.user.getFirstName()+" "+MainActivity.user.getLastName());
        phoneNum.setText(MainActivity.user.getPhoneNum());
        email.setText(MainActivity.user.getEmail());

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.eventListFragment, R.id.eventOrganizerListFragment, R.id.profileFragment)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
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

    //Testing Version 1 -- Rimsha1111
}