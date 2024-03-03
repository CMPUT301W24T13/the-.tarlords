package com.example.the_tarlords;

<<<<<<< Updated upstream
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
=======
import android.content.Intent;
>>>>>>> Stashed changes
import android.os.Bundle;
import android.view.View;
import android.view.Menu;

<<<<<<< Updated upstream
import com.example.the_tarlords.data.QR.QRCode;
import com.google.android.material.navigation.NavigationView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
=======
import com.example.the_tarlords.data.QR.QRScanActivity;
import com.google.android.material.navigation.NavigationView;

>>>>>>> Stashed changes
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.example.the_tarlords.databinding.ActivityMainBinding;
<<<<<<< Updated upstream
<<<<<<< Updated upstream
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import java.util.Objects;
=======
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
>>>>>>> Stashed changes
=======
import com.google.firebase.firestore.FirebaseFirestore;
>>>>>>> Stashed changes


public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    public static FirebaseFirestore db;

    private ActivityMainBinding binding;
    private static final int PERMISSION_REQUEST_CAMERA = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        FirebaseApp.initializeApp(this); // what dependencies are needed ?
        db = FirebaseFirestore.getInstance();

    
        setSupportActionBar(binding.appBarMain.toolbar);

        binding.appBarMain.scanQrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
<<<<<<< Updated upstream
                if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
                } else {
                    scanQr();
                }
=======
                Intent intent = new Intent(MainActivity.this, QRScanActivity.class);
                startActivity(intent);
>>>>>>> Stashed changes
            }
        });

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_events, R.id.nav_login, R.id.profileFragment)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void scanQr() {
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setPrompt("Scan QR code");
        intentIntegrator.setOrientationLocked(true); // Enable rotation
        intentIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                String qrValue = result.getContents();
                Toast.makeText(this, "Link: " + qrValue, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Unable to scan", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                scanQr();
            } else {
                Toast.makeText(this, "Please Enable Camera", Toast.LENGTH_SHORT).show();
                //finish();
            }
        }
    }

    //Testing Version 1 -- Rimsha1111
}