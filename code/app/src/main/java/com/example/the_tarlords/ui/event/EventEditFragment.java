package com.example.the_tarlords.ui.event;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.the_tarlords.MainActivity;
import com.example.the_tarlords.R;
import com.example.the_tarlords.data.DateHelper;
import com.example.the_tarlords.data.QR.QRCode;
import com.example.the_tarlords.data.event.Event;
import com.example.the_tarlords.data.photo.EventPoster;
import com.example.the_tarlords.databinding.FragmentEventEditBinding;
import com.example.the_tarlords.ui.profile.TakePhotoActivity;
import com.example.the_tarlords.ui.profile.UploadPhotoActivity;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.time.MonthDay;
import java.time.Year;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventEditFragment#newInstance} factory method to
 * create an instance of this fragment.
 * This inflates the event details and allows for user to edit details
 * linked to fragment_event_edit.xml
 * Will also take in an event as a parameter
 */
public class EventEditFragment extends Fragment implements MenuProvider {

    // the fragment initialization parameters
    private static Event event;
    // The views that the fragment will inflate
    private ImageView eventPosterImageView;
    private EditText eventNameEditText;
    private EditText eventAdditionalInfo;
    private TextView eventStartDateTextView;
    private TextView eventEndDateTextView;
    private TextView eventStartTimeTextView;
    private TextView eventEndTimeTextView;
    private EditText eventLocationEditText;
    private EditText maxAttendees;
    private CheckBox cbMaxAttendees;
    private ImageView checkInQR;
    private ImageView eventInfoQR;

    private  Menu menu;

    private EditText additionalInfoEditText;

    private FragmentEventEditBinding binding;
    private Spinner spinner;
    private ArrayList<String> eventsForReuse;
    private static CollectionReference QRRef = MainActivity.db.collection("QRCodes");
    private static CollectionReference EventsRef = MainActivity.db.collection("Events");


    public EventEditFragment() {
        // Required empty public constructor
    }

