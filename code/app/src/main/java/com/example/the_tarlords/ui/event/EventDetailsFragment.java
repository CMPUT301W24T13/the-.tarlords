package com.example.the_tarlords.ui.event;

import static com.example.the_tarlords.MainActivity.context;
import static com.example.the_tarlords.MainActivity.toolbar;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.navigation.fragment.NavHostFragment;

import com.example.the_tarlords.MainActivity;
import com.example.the_tarlords.R;
import com.example.the_tarlords.data.QR.QRCode;
import com.example.the_tarlords.data.attendance.AttendanceDBHelper;
import com.example.the_tarlords.data.attendance.AttendanceQueryCallback;
import com.example.the_tarlords.data.event.Event;
import com.example.the_tarlords.data.photo.EventPoster;
import com.example.the_tarlords.databinding.FragmentEventDetailsBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 * This fragment inflates the event details when an event is clicked on
 * from the attendee/homepage list view.
 * The nav bar should handle going back to the listview????
 */
public class EventDetailsFragment extends Fragment implements MenuProvider {
    private Button shareQrCodeCI;
    private Button shareQrCodeEI;
    private static Event event;
    private boolean isOrganizer;

    private boolean browse;

    private boolean isAdmin;

    private FragmentEventDetailsBinding binding;
    private static final int REQUEST_NOTIFICATION_PERMISSION = 101;


