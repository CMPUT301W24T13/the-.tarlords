package com.example.the_tarlords;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.os.Bundle;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.the_tarlords.data.Alert.Alert;
import com.example.the_tarlords.data.Alert.AlertCallback;
import com.example.the_tarlords.data.event.Event;
import com.example.the_tarlords.data.photo.EventPoster;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class EventTest {

    @Before
    public void setup() {
        Event event = new Event("Test Event", "Test Location");
        event.setId("123456");
        event.setOrganizerId("organizer123");
    }
    @Test
    public void testSetAndGetOrganizerId() {
        Event event = new Event();
        String organizerId = "123";
        event.setOrganizerId(organizerId);
        assertEquals(organizerId, event.getOrganizerId());
    }

    @Test
    public void testSetAndGetId() {
        Event event = new Event();
        String id = "456";
        event.setId(id);
        assertEquals(id, event.getId());
    }

    @Test
    public void testSetAndGetName() {
        Event event = new Event();
        String name = "Event Name";
        event.setName(name);
        assertEquals(name, event.getName());
    }

    @Test
    public void testSetAndGetLocation() {
        Event event = new Event();
        String location = "Event Location";
        event.setLocation(location);
        assertEquals(location, event.getLocation());
    }
    @Test
    public void testSetAndGetStartTime() {
        Event event = new Event();
        String startTime = "10:00 AM";
        event.setStartTime(startTime);
        assertEquals(startTime, event.getStartTime());
    }

    @Test
    public void testSetAndGetStartDate() {
        Event event = new Event();
        String startDate = "2024-04-03";
        event.setStartDate(startDate);
        assertEquals(startDate, event.getStartDate());
    }

    @Test
    public void testSetAndGetEndTime() {
        Event event = new Event();
        String endTime = "12:00 PM";
        event.setEndTime(endTime);
        assertEquals(endTime, event.getEndTime());
    }

    @Test
    public void testSetAndGetEndDate() {
        Event event = new Event();
        String endDate = "2024-04-03";
        event.setEndDate(endDate);
        assertEquals(endDate, event.getEndDate());
    }
    @Test
    public void testSetAndGetQrCode() {
        Event event = new Event();
        String qrCode = "abc123";
        event.setQrCode(qrCode);
        assertEquals(qrCode, event.getQrCode());
    }

    @Test
    public void testSetAndGetPoster() {
        Event event = new Event();
        EventPoster poster = new EventPoster("filename", null, event);
        event.setPoster(poster);
        assertEquals(poster, event.getPoster());

    }

    @Test
    public void testSetAndGetMaxSignUps() {
        Event event = new Event();
        int maxSignUps = 100;
        event.setMaxSignUps(maxSignUps);
        assertEquals(maxSignUps, (int) event.getMaxSignUps());
    }

    @Test
    public void testSetAndGetSignUps() {
        Event event = new Event();
        int signUps = 50;
        event.setSignUps(signUps);
        assertEquals(signUps, (int) event.getSignUps());
    }

    @Test
    public void testSetAndGetCheckIns() {
        Event event = new Event();
        int checkIns = 30;
        event.setCheckIns(checkIns);
        assertEquals(checkIns, (int) event.getCheckIns());
    }

    @Test
    public void testSetAndGetPosterData() {
        Event event = new Event();
        String posterData = "posterData";
        event.setPosterData(posterData);
        assertEquals(posterData, event.getPosterData());
    }

    @Test
    public void setPosterFromData() {
        Event event = new Event();
        EventPoster poster = new EventPoster("filename",null,event);
        poster.autoGenerate();
        String posterData = poster.getPhotoDataFromBitmap();
        event.setPosterFromData();
        assertEquals(posterData, event.getPoster().getPhotoDataFromBitmap());
    }

    @Test
    public void getAlertList() {
        Event event = new Event();
        Alert alert = new Alert("test alert title","message","currentDateTime");
        event.setAlert(alert);
        event.getAlertList(new AlertCallback() {
            @Override
            public void onAlertsLoaded(ArrayList<Alert> alertList) {
                assertEquals(1, alertList.stream().count());
                assertEquals("test alert title", alertList.get(0).getTitle());
            }
        });
    }

    @Test
    public void setAlert() {
        Event event = new Event();
        Alert alert = new Alert("test alert title2","message2","currentDateTime2");
        event.setAlert(alert);
        event.getAlertList(new AlertCallback() {
            @Override
            public void onAlertsLoaded(ArrayList<Alert> alertList) {
                assertEquals(1, alertList.stream().count());
                assertEquals("test alert title2", alertList.get(1).getTitle());
            }
        });
    }

    @Test
    public void describeContents() {
        Event event = new Event();
        assertEquals(0,event.describeContents());
    }

    @Test
    public void writeToParcel() {
        Event event = new Event();
        event.setName("parcelEvent");
        Bundle b = new Bundle();
        b.putParcelable("eventTest", event);
        Event test = b.getParcelable("eventTest");
        assertEquals("parcelEvent",test.getName());
    }


    @Test
    public void sendToFirestore() {
        /*event.setName("firestoreTest");
        assertSucceeds(event.sendToFirestore());
        verify(documentReference).set(any())*/
        /*Event event = new Event("name", "location");
        event.setId("eventId");

        when(db.collection("Events")).thenReturn(collectionReference);
        when(collectionReference.document(any(String.class))).thenReturn(documentReference);
        when(documentReference.set(any())).thenReturn(Tasks.forResult(null));

        event.sendToFirestore();

        verify(documentReference).set(any());*/


    }

    /*@Test
    public void removeFromFirestore() {
        Event event = new Event("name", "location");
        event.setId("eventId");

        CollectionReference attendanceRef = mock(CollectionReference.class);
        CollectionReference alertsRef = mock(CollectionReference.class);

        when(db.collection("Events/"+ event.getId() +"/Attendance")).thenReturn(attendanceRef);
        when(db.collection("Events/"+ event.getId() +"/alerts")).thenReturn(alertsRef);

        when(attendanceRef.get()).thenReturn(Tasks.forResult(querySnapshot));
        when(alertsRef.get()).thenReturn(Tasks.forResult(querySnapshot));

        when(querySnapshot.iterator()).thenReturn(new ArrayList<QueryDocumentSnapshot>().iterator());

        when(collectionReference.document(any(String.class))).thenReturn(documentReference);
        when(documentReference.delete()).thenReturn(Tasks.forResult(null));

        event.removeFromFirestore();

        verify(documentReference).delete();
        verify(attendanceRef).document(any(String.class));
        verify(alertsRef).document(any(String.class));
    }*/


    @Test
    public void testSetAndGetPosterIsDefault() {
        Event event = new Event();
        boolean posterIsDefault = true;
        event.setPosterIsDefault(posterIsDefault);
        assertEquals(posterIsDefault, event.getPosterIsDefault());
    }
    @Test
    public void testReachedMaxCap() {
        Event event = new Event("Test Event", "Test Location");
        event.setMaxSignUps(10);
        event.setSignUps(5);
        assertFalse(event.reachedMaxCap());

        event.setSignUps(10);
        assertTrue(event.reachedMaxCap());

        // Test when maxSignUps is null
        event.setMaxSignUps(null);
        assertFalse(event.reachedMaxCap());


    }

    @Test
    public void testMakeNewDocID() {
        Event event = new Event("Test Event", "Test Location");
        assertNotNull(event.makeNewDocID());
    }

    /**
     * This does not work but if you want to have a crack at it
     */
    /*
    @Test
    public void testSendToFirebase() {
        // Mock the behavior of FirebaseFirestore.getInstance() to return our mock instance
        when(FirebaseFirestore.getInstance()).thenReturn(firebaseFirestore);

        // Mock the behavior of collection().document() to return our mock document reference
        when(firebaseFirestore.collection("Events").document("123456")).thenReturn(documentReference);

        // Mock the behavior of documentReference.set() to return a successful Task
        when(documentReference.set(any(Map.class))).thenReturn(mock(Task.class));

        // Call the method under test
        event.sendToFirebase();

        // Verify that set() was called on the documentReference with the correct data
        Map<String, Object> expectedData = new HashMap<>();
        expectedData.put("id", "123456");
        expectedData.put("name", "Test Event");
        expectedData.put("location", "Test Location");
        expectedData.put("organizerId", "organizer123");
        // Add more fields as needed
        verify(documentReference).set(expectedData);
    }
    */


}
