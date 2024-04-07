package com.example.the_tarlords.data.event;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.the_tarlords.MainActivity;
import com.example.the_tarlords.data.Alert.Alert;
import com.example.the_tarlords.data.Alert.AlertCallback;
import com.example.the_tarlords.data.photo.EventPoster;
import com.example.the_tarlords.data.users.Attendee;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This class defines an event
 * UUID type for event attribute makes sure that everytime an event object is created it has a unique id
 * Not sure how QRcode will work , is it initialized when the event is created, or can it be set after being created
 */


/* NOTE FOR KHUSHI AND GRACE:

    2. Need to connect event location to the Map Class.
*/
public class Event implements Parcelable {
    String name;
    String location;
    String startTime;
    String endTime;
    String startDate;
    String endDate;
    String id;
    String organizerId;
    private String qrCode;
    private EventPoster poster;
    private String posterData;
    private Boolean posterIsDefault;
    private String additionalInfo;
    Integer maxSignUps;
    public Integer signUps;
    public Integer checkIns;
    private CollectionReference usersRef = MainActivity.db.collection("Users");

    private static CollectionReference eventsRef = MainActivity.db.collection("Events");

    public Event(String name, String location, String id, String startTime, String endTime, String startDate) {
        this.name = name;
        this.location = location;
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startDate = startDate;
    }
    public Event(String name, String location) {
        this.name = name;
        this.location = location;
        makeNewDocID();
    }
    public Event(String name, String location, String id) {
        this.name = name;
        this.location = location;
        this.id = id;
    }
    public Event (Parcel in) {
        name = in.readString();
        location = in.readString();
        id = in.readString();
        startTime = in.readString();
        endTime = in.readString();
        startDate = in.readString();
        maxSignUps = in.readInt();
        signUps = in.readInt();
        checkIns = in.readInt();
        additionalInfo = in.readString();
        endDate = in.readString();
        posterData = in.readString();
        organizerId = in.readString();
    }
    public Event (){};

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    public void setOrganizerId(String organizerId) {
        this.organizerId = organizerId;
    }
    public String getOrganizerId() {
        return organizerId;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getId() {
        return id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getQrCode() {
        return qrCode;
    }


    public EventPoster getPoster() {
      if (poster == null && posterData!=null) {
            setPosterFromData();
        }
        return poster;
    }

    public void setPoster(EventPoster poster) {
        this.poster = poster;
    }

    public Integer getMaxSignUps() {
        return maxSignUps;
    }

    public void setMaxSignUps(Integer maxSignUps) {
        this.maxSignUps = maxSignUps;
    }
    public void setCheckIns(Integer checkIns){
        this.checkIns = checkIns;
    }
    public Integer getCheckIns(){return checkIns;}
    public Integer getSignUps() {return signUps;}

    public void setSignUps(Integer signUps) {
        this.signUps = signUps;
    }
    /**
     * Gets profile photo data firestore by converting the base 64 string stored in firestore
     * to a bitmask, then setting the profile photo to have that bitmask.
     * @return String base 64 profile photo data
     */
    public String getPosterData() {
        return posterData;
    }
    public void setPosterFromData() {
        poster = new EventPoster(id,null,this);
        poster.setBitmapFromPhotoData(posterData);
        posterData=posterData;
    }
    public boolean reachedMaxCap() {
        if (signUps == null || maxSignUps ==-1){
            return false;
        } else {
            return maxSignUps <= signUps;
        }
    }

    public ArrayList<Alert> getAlertList(AlertCallback callback){
        CollectionReference alertRef = MainActivity.db.collection("Events/"+ id +"/alerts");
        ArrayList<Alert> alertList = new ArrayList<>();
        alertRef.orderBy("timestamp", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot alertDoc : task.getResult()) {
                        Alert alert = new Alert(alertDoc.getString("title"), alertDoc.getString("message"), alertDoc.getString("currentDateTime"));
                        //alert.setCurrentDateTime(alertDoc.getString("currentDateTime"));
                        alert.setId(alertDoc.getId());
                        alertList.add(alert);

                    }
                    callback.onAlertsLoaded(alertList);
                } else {
                    Log.d("firestore", "Error getting documents: ", task.getException());
                }
            }
        });

    return alertList;

    }

    //test, used to write alerts to firebase
    public void setAlert(Alert alert) {
        CollectionReference alertRef = MainActivity.db.collection("Events/"+ id +"/alerts");
        Map<String, Object> alertMap = new HashMap<>();
        alertMap.put("title", alert.getTitle());
        alertMap.put("message", alert.getMessage());
        alertMap.put("currentDateTime", alert.getCurrentDateTime());
        alertMap.put("timestamp", FieldValue.serverTimestamp());
        alertRef.add(alertMap);

        Log.d("alert adding","working");
    }

    /**
     * Mandatory method for Parcelable interface.
     * @return int
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Mandatory method for Parcelable interface.
     * Allows event objects to be bundled and passed as arguments between fragments.
     * ***** Must include all fields to be passed through *****
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        //add additional fields as necessary
        //additional fields must be added to the Event(Parcelable) constructor
        //all fields must be in the same order as the Event(Parcelable) constructor
        dest.writeString(name);
        dest.writeString(location);
        dest.writeString(id);
        dest.writeString(startTime);
        dest.writeString(endTime);
        dest.writeString(startDate);
        dest.writeInt(maxSignUps);
        dest.writeInt(signUps);
        dest.writeInt(checkIns);
        dest.writeString(additionalInfo);
        dest.writeString(endDate);
        dest.writeString(posterData);
        dest.writeString(organizerId);
    }

    /**
     * Generates an event id for a new event based off an auto-generated firestore doc id.
     * @return String of the event id
     */
    public String makeNewDocID() {
        DocumentReference ref = eventsRef.document(); //get new document from firestore
        id = ref.getId(); //assign event id to doc id
        return id;
    }

    /**
     * Uploads event data to fire store via a hash table.
     * Includes: id, name, location, startDate, startTime, endTime, organizerId, maxSignUps,
     * qrCodeCheckIns (as string) and qrCodePromo (as string)
     */
    public void sendToFirebase() {
        // Add the new user document to Firestore
        if (signUps==null){
            signUps=0;
        }
        if (checkIns==null){
            checkIns=0;
        }
        Map<String, Object> docData = new HashMap<>();
        docData.put("id", id);
        docData.put("name", name);
        docData.put("location", location);
        docData.put("startDate", startDate);
        docData.put("endDate",endDate);
        docData.put("startTime", startTime);
        docData.put("endTime", endTime);
        docData.put("organizerId",organizerId);
        docData.put("maxSignUps", maxSignUps);
        docData.put("signUps", signUps);
        docData.put("checkIns", checkIns);
        docData.put("qrCode",qrCode);
        docData.put("posterData",poster.getPhotoDataFromBitmap());
        docData.put("posterIsDefault", posterIsDefault);
        docData.put("addtionalInfo",additionalInfo);
        docData.put("posterData",poster.getPhotoDataFromBitmap());

        eventsRef.document(id).set(docData)
                .addOnSuccessListener(aVoid -> {
                    // Document successfully added
                    Log.d("debug", "User added successfully to Firestore");
                })
                .addOnFailureListener(e -> {
                    // Handle the failure
                    Log.e("debug", "Error adding user to Firestore", e);
                });
    }

    /**
     * Deletes an event from the Events collection in firestore, as well as its Attendance
     * sub-collection.
     */
    public void removeFromFirestore() {
        CollectionReference attendanceRef = MainActivity.db.collection("Events/"+ id +"/Attendance");
        CollectionReference alertsRef = MainActivity.db.collection("Events/"+id+"/alerts");
        //remove event doc
        eventsRef.document(id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Firestore", "Document successfully removed!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Firestore", e.getMessage());
                    }
                });
        //remove attendance sub-collection
        attendanceRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot attendeeDoc : queryDocumentSnapshots) {
                            attendeeDoc.getReference().delete();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Firestore", e.getMessage());
                    }
                });
        //remove alerts sub-collection
        alertsRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot alertDoc : queryDocumentSnapshots) {
                            alertDoc.getReference().delete();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Firestore", e.getMessage());
                    }
                });

    }

    /**
     * Returns a list of Attendee objects attending the event. This is the default "signup" list
     * Updates the user's checked in status if they check in or not.
     * @return list of User objects
     */
    public ArrayList<Attendee> getAttendanceList() {
        ArrayList<Attendee> attendees = new ArrayList<>();
        CollectionReference attendanceRef = MainActivity.db.collection("Events/"+ id +"/Attendance");
        attendanceRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot attendeeDoc : queryDocumentSnapshots) {
                            DocumentSnapshot userDoc = usersRef.document(attendeeDoc.getId()).get().getResult();
                            Attendee attendee = userDoc.toObject(Attendee.class);
                            attendee.setProfilePhotoFromData(attendee.getProfilePhotoData());
                            attendees.add(attendee);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Firestore", e.getMessage());
                    }
                });
        return attendees;
    }

    public Boolean getPosterIsDefault() {
        return posterIsDefault;
    }

    public void setPosterIsDefault(Boolean posterIsDefault) {
        this.posterIsDefault = posterIsDefault;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }
}

