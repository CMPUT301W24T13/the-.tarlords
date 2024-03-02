package com.example.the_tarlords.data.QR;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Objects;

public class Event extends AppCompatActivity {
    private FirebaseFirestore db;
    private CollectionReference eventsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        db = FirebaseFirestore.getInstance();
        eventsRef = db.collection("Events");

        String QrID = getIntent().getStringExtra("QrID");

        eventsRef.addSnapshotListener((querySnapshots, error) -> {
            if (error != null) {
                Log.e("Firestore", error.toString());
                return;
            }
            if (querySnapshots != null) {
                for (QueryDocumentSnapshot doc: querySnapshots) {
                    String eventID = doc.getId();
                    String CheckInQR = doc.getString("CheckInQR");
                    String EventInfoQR = doc.getString("EventInfoQR");
                    Log.d("Firestore", String.format("Event(%s, %s) fetched", CheckInQR, EventInfoQR));

                    if (Objects.equals(QrID, CheckInQR) || Objects.equals(QrID, EventInfoQR)) {
                        // Update the TextView with the value of "LogInQR"
                        TextView textView = findViewById(R.id.text1);
                        String EventName = doc.getString("EventName");
                        textView.setText(EventName);
                        break;
                    }
                }
            }

            TextView textView = findViewById(R.id.text1);
            textView.setText("Reset");
        });
    }
}

