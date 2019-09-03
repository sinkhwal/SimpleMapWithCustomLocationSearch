package com.example.latechengage.Fragments.EventsRelated;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.latechengage.Adapter.EventsListRecyclerViewAdapter;
import com.example.latechengage.R;
import com.example.latechengage.ViewModel.EventViewModel;
import com.example.latechengage.database.EventEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Events.
 * This is first tab in the app, list Events with authors, date, event title etc.
 * Give option to put reminder by bell icon.
 * Periodically refresh events. in second to refresh events only when you are in this tab.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class EventListFragment extends Fragment {

    public static int currentVisiblePosition;
    Handler handler = new Handler();
    int apiDelayed = 60 * 1000; //in second to refresh events only when you are in this tab.
    Runnable runnable;
    private OnListFragmentInteractionListener mListener;
    private OnListFragmentImageClickListner listner;
    private OnListFragmentLocationClickListner locationlistner;
    private List<EventEntry> eventList = new ArrayList<>();
    private List<EventEntry> eventListFiltered = new ArrayList<>();
    private List<String> eventCategoryEntries;
    private EventViewModel viewModel;
    private RecyclerView recyclerView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public EventListFragment() {


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);// this is for menu show option change
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_eventlist_list, container, false);

            Context context = view.getContext();
            recyclerView = (RecyclerView) view.findViewById(R.id.event_list_recycle_view);

            recyclerView.setLayoutManager(new LinearLayoutManager(context));
recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(50);

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    currentVisiblePosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();

                }
            });

            viewModel = ViewModelProviders.of(getActivity()).get(EventViewModel.class);
            viewModel.getEvents().observe(this, eventEntries -> {
                eventList = eventEntries;
                liveDataChangeEvent();

            });

            viewModel.getAllEventCategories().observe(this, ev -> liveDataChangeEvent());

        return view;
    }

    private void liveDataChangeEvent() {
        eventListFiltered.clear();
        eventCategoryEntries = viewModel.getSelectedEventCategories();

        for (EventEntry eE : eventList) {
            if (eE.eventIsFeatured == 1) {
                eventListFiltered.add(eE);
                continue;
            }
            if (eventCategoryEntries != null)
                for (String ecE : eventCategoryEntries) {


                    if (eE.eventCategories.contains(ecE)) {
                        eventListFiltered.add(eE);
                        break;
                    }
                }
        }

        recyclerView.setAdapter(new EventsListRecyclerViewAdapter(eventListFiltered, mListener, listner, locationlistner));
        (recyclerView.getLayoutManager()).scrollToPosition(currentVisiblePosition);

        TextView emptyView = (TextView) getActivity().findViewById(R.id.empty_view);
        if (eventListFiltered.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
        if (context instanceof OnListFragmentImageClickListner) {
            listner = (OnListFragmentImageClickListner) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }

        if (context instanceof OnListFragmentLocationClickListner) {
            locationlistner = (OnListFragmentLocationClickListner) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        listner = null;
    }

    /**
     * On resume it again start waiting for refreshing events.
     */
    @Override
    public void onResume() {
        super.onResume();
        (recyclerView.getLayoutManager()).scrollToPosition(currentVisiblePosition);
        handler.postDelayed(runnable = () -> {
            ConnectivityManager cm =
                    (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            viewModel.refreshEvents();
            handler.postDelayed(runnable, apiDelayed);
        }, apiDelayed);
    }

    @Override
    public void onPause() {
        super.onPause();
        // this variable should be static in class
        currentVisiblePosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        handler.removeCallbacks(runnable); //stop handler when activity not visible
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(EventEntry event);
    }

    public interface OnListFragmentImageClickListner {
        void OnListFragmentImageClick(EventEntry event, View view);
    }

    public interface OnListFragmentLocationClickListner {
        void OnListFragmentLocationClick(EventEntry event, View view);
    }
}