    /**
     * This is used to create a new instance of EditEventFragment
     *
     * @param event , an object of event class
     * @return the fragment to parent
     */
    public static EventEditFragment newInstance(Event event) {
        EventEditFragment fragment = new EventEditFragment();
        Bundle args = new Bundle();
        args.putParcelable("event", (Parcelable) event); //When getting the event cast to Event again
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            event = getArguments().getParcelable("event");
        }
    }

    /**
     * Method to set all text and edit views to "Non editable" or "editable"
     * TODO are the QR codes ever editable?
     */
    private void setTextViewsClickablity(Boolean isEditable) {
        eventPosterImageView.setClickable(isEditable);
        eventNameEditText.setEnabled(isEditable);
        eventStartDateTextView.setClickable(isEditable);
        eventEndDateTextView.setClickable(isEditable);
        eventStartTimeTextView.setClickable(isEditable);
        eventEndTimeTextView.setClickable(isEditable);
        eventLocationEditText.setEnabled(isEditable);
        maxAttendees.setEnabled(isEditable);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEventEditBinding.inflate(inflater, container, false);

        return binding.getRoot();

    }

    /**
     * Method to check if a string represents a valid integer
     * @param str
     * @return Boolean
     */
    private boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Helps user pick a valid start or end date for their event
     * @param s, string repsentation of date stored
     */
    private void showDatePickerDialog(String s) {
        // logic for showing a date picker dialog
        int year = Year.now().getValue();
        int month = YearMonth.now().getMonthValue()-1;
        int dayOfMonth = MonthDay.now().getDayOfMonth();
        if (s == "start"){
            if (event.getStartDate()!=null){
                year = DateHelper.getYear(event.getStartDate());
                month = DateHelper.getMonth(event.getStartDate());
                dayOfMonth = DateHelper.getDayOfMonth(event.getStartDate());
            }
        } else {
            if (event.getEndDate()!=null){
                if (event.getStartDate()!=null&&DateHelper.getTimestamp(event.getEndDate()) >= DateHelper.getTimestamp(event.getStartDate())){
                    year = DateHelper.getYear(event.getEndDate());
                    month = DateHelper.getMonth(event.getEndDate());
                    dayOfMonth = DateHelper.getDayOfMonth(event.getEndDate());
                }
            }

        }
        DatePickerDialog dialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
            /**
             * Sets the startDate attribute using a built-in date picker
             * @param view the picker associated with the dialog
             * @param year the selected year
             * @param month the selected month (0-11 for compatibility with
             *              {@link Calendar#MONTH})
             * @param dayOfMonth the selected day of the month (1-31, depending on
             *                   month)
             */
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // Array of month names
                String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

                // Format the date components into a string "YYYY.MonthName.DD"
                @SuppressLint("DefaultLocale")

                String formattedDate = String.format("%.3s %02d, %04d", monthNames[month], dayOfMonth,year);

                // Update the text view
                if (s == "start") {
                    eventStartDateTextView.setText(formattedDate);
                }else{
                    eventEndDateTextView.setText(formattedDate);
                }
            }
        }, year, month, dayOfMonth);
        //show the dialog
        dialog.show();
    }

    /**
     * Dialog that helps user pick a start ond endtime for thier event
     * @param s, string representation of time stored
     */

    private void showTimePickerDialog(String s) {
        // logic for showing a time picker dialog
        TimePickerDialog dialog = new TimePickerDialog(requireContext(), new TimePickerDialog.OnTimeSetListener() {
            /**
             * Sets the time attribute using a built-in TimePicker Dialog
             * @param view the view associated with this listener
             * @param hourOfDay the hour that was set
             * @param minute the minute that was set
             */
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String amPm;
                // Check if the selected hour is in the AM or PM period
                if (hourOfDay < 12) {
                    amPm = "AM";
                } else {
                    amPm = "PM";
                    // Adjust the hour for PM
                    hourOfDay -= 12;
                }
                // Use the amPm and adjusted hour to display or process the time
                String formattedTime = String.format("%02d:%02d %s", hourOfDay, minute, amPm);
                if (s == "start") {
                    //update text view
                    eventStartTimeTextView.setText(formattedTime);
                } else {
                    //update text view
                    eventEndTimeTextView.setText(formattedTime);
                }

            }
        }, 7, 30, false);
        //show the dialog
        dialog.show();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        //MANDATORY for MenuProvider options menu
        requireActivity().addMenuProvider(this);

        //Event id is a textview because user should not be able to edit it, assigned when event object created
        eventPosterImageView = view.findViewById(R.id.edit_iv_poster);
        eventNameEditText = view.findViewById(R.id.et_event_name);
        eventStartDateTextView = view.findViewById(R.id.tv_edit_event_startDate);
        eventEndDateTextView = view.findViewById(R.id.tv_edit_event_endDate);
        eventStartTimeTextView = view.findViewById(R.id.tv_edit_event_startTime);
        eventEndTimeTextView = view.findViewById(R.id.tv_edit_event_endTime);
        eventLocationEditText = view.findViewById(R.id.et_event_location);
        eventAdditionalInfo = view.findViewById(R.id.et_additional_info);
        maxAttendees = view.findViewById(R.id.et_max_attendees);
        cbMaxAttendees = view.findViewById(R.id.cb_max_attendees);
        additionalInfoEditText = view.findViewById(R.id.et_additional_info);
        additionalInfoEditText.setText(event.getAdditionalInfo());

        //add more attributes as desired

        //check event is not null
        if (event != null) {
            // Populate UI elements with event details
            eventNameEditText.setText(event.getName());
            eventLocationEditText.setText(event.getLocation());
            eventStartTimeTextView.setText(event.getStartTime());
            eventStartDateTextView.setText(event.getStartDate());
            eventEndDateTextView.setText(event.getEndDate());
            eventEndTimeTextView.setText(event.getEndTime());
            eventAdditionalInfo.setText(event.getAdditionalInfo());
            if (event.getPoster()!=null){
                eventPosterImageView.setImageBitmap(event.getPoster().getBitmap());
            }
            if (event.getMaxSignUps()!=null&&event.getMaxSignUps()!=-1) {
                cbMaxAttendees.setChecked(true);
                maxAttendees.setEnabled(true);
                maxAttendees.setText(event.getMaxSignUps().toString());
            } else {
                cbMaxAttendees.setChecked(false);
                event.setMaxSignUps(-1);
                maxAttendees.setText("-1");
                maxAttendees.setVisibility(view.GONE);
            }
            cbMaxAttendees.setOnCheckedChangeListener((buttonView, isChecked) -> {
                maxAttendees.setEnabled(isChecked);
                if (isChecked) {
                    maxAttendees.setText("");
                    maxAttendees.setFocusableInTouchMode(true);
                    maxAttendees.setFocusable(true);
                    maxAttendees.setClickable(true);
                    maxAttendees.setVisibility(view.VISIBLE);
                    if (event.getMaxSignUps()!=null) {
                        maxAttendees.setText(event.getMaxSignUps().toString());
                    }
                } else {
                    maxAttendees.setText("-1"); // Clear the text when the checkbox is unchecked
                    maxAttendees.setVisibility(view.GONE);
                }
            });
            if (event.getId()!=null) {
                view.findViewById(R.id.edit_tv_event_detail_QR_label).setVisibility(view.GONE);
                view.findViewById(R.id.editQRconstraintLayout).setVisibility(view.GONE);
            }
            // Populate more attributes as desired
        }

        // Set an OnClickListener for the eventPosterImageView
        eventPosterImageView.setOnClickListener(v -> {
            PopupMenu addPhotoOptions = new PopupMenu(this.getContext(), v);
            addPhotoOptions.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.camera_open) {
                        //take photo
                        Intent eventPosterCapture = new Intent(getActivity(), TakePhotoActivity.class);
                        eventPosterCapture.putExtra("event", event);
                        startActivityForResult(eventPosterCapture,1000);
                        return true;
                    } else if (item.getItemId() == R.id.gallery_open) {
                        //upload photo
                        Intent eventPosterUpload = new Intent(getActivity(), UploadPhotoActivity.class);
                        eventPosterUpload.putExtra("event", event);
                        startActivityForResult(eventPosterUpload,1001);
                        return true;
                    } else if (item.getItemId() == R.id.remove_current_photo) {
                        //remove current photo:
                        event.getPoster().autoGenerate();
                        event.setPosterIsDefault(true);
                        return true;
                    } else {
                        return false;
                    }
                }
            });
            addPhotoOptions.getMenuInflater().inflate(R.menu.photo_add_menu, addPhotoOptions.getMenu());
            addPhotoOptions.show();
        });


        // Set an OnClickListener for the eventStartDateTextView
        eventStartDateTextView.setOnClickListener(v -> showDatePickerDialog("start"));
        // Set an OnClickListener for the eventStartDateTextView
        eventEndDateTextView.setOnClickListener(v -> showDatePickerDialog("end"));
        // Set an OnClickListener for the eventStartTimeTextView
        eventStartTimeTextView.setOnClickListener(v -> showTimePickerDialog("start"));
        // Set an OnClickListener for the eventEndDateTextView
        eventEndTimeTextView.setOnClickListener(v -> showTimePickerDialog("end"));

        // this is for reuse QR code dropdown/spinner
        spinner = view.findViewById(R.id.reuseQrCode);
        QRCode qrCode = new QRCode();
        qrCode.findPastEvents(MainActivity.user.getUserId(), new QRCode.EventsCallback() {
            @Override
            public void onEventsLoaded(ArrayList<String> events) {
                try {
                    Log.e("Old Events", String.valueOf(events));   //list is loaded
                    events.add(0, "Optional Reuse QRCode");
                    eventsForReuse = events;

                    ArrayAdapter<String> adapter2 = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, events);
                    spinner.setAdapter(adapter2);

                    Drawable spinnerDrawable = spinner.getBackground().getConstantState().newDrawable();
                    spinnerDrawable.setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
                    spinner.setBackground(spinnerDrawable);
                } catch(Exception e) { }
            }
        });
    }


    /**
     * Mandatory MenuProvider interface method.
     * Displays eventEditFragment options menu.
     * @param menu         the menu to inflate the new menu items into
     * @param menuInflater the inflater to be used to inflate the updated menu
     */
    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        this.menu = menu; //store the menu
        menu.clear();
        menuInflater.inflate(R.menu.options_menu, menu);
        //set visibility of menu options
        menu.findItem(R.id.saveOptionsMenu).setVisible(true);
        menu.findItem(R.id.cancelOptionsMenu).setVisible(true);

        //set clickability of views and edit texts
        setTextViewsClickablity(true);
    }

    /**
     * Mandatory MenuProvider interface method.
     * This shouldn't save changes if cancelOptions menu is selected
     * @param menuItem the menu item that was selected
     * @return boolean
     */
    // TODO: when an event is saved duplicate events show up?
    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.saveOptionsMenu || menuItem.getItemId() == R.id.cancelOptionsMenu) {

            //set clickability of views and edit texts
            setTextViewsClickablity(false);
            if(validateInput()){
            //save changes to event details
                if (menuItem.getItemId() == R.id.saveOptionsMenu) {

                    // Update the event details
                    event.setName(eventNameEditText.getText().toString());
                    event.setStartDate(eventStartDateTextView.getText().toString());
                    event.setEndDate(eventEndDateTextView.getText().toString());
                    event.setStartTime(eventStartTimeTextView.getText().toString());
                    event.setEndTime(eventEndTimeTextView.getText().toString());
                    event.setLocation(eventLocationEditText.getText().toString());
                    event.setOrganizerId(MainActivity.user.getUserId());
                    event.setAdditionalInfo(eventAdditionalInfo.getText().toString());
                    event.setMaxSignUps(Integer.valueOf(maxAttendees.getText().toString()));
                    if (event.getPoster() == null) {
                        EventPoster poster = new EventPoster(event.getId(), null, event);
                        poster.autoGenerate();
                        event.setPoster(poster);
                    }

                    boolean reuse = false;
                    String eventID;

                    int position = spinner.getSelectedItemPosition();
                    String eventToReuse = eventsForReuse.get(position);

                    if (Objects.equals(eventToReuse, "Optional Reuse QRCode") && event.getId() == null) {
                        //Normal event creation
                        Log.e("SPINNERN", "NORMAL");
                        event.makeNewDocID(); //generate new event id
                        QRCode qr = new QRCode();
                        qr.makeQR(event.getId());
                        event.setQrCode(qr.getQrID()); //generate check in QR
                    } else if (event.getId() == null) {
                        //Reuse QR code event creation
                        Log.e("SPINNERR", "REUSE");
                        event.makeNewDocID(); //generate new event id
                        QRCode qr = new QRCode();
                        qr.reuseQR(MainActivity.user.getUserId(), eventToReuse, event.getId());
                        event.setQrCode(qr.getQrID()); //generate check in QR
                        reuse = true;
                    }

                    //upload event in firebase
                    event.sendToFirebase();

                        if (reuse) {
                            //find event id in qr collection, get qrid, set in original event
                            QRRef.addSnapshotListener((querySnapshots, error) -> {
                                if (error != null) {
                                    Log.e("Firestore", error.toString());
                                    return;
                                }
                                if (querySnapshots != null) {
                                    for (QueryDocumentSnapshot doc : querySnapshots) {
                                        String DocCodeID = doc.getId();
                                        try {
                                            if (Objects.equals(doc.getString("EventId"), event.getId())) {
                                                //Replace and put in new EventID
                                                EventsRef.document(event.getId()).update("qrCode", DocCodeID);
                                            }
                                        } catch (Exception e) {
                                        }
                                    }
                                }
                            });
                            reuse = false;
                        }

                    }
                    //create event bundle to pass to details fragment
                    Bundle args = new Bundle();
                    args.putParcelable("event", event);
                    args.putBoolean("isOrganizer", true);

                    //navigate to event details fragment
                    //try catch to prevent crashes
                    try {
                        NavHostFragment.findNavController(EventEditFragment.this)
                                .navigate(R.id.action_eventEditFragment_pop, args);
                    } catch (Exception ignored) {}
                    return false; //required to prevent crashes
                } else {
                    setTextViewsClickablity(true);
                }
            }


        return false;
    }
    public boolean validateInput(){
        boolean validInput =true;

        if (eventNameEditText.getText().toString().length() == 0) {
            eventNameEditText.setError("Required Field.");
            validInput = false;
        }
        if (eventLocationEditText.getText().toString().length() == 0) {
            eventLocationEditText.setError("Required Field.");
            validInput = false;
        }
        if (eventStartDateTextView.getText().toString().length() == 0) {
            eventStartDateTextView.setError("Required Field.");
            validInput = false;
        } if (eventEndDateTextView.getText().toString().length() == 0) {
            eventEndDateTextView.setText(eventStartDateTextView.getText().toString());
        }if (DateHelper.getTimestamp(eventStartDateTextView.getText().toString()) > DateHelper.getTimestamp(eventEndDateTextView.getText().toString())) {
            eventEndDateTextView.setError("Invalid Date.");
            validInput = false;
        } if (eventStartTimeTextView.getText().toString().length() == 0) {
            eventStartTimeTextView.setError("Required Field.");
            validInput = false;
        }if (eventEndTimeTextView.getText().toString().length() == 0) {
            eventEndTimeTextView.setError("Required Field.");
            validInput = false;
        }
        if (maxAttendees.getText().toString().length() == 0) {
            maxAttendees.setError("Required Field.");
            validInput = false;
        }
        return validInput;
    }
    //TODO: not working
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ( resultCode==RESULT_OK){
            Bitmap capturedPhoto = event.getPoster().getBitmap();
            if (requestCode==1000) {
                capturedPhoto = (Bitmap) (data.getExtras().get("data"));
                event.setPoster(new EventPoster(event.getId(),capturedPhoto,event));
            } else if (requestCode == 1001){
                Bitmap photoUpload;
                Uri uploadPath = data.getData();

                try {
                    photoUpload = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), uploadPath);
                    event.setPoster(new EventPoster(event.getId(),photoUpload,event));

                } catch (Exception e){}
            }
            //event.getPoster().setBitmap(capturedPhoto);
            event.setPosterIsDefault(false);
            ImageView poster = getView().findViewById(R.id.edit_iv_poster);
            poster.setImageBitmap(capturedPhoto);

        }
    }
}