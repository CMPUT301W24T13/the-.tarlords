package com.example.the_tarlords.data.users;

import android.widget.ImageView;

import androidx.fragment.app.FragmentActivity;

import com.example.the_tarlords.data.Alert.Alert;
import com.example.the_tarlords.data.QR.QRCode;
import com.example.the_tarlords.data.app.App;
import com.example.the_tarlords.data.attendance.Attendance;
import com.example.the_tarlords.data.event.Event;
import com.example.the_tarlords.data.event.EventPoster;
import com.example.the_tarlords.data.map.Map;

import java.io.IOException;
import java.util.ArrayList;

//Notes for myself(Rimsha): Finish the methods once others are done their parts
// ask lucy if she is putting event details page and event poster together or not. If they are separate, then create one more method.
/* ask iz if she is working on attendeeSignUpList(). And for the setLimitOnSignUps() thing, can use the reachedMaxCap() method from event
   in SignUp() method from Attendance to check if we have reached max, there is also maxLimit org can set */

/**
 * This is the Organizer Class, which is subclass of Attendee but with more permissions
 */
public class Organizer extends Attendee {


}