package com.example.mapapp;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

/**
 * Location Related view model
 * //https://www.youtube.com/watch?v=JOinUZLs3l8&t=619s
 */
public class LocationViewModel extends AndroidViewModel {
    private List<LocationEntry> locationEntries=new ArrayList<LocationEntry>();


    private  static final String Tag= LocationViewModel.class.getSimpleName();

    public LocationViewModel(@NonNull Application application) {
        super(application);

//        AppExecutors.getInstance().diskIO().execute(() -> {
//                    locationEntries = mLocationRepository.getLocations();
//                });



       // List<LocationEntry> a = new List<LocationEntry>();
        locationEntries.add(new LocationEntry( 14,"A.E. Phillips Laboratory School","32.525847","-92.650641"));// };
        locationEntries.add(new LocationEntry( 15,"Adam Hall","32.52536","-92.646972"));
        locationEntries.add(new LocationEntry( 15,"Art & Architecture Workshop","32.51515","-92.654365"));
        locationEntries.add(new LocationEntry( 16,"Aswell A","32.526138","-92.647278"));
        locationEntries.add(new LocationEntry( 17,"Aswell B","32.526111","-92.646881"));
        locationEntries.add(new LocationEntry( 18,"Aswell C","32.525822","-92.646785"));
        locationEntries.add(new LocationEntry( 19,"Aswell Hall","32.525847","-92.650641"));

    }

    public List<LocationEntry> getAllLocations(){

        return locationEntries;
    }

}
