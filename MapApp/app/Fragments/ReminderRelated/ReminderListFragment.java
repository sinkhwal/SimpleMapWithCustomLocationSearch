package com.example.latechengage.Fragments.ReminderRelated;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.latechengage.Adapter.ReminderEventsListRecyclerViewAdapter;
import com.example.latechengage.R;
import com.example.latechengage.ViewModel.EventViewModel;
import com.example.latechengage.ViewModel.ReminderViewModel;
import com.example.latechengage.database.ReminderEventEntry;

import java.util.ArrayList;
import java.util.List;


/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ReminderListFragment extends Fragment {
    private OnListFragmentInteractionListener mListener;
    private OnListFragmentImageClickListner listner;
    private OnListFragmentLocationClickListner locationlistner;
    private List<ReminderEventEntry> eventList = new ArrayList<>();
    private List<ReminderEventEntry> eventListFiltered = new ArrayList<>();
    private List<String> eventCategoryEntries;

    private RecyclerView recyclerView;
    private ReminderViewModel viewModelR;
    private EventViewModel viewModel;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ReminderListFragment() {


    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ReminderListFragment newInstance(int columnCount, boolean isGeneral, boolean isSeminar, boolean isTalk, boolean isWorkShop) {
        ReminderListFragment fragment = new ReminderListFragment();
        return fragment;
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
        viewModelR = ViewModelProviders.of(getActivity()).get(ReminderViewModel.class);
        viewModel = ViewModelProviders.of(getActivity()).get(EventViewModel.class);
        viewModelR.getEvents().observe(this, eventEntries -> {
            eventList = eventEntries;
            liveDataChageEvent();

        });
        viewModel.getAllEventCategories().observe(this, ev -> liveDataChageEvent());
        return view;
    }

    private void liveDataChageEvent() {
        eventListFiltered.clear();
        eventCategoryEntries = viewModel.getSelectedEventCategories();

        for (ReminderEventEntry eE : eventList) {

            if (eventCategoryEntries != null)
                for (String ecE : eventCategoryEntries) {
                    if (eE.eventCategories.contains(ecE)) {
                        eventListFiltered.add(eE);
                        break;
                    }
                }
        }
        recyclerView.setAdapter(new ReminderEventsListRecyclerViewAdapter(eventListFiltered, mListener, listner, locationlistner));
        TextView emptyView = (TextView) getActivity().findViewById(R.id.empty_view);
        emptyView.setText("It seems like you do not have any event, from the selected categories, saved.");

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
        void onReminderListFragmentInteraction(ReminderEventEntry event);
    }

    public interface OnListFragmentImageClickListner {
        void OnRemindListFragmentImageClick(ReminderEventEntry event, View view);
    }

    public interface OnListFragmentLocationClickListner {
        void OnListFragmentLocationClick(ReminderEventEntry event, View view);
    }
}
