package com.example.mapapp;

import androidx.annotation.NonNull;

public class LocationEntry {

    @NonNull
    public Integer id;
    public String title;
    public String latitude;
    public String longitude;

    public LocationEntry(@NonNull Integer id, String title, String latitude, String longitude) {
        this.id = id;
        this.title = title;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @NonNull
    public Integer getId() {
        return id;
    }

    public void setId(@NonNull Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
