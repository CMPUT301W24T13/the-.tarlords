package com.example.the_tarlords.data.attendance;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.the_tarlords.MainActivity;
import com.example.the_tarlords.data.event.Event;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;

import junit.framework.TestCase;

import java.util.HashMap;

public class AttendanceTest extends TestCase {
    CollectionReference eventsRef = MainActivity.db.collection("Events");
    Event event = new Event("My B-Day", "My House");

    public void testGetAttendanceList() {

    }

    public void testSignUp() {
    }

    public void testRemoveSignUp() {
    }

    public void testCheckIn() {
    }

    public void testRemoveCheckIn() {
    }
}