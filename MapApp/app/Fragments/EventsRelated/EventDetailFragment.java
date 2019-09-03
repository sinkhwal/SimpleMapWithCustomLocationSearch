package com.example.latechengage.Fragments.EventsRelated;

import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.latechengage.Fragments.MapFragment;
import com.example.latechengage.R;
import com.example.latechengage.database.AppDatabase;
import com.example.latechengage.database.EventEntry;
import com.example.latechengage.utilities.AppExecutors;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in
 * in two-pane mode (on tablets)
 */
public class EventDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_Event_ID = "id";
    Button btnShare;
    Button btnAddToCalendar;
    Intent sharedIntent;
    private AppDatabase appDatabase;
    /**
     * The dummy content this fragment is presenting.
     */
    private EventEntry mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public EventDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_Event_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.

            appDatabase = AppDatabase.getsInstance(getContext());
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.

            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    mItem = appDatabase.eventDao().loadEventDetailById(getArguments().getInt(ARG_Event_ID));
                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.event_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.event_heading)).setText(mItem.eventTitle);
            ((TextView) rootView.findViewById(R.id.item_author)).setText(mItem.eventPresenter);
            ((TextView) rootView.findViewById(R.id.event_date)).setText(mItem.eventFrom.toString());
            ((TextView) rootView.findViewById(R.id.event_detail)).setText(mItem.eventDescription);

            String venue = mItem.eventVenue;
            String[] venues = venue.split("\\|");
            ((TextView) rootView.findViewById(R.id.event_location)).setText(venues[0]);
            ((TextView) rootView.findViewById(R.id.event_location)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showLocationInMap();

                }
            });

            ((ImageView) rootView.findViewById(R.id.personIcon1)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showLocationInMap();
                }
            });
        }
        return rootView;
    }

    private void showLocationInMap() {
        getActivity().getSupportFragmentManager().popBackStack();// this is for showing drawer.. removing back button
        Bundle arguments = new Bundle();
        String venue = mItem.eventVenue;
        String[] venues = venue.split("\\|");
        arguments.putString(MapFragment.ARG_Latitude, venues[1]);
        arguments.putString(MapFragment.ARG_Longitude, venues[2]);
        arguments.putString(MapFragment.ARG_Title, venues[0]);
        MapFragment fragment = new MapFragment();
        fragment.setArguments(arguments);
        //  replaceFragment(fragment);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        // Begin Fragment transaction.
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Replace the layout holder with the required Fragment object.
        fragmentTransaction.replace(R.id.mainframe, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        btnShare = getView().findViewById(R.id.btnShareEvent);
        btnShare.setOnClickListener(v -> {

            sharedIntent = new Intent(Intent.ACTION_SEND);
            sharedIntent.setType("text/plain");
            sharedIntent.putExtra(Intent.EXTRA_SUBJECT, mItem.eventTitle);

            String content = "Event Title: " + mItem.eventTitle + "\n Event Time: " + mItem.eventFrom + "\n Event Location: " + mItem.eventVenue;
            sharedIntent.putExtra(Intent.EXTRA_TEXT, content);
            startActivity(Intent.createChooser(sharedIntent, "Share Via"));
        });
//TODO: datetime conversion and data change
//button add to calender event handling
        btnAddToCalendar = getView().findViewById(R.id.btnAddToCalender);
        btnAddToCalendar.setOnClickListener(v -> {

            Calendar cal = Calendar.getInstance();
            cal.setTime(mItem.eventFrom);

            Calendar beginTime = Calendar.getInstance();
            beginTime.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE),cal.get(Calendar.AM_PM));
            Calendar endTime = Calendar.getInstance();
            endTime.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE),cal.get(Calendar.AM_PM));
            Intent intent = new Intent(Intent.ACTION_INSERT)
                    .setData(CalendarContract.Events.CONTENT_URI)
                    .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                    .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                    .putExtra(CalendarContract.Events.TITLE, mItem.eventTitle)
                    .putExtra(CalendarContract.Events.DESCRIPTION, mItem.eventDescription)
                    .putExtra(CalendarContract.Events.EVENT_LOCATION, mItem.eventVenue)
                    .putExtra(CalendarContract.Events._ID, mItem.eventID);
            startActivity(intent);
        });
    }
}
