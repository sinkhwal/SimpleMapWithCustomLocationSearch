package com.example.mapapp;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity implements MapFragment.OnFragmentInteractionListener, LocationFragment.OnListFragmentInteractionListener {
    private FragmentManager fragmentManager;

    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputManager = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        // check if no view has focus:
        View currentFocusedView = activity.getCurrentFocus();

        if (currentFocusedView != null) {
            inputManager.hideSoftInputFromWindow(currentFocusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // This is for fragment back button enabling in toolbar if fragment has parent fragment. else drawer is shown.
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    //  drawer.setdraweri(false);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);// show back button
                    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                            onBackPressed();
                        }
                    });
                }
            }
        });


        MapFragment fragment = new MapFragment();

        //  replaceFragment(fragment);
        fragmentManager = this.getSupportFragmentManager();

        // Begin Fragment transaction.
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Replace the layout holder with the required Fragment object.
        fragmentTransaction.replace(R.id.mainframe, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (item.getItemId() == R.id.action_search) {
            Bundle arguments = new Bundle();
            LocationFragment fragment1 = new LocationFragment();
            fragment1.setArguments(arguments);
            fragmentManager = this.getSupportFragmentManager();
            // Begin Fragment transaction.
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            // Replace the layout holder with the required Fragment object.
            fragmentTransaction.replace(R.id.mainframe, fragment1).addToBackStack("MapFragment");
            fragmentTransaction.commit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onListFragmentInteraction(LocationEntry locationEntry) {
        getSupportFragmentManager().popBackStack();// this is for showing drawer.. removing back button
        getSupportFragmentManager().popBackStack();
        hideKeyboard(this);
        navigateToMap(locationEntry);
    }

    private void navigateToMap(LocationEntry locationEntry) {
        Bundle arguments = new Bundle();

        arguments.putString(MapFragment.ARG_Latitude, locationEntry.latitude);
        arguments.putString(MapFragment.ARG_Longitude, locationEntry.longitude);
        arguments.putString(MapFragment.ARG_Title, locationEntry.title);
        MapFragment fragment = new MapFragment();
        fragment.setArguments(arguments);
        //  replaceFragment(fragment);
        fragmentManager = this.getSupportFragmentManager();

        // Begin Fragment transaction.
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Replace the layout holder with the required Fragment object.
        fragmentTransaction.replace(R.id.mainframe, fragment);

        fragmentTransaction.commit();
    }
}
