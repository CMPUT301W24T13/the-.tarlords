package com.example.the_tarlords.data.event;

import static androidx.fragment.app.FragmentManager.TAG;

import static com.example.the_tarlords.MainActivity.db;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.the_tarlords.MainActivity;
import com.example.the_tarlords.data.QR.QRCode;
import com.example.the_tarlords.data.attendance.Attendance;
import com.example.the_tarlords.data.users.Attendee;
import com.example.the_tarlords.data.users.Organizer;
import com.example.the_tarlords.data.users.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.common.returnsreceiver.qual.This;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * This class defines an event
 * UUID type for event attribute makes sure that everytime an event object is created it has a unique id
 * Not sure how QRcode will work , is it initialized when the event is created, or can it be set after being created
 */


/* NOTE FOR KHUSHI AND GRACE:

    2. Need to connect event location to the Map Class.
*/
public class Event implements Attendance, Parcelable {
    String name;
    String location;
    String startTime;
    String endTime;
    String startDate;
    String id;
    String organizerId;
    private QRCode qrCodeCheckIns;
    private QRCode qrCodePromo;
    private String checkInQR;
    private String eventInfoQR;

    private EventPoster poster;

    private Integer maxNumOfSignUps;

    //this is accessing the same FirebaseFirestore from mainactivity's static db
    private FirebaseFirestore db = MainActivity.db;
    private CollectionReference attendanceRef = MainActivity.db.collection("Events/"+ id +"/Attendees");
    private CollectionReference usersRef = MainActivity.db.collection("Users");

    //this is static so that all instances of the Event class have the same eventsRef. So Im not including this ref in the constructors
    private static CollectionReference eventsRef = MainActivity.db.collection("Events");

    public Event(String name, String location, String id, String startTime, String endTime, String startDate) {
        this.name = name;
        this.location = location;
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startDate = startDate;
        this.attendanceRef = db.collection("Events").document(id).collection("Attendees");
        this.usersRef = db.collection("Users");
    }

    public Event (Parcel in) {
        name = in.readString();
        location = in.readString();
        id = in.readString();
        startTime = in.readString();
        endTime = in.readString();
        startDate = in.readString();
        this.attendanceRef = db.collection("Events").document(id).collection("Attendees");
        this.usersRef = db.collection("Users");
    }
    public Event (){
        this.attendanceRef = db.collection("Events").document(id).collection("Attendees");
        this.usersRef = db.collection("Users");
    };


