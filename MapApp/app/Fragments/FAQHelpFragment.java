package com.example.latechengage.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.example.latechengage.Adapter.FaqHelpExpandableListAdapter;
import com.example.latechengage.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 *FAQHelpFragment static page.
 */
public class FAQHelpFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private ExpandableListView listView;
    private FaqHelpExpandableListAdapter listAdapter;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listHashMap;

    private OnFragmentInteractionListener mListener;

    public FAQHelpFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initData() {
        listDataHeader = new ArrayList<>();
        listHashMap = new HashMap<>();

        listDataHeader.add("What is LATech Engage?");
        List<String> whatDesc = new ArrayList<>();
        whatDesc.add("Louisiana Tech Engage is the first app to give students the ability to feel a part of Louisiana Tech University on a new level. With Louisiana Tech Engage, students have the opportunity to stay up to date with activities in the Louisiana Tech campus introduced by the Office of Research and Partnerships.\n" +
                "\n" +
                "\n" +
                "The app presents features, which include:\n" +
                "\n" +
                "— Real-time updated events\n" +
                "\n" +
                "— Campus map with eventVenue search\n" +
                "\n" +
                "— Louisiana Tech Academic Calendar\n" +
                "\n" +
                "— Student opportunities\n" +
                "\n" +
                "\n" +
                "Louisiana Tech Engage has the ability to notify students when a new event or opportunity is available. With push notifications and reminders, the app makes it easy for students to stay connected with current events happening on campus.");

        listDataHeader.add("Who is LATech Engage app for?");
        List<String> WhoDes = new ArrayList<>();
        WhoDes.add("Everest 1");

        listDataHeader.add("How can I view the list of upcoming events?");
        List<String> upcomingDesc = new ArrayList<>();
        upcomingDesc.add("Everest 1");

        listDataHeader.add("How do I filter the events by category?");
        List<String> CategoryDes = new ArrayList<>();
        CategoryDes.add("Everest 1");

        listDataHeader.add("How do I save an event?");
        List<String> SaveDes = new ArrayList<>();
        SaveDes.add("Everest 1");

        listDataHeader.add("What happens when I save an Event?");
        List<String> WhatSaveDes = new ArrayList<>();
        WhatSaveDes.add("Everest 1");

        listDataHeader.add("Can I share event with my friends?");

        List<String> ShareDes = new ArrayList<>();
        ShareDes.add("Everest 1");

        listHashMap.put(listDataHeader.get(0), whatDesc);
        listHashMap.put(listDataHeader.get(1), WhoDes);
        listHashMap.put(listDataHeader.get(2), upcomingDesc);
        listHashMap.put(listDataHeader.get(3), CategoryDes);
        listHashMap.put(listDataHeader.get(4), SaveDes);
        listHashMap.put(listDataHeader.get(5), WhatSaveDes);
        listHashMap.put(listDataHeader.get(6), ShareDes);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_faqhelp, container, false);
        listView = (ExpandableListView) view.findViewById(R.id.lvExpandableFaq);
        initData();
        Context a = this.getContext();
        listAdapter = new FaqHelpExpandableListAdapter(a, listDataHeader, listHashMap);
        listView.setAdapter(listAdapter);
            setListViewHeight(listView, 0);
        return view;
    }

    private void setListViewHeight(ExpandableListView listView,
                                   int group) {
        ExpandableListAdapter listAdapter = (ExpandableListAdapter) listView.getExpandableListAdapter();
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.EXACTLY);
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupItem = listAdapter.getGroupView(i, false, null, listView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

            totalHeight += groupItem.getMeasuredHeight();

            if (((listView.isGroupExpanded(i)) && (i != group))
                    || ((!listView.isGroupExpanded(i)) && (i == group))) {
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    View listItem = listAdapter.getChildView(i, j, false, null,
                            listView);
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

                    totalHeight += listItem.getMeasuredHeight();

                }
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        if (height < 10)
            height = 200;
        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