    /**
     * Required empty public constructor.
     */
    public EventDetailsFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param event event to be displayed.
     * @param isOrganizer whether or not the user has organizer permissions for this event.
     * @return A new instance of fragment EventDetailsFragment.
     */
    public static EventDetailsFragment newInstance(Event event, boolean isOrganizer) {
        EventDetailsFragment fragment = new EventDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable("event", event); //When getting the event cast to Event again
        args.putBoolean("isOrganizer", isOrganizer);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            event = getArguments().getParcelable("event");
            isOrganizer = getArguments().getBoolean("isOrganizer");
            browse = getArguments().getBoolean("browse");
        }
        requestNotificationPermissions();
    }

    /**
     * This is connected to fragment_event_details , need to add more details to the fragment layout
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEventDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        //intialize isAdmin, this part is working
        isAdmin = MainActivity.isAdmin;
        Log.d("admin", String.valueOf(isAdmin));
        //MANDATORY: required for MenuProvider options menu
        requireActivity().addMenuProvider(this);

        //set fragment views
        TextView eventNameTextView = view.findViewById(R.id.tv_event_name);
        TextView eventLocationTextView = view.findViewById(R.id.tv_event_location);
        TextView eventStartDateTextView = view.findViewById(R.id.tv_event_date_time);
        TextView eventMaxAttendees = view.findViewById(R.id.tv_max_attendees);
        TextView signUps = view.findViewById(R.id.countOfSignUps);
        TextView checkIns = view.findViewById(R.id.countOfCheckIns);
        ImageView eventPosterImageView = view.findViewById(R.id.iv_poster);
        //add additional views here as desired

        // Check if event is not null before accessing its attributes
        if (event != null) {
            eventNameTextView.setText(event.getName());
            eventLocationTextView.setText("  "+event.getLocation());
            String date;
            if (event.getEndDate() == event.getStartDate()&&event.getEndDate()!=null) {
                date = String.format("  %s\n %s - %s", event.getStartDate(), event.getStartTime(), event.getEndTime());
            } else {
                date = String.format("  %s %s -\n  %s %s", event.getStartDate(), event.getStartTime(), event.getEndDate(), event.getEndTime());
            }
            eventStartDateTextView.setText(date);
            if (event.getMaxSignUps()==(Integer) (-1)){
                eventMaxAttendees.setText("Max Capacity: Unlimited");
            } else {
                eventMaxAttendees.setText("Max Capacity: "+event.getMaxSignUps());
            }

            signUps.setText("  "+event.getSignUps()+" Sign Ups");
            checkIns.setText("  "+event.getCheckIns()+" Check Ins");

            // set additional fields here as desired

            if (event.getPoster()!=null){ //if poster data exists, display uploaded poster
                eventPosterImageView.setImageBitmap(event.getPoster().getBitmap());
            }
            else { //otherwise generate poster
                EventPoster eventPoster = new EventPoster(event.getId(),null, event);
                eventPoster.autoGenerate();
                event.setPoster(eventPoster);
                eventPosterImageView.setImageBitmap(eventPoster.getBitmap());
            }

        }

        //display event QR codes if user has organizer perms
        if (isOrganizer == true) {
            FragmentContainerView miniMap = view.findViewById(R.id.event_details_mini_map);
            miniMap.setVisibility(view.VISIBLE);
            CardView map = view.findViewById(R.id.miniMapCardView);
            map.setVisibility(view.VISIBLE);
            View mapClick = view.findViewById(R.id.miniMapClickView);
            mapClick.setClickable(true);
            mapClick.bringToFront();
            mapClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle args = new Bundle();
                    args.putParcelable("event",event);
                    try {
                        NavHostFragment.findNavController(EventDetailsFragment.this)
                                .navigate(R.id.action_eventDetailsFragment_to_MapsFragment, args);
                    }catch(Exception e)
                    {
                        Log.e("maps", Log.getStackTraceString(e));
                    }
                }
            });
            View attendance = view.findViewById(R.id.attendanceConstraint);
            attendance.setClickable(true);
            view.findViewById(R.id.view_attendance_details).setVisibility(view.VISIBLE);
            attendance.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //navigate to attendance fragment
                    Bundle args = new Bundle();
                    args.putParcelable("event",event);
                    try {
                        NavHostFragment.findNavController(EventDetailsFragment.this)
                                .navigate(R.id.action_eventDetailsFragment_to_attendanceFragment, args);
                    } catch (Exception ignored) {}
                }
            });

            if (event.getQrCode()!=null){
                view.findViewById(R.id.tv_event_detail_QR_label).setVisibility(view.VISIBLE);
                view.findViewById(R.id.QRconstraintLayout).setVisibility(view.VISIBLE);
                ImageView checkInQr = view.findViewById(R.id.iv_checkin_details);
                ImageView eventInfoQr = view.findViewById(R.id.iv_info_details);
                QRCode.generateQR("CI"+event.getQrCode(),checkInQr);
                QRCode.generateQR("EI"+event.getQrCode(),eventInfoQr);
            }
        }

        ImageView imageViewCI = view.findViewById(R.id.iv_checkin_details);
        shareQrCodeCI = view.findViewById(R.id.shareQrCodeCI);
        shareQrCodeCI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QRCode qrcode = new QRCode();
                qrcode.shareQR(imageViewCI, getActivity());
            }
        });

        ImageView imageViewEI = view.findViewById(R.id.iv_info_details);
        shareQrCodeEI = view.findViewById(R.id.shareQrCodeEI);
        shareQrCodeEI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QRCode qrcode = new QRCode();
                qrcode.shareQR(imageViewEI, getActivity());
            }
        });
    }

    @Override
    public void onDestroyView(){
        onCreateMenu(toolbar.getMenu(), new MenuInflater(getContext()));
        super.onDestroyView();
    }

    /**
     * Mandatory MenuProvider interface method.
     * Displays options menu for details fragment dependant on user status (organizer or attendee)
     * @param menu         the menu to inflate the new menu items into
     * @param menuInflater the inflater to be used to inflate the updated menu
     */
    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menu.clear();

        //link options menu xml
        menuInflater.inflate(R.menu.options_menu, menu);

        //if user is the organizer, display edit and attendance icons
        if (isOrganizer) {
            menu.findItem(R.id.editOptionsMenu).setVisible(true);
            menu.findItem(R.id.deleteOptionsMenu).setVisible(true);
        }
        //if user came from browse fragment display sign up button
        if (browse) {
            menu.findItem(R.id.signUpOptionsMenu).setVisible(true);
        }
        //display announcement icon for all users
        menu.findItem(R.id.anouncementsOptionsMenu).setVisible(true);
        //if user is also an admin, display delete options icon

        if (isAdmin) {
            menu.findItem(R.id.deleteOptionsMenu).setVisible(true);
        }

    }

    /**
     * Mandatory MenuProvider interface method.
     * Handles fragment navigation depending on which menu item was pressed.
     * @param menuItem the menu item that was selected
     * @return
     */
    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        //try/catch around all navigation actions in order to prevent crashes

        //navigate to edit fragment
        if (menuItem.getItemId() == R.id.editOptionsMenu) {
            Bundle args = new Bundle();
            args.putParcelable("event",event);
            try {
                NavHostFragment.findNavController(EventDetailsFragment.this)
                        .navigate(R.id.action_eventDetailsFragment_to_eventEditFragment, args);
            } catch (Exception ignored) {}
        }
        //navigate to announcements fragment
        else if (menuItem.getItemId()==R.id.anouncementsOptionsMenu) {
            Bundle args = new Bundle();
            args.putParcelable("event",event);
            args.putBoolean("isOrganizer",isOrganizer);
            try {
                NavHostFragment.findNavController(EventDetailsFragment.this)
                        .navigate(R.id.action_eventDetailsFragment_to_alertFragment, args);
            } catch (Exception ignored) {}

        }
        else if (menuItem.getItemId()==R.id.deleteOptionsMenu) {
            if (isAdded()) { // Check if the fragment is attached to an activity
                AlertDialog dialog = new AlertDialog.Builder(requireContext())
                        .setMessage("Are you sure you would like to delete the event " + event.getName() + "?")
                        .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                event.removeFromFirestore();
                                try {
                                    // Return to event organizer list fragment
                                    NavHostFragment.findNavController(EventDetailsFragment.this)
                                            .navigate(R.id.action_eventDetailsFragment_pop);
                                } catch (Exception ignored) {
                                }
                            }
                        })
                        .setCancelable(true)
                        .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Handle cancel action if needed
                            }
                        }).show();
            } else {
                // Fragment is not attached to an activity, handle the situation accordingly
                Log.d("admin", "fragment not attached to activity");
            }
        } else if (menuItem.getItemId()==R.id.signUpOptionsMenu) {

            AttendanceDBHelper.signUp(event, MainActivity.user, new AttendanceQueryCallback() {
                @Override
                public void onQueryComplete(int result) {
                    if (result== AttendanceDBHelper.SUCCESSFUL){
                        Toast.makeText(context, "Sign up successful!", Toast.LENGTH_SHORT).show();
                    } else if (result == AttendanceDBHelper.ALREADY_SIGNED_UP) {
                        Toast.makeText(context, "Already signed up!", Toast.LENGTH_SHORT).show();
                    } else if (result== AttendanceDBHelper.EVENT_FULL) {
                        Toast.makeText(context, "Unable to sign up. Max capacity reached.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Error. Sign up failed.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            return false;

        }
        //should return false to prevent crashing
        return false;

    }

    /**
     * Requests notification permissions
     */
    private void requestNotificationPermissions(){

        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.POST_NOTIFICATIONS) != PermissionChecker.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.POST_NOTIFICATIONS},REQUEST_NOTIFICATION_PERMISSION);
        }

    }

}