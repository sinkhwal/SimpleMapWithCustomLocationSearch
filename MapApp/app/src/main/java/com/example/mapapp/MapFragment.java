package com.example.mapapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


/**
 * This is for map module. Event Listing, Reminder Listing and Map filters redirect location here.

 */
public class MapFragment extends Fragment {
    public static final String ARG_Latitude = "latitude";
    public static final String ARG_Longitude = "longitude";
    public static final String ARG_Title = "title";

    private Double mLatitude;
    private Double mLongitude;
    private String mTitle;

    private OnFragmentInteractionListener mListener;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setHasOptionsMenu(true);// this is for menu show option change

        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        if (getArguments() != null) {

            try {
                mLatitude = Double.parseDouble(getArguments().getString(ARG_Latitude));
                mLongitude = Double.parseDouble(getArguments().getString(ARG_Longitude));
                mTitle = getArguments().getString(ARG_Title);
            } catch (NumberFormatException e) {
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.frg);  //use SuppoprtMapFragment for using in fragment instead of activity  MapFragment = activity   SupportMapFragment = fragment
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                mMap.clear(); //clear old markers

                CameraPosition googlePlex = CameraPosition.builder()
                        .target(new LatLng(32.526348, -92.645694))
                        .zoom(15)
                        .bearing(0)
                        .tilt(45)
                        .build();

                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(googlePlex));

                if (mLatitude != null) {
                    CameraPosition googlePlex1 = CameraPosition.builder()
                            .target(new LatLng(mLatitude, mLongitude))
                            .zoom(15)
                            .bearing(0)
                            .tilt(45)
                            .build();

                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex1), 500, null);
                    mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(mLatitude, mLongitude))
                            .title(mTitle)).showInfoWindow();
                }

                mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {//this is for formatting snippet in google map marker

                    @Override
                    public View getInfoWindow(Marker marker) {
                        return null;
                    }

                    @Override
                    public View getInfoContents(Marker marker) {

                        LinearLayout info = new LinearLayout(getContext());

                        info.setPadding(20, 20, 20, 20);
                        info.setOrientation(LinearLayout.VERTICAL);

                        TextView title = new TextView(getContext());
                        title.setTextColor(Color.BLACK);
                        title.setGravity(Gravity.CENTER);
                        title.setTypeface(null, Typeface.BOLD);
                        title.setText(marker.getTitle());

                        TextView snippet = new TextView(getContext());
                        snippet.setTextColor(Color.GRAY);
                        snippet.setText(marker.getSnippet());

                        info.addView(title);
                        if (marker.getSnippet() != null)
                            info.addView(snippet);

                        return info;
                    }
                });
            }
        });
        return rootView;
    }


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

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