    public void setOrganizerId(String organizerId) {
        this.organizerId = organizerId;
    }
    public String getOrganizerId() {
        return organizerId;
    }
    public String getId() {
        eventsRef
                .document(id).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        id = documentSnapshot.getString("id");
                        Log.d("Event", id);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Event", e.getMessage());
                    }
                });

        return id;
    }

    public void setId(String id) {
        eventsRef
                .document(id).update("id", id)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Event.this.id = id;
                        Log.d("Event", "Event id successfully set");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Event", e.getMessage());
                    }
                });

    }

    public String getLocation() {
        eventsRef
                .document(id).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        location = documentSnapshot.getString("location");
                        Log.d("Event", location);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Event", e.getMessage());
                    }
                });

        return location;
    }


    public void setLocation(String location) {
        eventsRef
                .document(id).update("location", location)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Event.this.location = location;
                        Log.d("Event", "Event location successfully set");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Event", e.getMessage());
                    }
                });

    }

    public String getName() {
        eventsRef
                .document(id).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        name = documentSnapshot.getString("name");
                        Log.d("Event", name);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Event", e.getMessage());
                    }
                });

        return name;
    }

    public void setName(String name) {
        eventsRef
                .document(id).update("name", name)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Event.this.name = name;
                        Log.d("Event", "Event name successfully set");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Event", e.getMessage());
                    }
                });
    }

    public String getStartTime() {
        eventsRef
                .document(id).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        startTime = documentSnapshot.getString("startTime");
                        Log.d("Event", startTime);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Event", e.getMessage());
                    }
                });

        return startTime;
    }

    public void setStartTime(String startTime) {
        eventsRef
                .document(id).update("startTime", startTime)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Event.this.startTime = startTime;
                        Log.d("Event", "Event startTime successfully set");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Event", e.getMessage());
                    }
                });
    }

    public String getEndTime() {
        eventsRef
                .document(id).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        endTime = documentSnapshot.getString("endTime");
                        Log.d("Event", endTime);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Event", e.getMessage());
                    }
                });

        return endTime;
    }

    public void setEndTime(String endTime) {
        eventsRef
                .document(id).update("endTime", endTime)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Event.this.endTime = endTime;
                        Log.d("Event", "Event endTime successfully set");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Event", e.getMessage());
                    }
                });
    }

    public String getStartDate() {
        eventsRef
                .document(id).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        startDate = documentSnapshot.getString("startDate");
                        Log.d("Event", startDate);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Event", e.getMessage());
                    }
                });

        return startDate;
    }

    public void setStartDate(String startDate) {
        eventsRef
                .document(id).update("startDate", startDate)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Event.this.startDate = startDate;
                        Log.d("Event", "Event startDate successfully set");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Event", e.getMessage());
                    }
                });
    }


    public String getCheckInQR() {
        eventsRef
                .document(id).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        checkInQR = documentSnapshot.getString("checkInQR");
                        Log.d("Event", checkInQR);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Event", e.getMessage());
                    }
                });

        return checkInQR;
    }

    public void setCheckInQR(String checkInQR) {
        eventsRef
                .document(id).update("checkInQR", checkInQR)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Event.this.checkInQR = checkInQR;
                        Log.d("Event", "Event checkInQR successfully set");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Event", e.getMessage());
                    }
                });
    }

    public String getEventInfoQR() {
        eventsRef
                .document(id).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        eventInfoQR = documentSnapshot.getString("eventInfoQR");
                        Log.d("Event", eventInfoQR);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Event", e.getMessage());
                    }
                });

        return eventInfoQR;
    }

    public void setEventInfoQR(String eventInfoQR) {
        eventsRef
                .document(id).update("eventInfoQR", eventInfoQR)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Event.this.eventInfoQR = eventInfoQR;
                        Log.d("Event", "Event eventInfoQR successfully set");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Event", e.getMessage());
                    }
                });
    }

    //DO firebase for these after poster is done
    public EventPoster getPoster() {
        return poster;
    }

    public void setPoster(EventPoster poster) {
        this.poster = poster;
    }


    public int getMaxNumOfSignUps() {
        eventsRef
                .document(id).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        maxNumOfSignUps = Integer.valueOf(documentSnapshot.getString("maxNumOfSignUps"));
                        Log.d("Event", String.valueOf(maxNumOfSignUps));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Event", e.getMessage());
                    }
                });

        return maxNumOfSignUps;
    }

    public void setMaxNumOfSignUps(int maxNumOfSignUps) {
        eventsRef
                .document(id).update("maxNumOfSignUps", maxNumOfSignUps)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Event.this.maxNumOfSignUps = maxNumOfSignUps;
                        Log.d("Event", "Event maxNumOfSignUps successfully set");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Event", e.getMessage());
                    }
                });
    }


    /**
     * Returns a list of Attendee objects attending the event. This is the default "signup" list
     * Updates the user's checked in status if they check in or not.
     * @return list of User objects
     */
    public ArrayList<Attendee> getAttendanceList() {
        ArrayList<Attendee> attendees = new ArrayList<Attendee>();
        attendanceRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot attendeeDoc : task.getResult()) {
                        DocumentSnapshot userDoc = usersRef.document(attendeeDoc.getId()).get().getResult();
                        String id = userDoc.getId();
                        String firstName = userDoc.get("firstName").toString();
                        String lastName = userDoc.get("lastName").toString();
                        String email = userDoc.get("email").toString();
                        String phoneNum = userDoc.get("phoneNum").toString();

                        //User user = userDoc.toObject(User.class);
                        Attendee attendee = new Attendee(id, firstName,lastName,phoneNum,email,Event.this);
                        attendee.setCheckInStatus(attendeeDoc.getBoolean("checkedInStatus"));
                        attendees.add(attendee);
                    }
                    Log.d("firestore", attendees.toString());
                } else {
                    Log.d("firestore", "Error getting documents: ", task.getException());
                }
            }
        });
        return attendees;
    }
    /**
     * Signs up a user to attend an event by adding their name to the attendance list.
     *
     * @param user to add
     */
    public void signUp(User user) {
        attendanceRef
                .document(user.getId().toString())
                .set(false)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Firestore", "DocumentSnapshot successfully written!");
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
     * Removes a user from the attendance list of an event.
     *
     * @param user to remove
     */
    public void removeSignUp(User user) {
        attendanceRef
                .document(user.getId().toString())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Firestore", "DocumentSnapshot successfully written!");
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
     * Sets check in status of a user for an event.
     * @param user to check in, status to set (boolean)
     */
    public void setCheckIn(User user, Boolean status) {
        attendanceRef
                .document(user.getId().toString())
                .set(status)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Firestore", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Firestore", e.getMessage());
                    }
                });
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(location);
        dest.writeString(id);
        dest.writeString(startTime);
        dest.writeString(endTime);
        dest.writeString(startDate);
    }
    //NEED TO JAVADOC
    //Generates a new doc id for the new event, IMPORTANT FOR THE QRCode stuff

    private String newDocID;
    public String makeNewDocID() {
        db = FirebaseFirestore.getInstance();
        eventsRef = db.collection("Events");

        eventsRef.addSnapshotListener((querySnapshots, error) -> {
            if (error != null) {
                Log.e("Firestore", error.toString());
                return;
            }
            if (querySnapshots != null) {
                for (QueryDocumentSnapshot doc: querySnapshots) {
                    AggregateQuery countQuery = eventsRef.count();
                    countQuery.get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                // Count fetched successfully
                                AggregateQuerySnapshot snapshot = task.getResult();
                                newDocID = String.valueOf((int)snapshot.getCount() + 1);
                            } else {
                                throw new RuntimeException("Could not find number of documents in FireBase");
                            }
                        }
                    });
                }
            }
        });
        return newDocID;
    }




}
