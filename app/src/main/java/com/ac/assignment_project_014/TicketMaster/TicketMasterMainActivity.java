package com.ac.assignment_project_014.TicketMaster;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.ac.assignment_project_014.R;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class TicketMasterMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String SEARCH_CITY = "SEARCH_CITY";
    private static final String SEARCH_RADIUS = "SEARCH_RADIUS";

    private ProgressBar progressBar;

    private ListView eventListView;

    private EventsAdapter eventsAdapter;

    private List<Event> events;

    private SharedPreferences prefsCity;
    private SharedPreferences prefsRadius;

    private boolean isTablet;

    private SQLiteDatabase db;

    private EditText cityNameText = null;
    private EditText radiusText = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ticketmaster_activity_main);

        //toolbar
        Toolbar toolBar = findViewById(R.id.ticketmaster_toolbar);
        setSupportActionBar(toolBar);

        //navigation bar
        DrawerLayout drawerLayout = findViewById(R.id.ticketmaster_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolBar, R.string.ticketmaster_navigation_open, R.string.ticketmaster_navigation_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.ticketmaster_navigation_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);

        prefsCity = getSharedPreferences("city", Context.MODE_PRIVATE);
        prefsRadius = getSharedPreferences("radius", Context.MODE_PRIVATE);
        String searchCity = prefsCity.getString(SEARCH_CITY, "");
        String searchRadius = prefsCity.getString(SEARCH_RADIUS, "");

        /**
         * init components
         */
        EditText cityNameEditText = findViewById(R.id.cityName);
        EditText radiusEditText = findViewById(R.id.radius);
        cityNameEditText.setOnEditorActionListener((v, actionId, event) -> {
            searchEvent(cityNameEditText.getText().toString().trim(), radiusEditText.getText().toString().trim());
            return true;
        });
        cityNameEditText.setText(searchCity);
        radiusEditText.setText(searchRadius);

        Button btnSearch = findViewById(R.id.searchBtn);
        Button btnFavSong = findViewById(R.id.savedEventsBtn);
        progressBar = findViewById(R.id.processBar);
        eventListView = findViewById(R.id.eventListView);

        isTablet = (findViewById(R.id.fragment_event_detail) != null);

/**
 * init data holder and adapter
 */
        events = new ArrayList<>();
        eventsAdapter = new EventsAdapter();
        eventListView.setAdapter(eventsAdapter);

        eventListView.setOnItemClickListener((parent, view, position, id) -> {
            Event event = events.get(position);

            view.setSelected(true);

            Bundle bundle = new Bundle();
            bundle.putBoolean(EventDetailFragment.KEY_IS_TABLET, isTablet);
            bundle.putBoolean(EventDetailFragment.KEY_IS_FAVORITE, false);
            bundle.putString(EventDetailFragment.KEY_EVENT_NAME, event.getName());
            bundle.putString(EventDetailFragment.KEY_EVENT_DATE, event.getDate());
            bundle.putString(EventDetailFragment.KEY_EVENT_MIN_PRICE, event.getMinPrice());
            bundle.putString(EventDetailFragment.KEY_EVENT_MAX_PRICE, event.getMaxPrice());
            bundle.putString(EventDetailFragment.KEY_EVENT_URL, event.getURL());
            bundle.putString(EventDetailFragment.KEY_EVENT_IMAGE, event.getImage());

            if (isTablet) {
                // init fragment
                // show fragment -- referenced professor Islam's work
                EventDetailFragment eventDetailFragment = new EventDetailFragment();
                eventDetailFragment.setArguments(bundle);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_event_detail, eventDetailFragment) //Add the fragment in FrameLayout
                        .commit(); //actually load the fragment. Calls onCreate() in DetailFragment

            } else {
                Intent intent = new Intent(TicketMasterMainActivity.this, TicketMasterEventDetailsActivity.class);
                intent.putExtra(TicketMasterEventDetailsActivity.EVENT_DETAIL, bundle);
                startActivity(intent);
            }
        });

        btnSearch.setOnClickListener(v -> searchEvent(cityNameEditText.getText().toString().trim(), radiusEditText.getText().toString().trim()));

        btnFavSong.setOnClickListener(v -> {
            Intent intent = new Intent(TicketMasterMainActivity.this, TicketMasterSavedEventsActivity.class);
            startActivity(intent);
        });

        EventOpenHelper eventDB = new EventOpenHelper(this);
        db = eventDB.getWritableDatabase();
        //loadLastSearchResult();
    }

    private void searchEvent(String cityName, String radius) {
        if (cityName.isEmpty() || radius.isEmpty()) {
            // show alert to tell user input something
            showAlertMessageWithTitle(getString(R.string.ticketmaster_alert_title), getString(R.string.ticketmaster_alert_message));
            return;
        }

        // hide keyboard when start searching.
        // reference to: https://stackoverflow.com/questions/1109022/how-do-you-close-hide-the-android-soft-keyboard-using-java
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

        SharedPreferences.Editor editorCity = prefsCity.edit();
        SharedPreferences.Editor editorRadius = prefsRadius.edit();
        editorCity.putString(SEARCH_CITY, cityName);
        editorRadius.putString(SEARCH_RADIUS, radius);
        editorCity.apply();
        editorRadius.apply();

        Snackbar snackbar = Snackbar.make(eventListView,
                String.format(getString(R.string.ticketmaster_search_events_searby), cityName),
                Snackbar.LENGTH_LONG);
        snackbar.show();

        // search Artist first
        progressBar.setVisibility(View.VISIBLE);

        QueryEvent queryEvent = new QueryEvent();
        queryEvent.execute(String.format("https://app.ticketmaster.com/discovery/v2/events.json?apikey=7UF99kWXsCJZmFLtlXGJ11mx77gM2v1D&city=%s&radius=%s", cityName, radius));
    }

    private void addToSearchResult(Event event) {
        ContentValues newRowValue = new ContentValues();
        newRowValue.put(Event.COL_NAME, event.getName());
        newRowValue.put(Event.COL_DATE, event.getDate());
        newRowValue.put(Event.COL_MINPRICE, event.getMinPrice());
        newRowValue.put(Event.COL_MAXPRICE, event.getMaxPrice());
        newRowValue.put(Event.COL_URL, event.getURL());
        newRowValue.put(Event.COL_IMAGE, event.getImage());

        db.insert(Event.TABLE_NAME_SEARCH_RESULT, null, newRowValue);
    }

    /**
     * Initialize menu here
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.ticketmaster_toolbar, menu);
        return true;
    }

//    /**
//     * handle toolbar menu item click event
//     */
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.A_toolbar:
//                Intent goToGeo = new Intent(this, A.class);
//                startActivity(goToGeo);
//                break;
//            case R.id.B_toolbar:
//                Intent goToLyrics = new Intent(this, B.class);
//                startActivity(goToLyrics);
//                break;
//            case R.id.C:
//                Intent goToSoccer = new Intent(this, C.class);
//                startActivity(goToSoccer);
//                break;
//            case R.id.ticketmaster_menu_item_about:
//                Toast.makeText(this, "This is the Ticket Master search project using TicketMaster api, written by Xingan Wang", Toast.LENGTH_SHORT).show();
//                break;
//        }
//        return true;
//    }

    /**
     * Implement interface method, to reactive with navigation items
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.ticketmaster_nav_item_help:
                showAlertMessageWithTitle(getString(R.string.ticketmaster_help_itle), getString(R.string.ticketmaster_help_infor));
                break;
            case R.id.ticketmaster_nav_item_about:
                String apiLink = "https://developer-acct.ticketmaster.com";
                Intent launchBrower = new Intent(Intent.ACTION_VIEW, Uri.parse(apiLink));
                startActivity(launchBrower);
                break;
        }

        DrawerLayout drawerLayout = findViewById(R.id.ticketmaster_drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);

        return false;
    }

    private void showAlertMessageWithTitle(String title, String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton("Ok", (v, arg) -> {
        });

        alertDialogBuilder.create().show();
    }


    private class QueryEvent extends AsyncTask<String, Integer, List<Event>> {

        @Override
        protected List<Event> doInBackground(String... strings) {
            List<Event> eventList = new ArrayList<>();
            try {
                URL url;
                HttpURLConnection urlConnection;
                InputStream response;
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                response = urlConnection.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(response, StandardCharsets.UTF_8), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                String result = sb.toString();

                // clear search result in db
                db.delete(Event.TABLE_NAME_SEARCH_RESULT, null, null);

                // get event info and store it in eventList
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getInt("totalElements") > 0) {
                    JSONArray eventArray = (JSONArray) jsonObject.getJSONArray("events");
                    for (int i = 0; i < eventArray.length(); i++) {
                        JSONObject eventObj = (JSONObject) eventArray.get(i);

                        Event event = new Event();
                        event.setName(eventObj.getString("name"));
                        event.setURL(eventObj.getString("URL"));

                        JSONObject dataObj = eventObj.getJSONObject("dates");
                        JSONObject startObj = dataObj.getJSONObject("start");
                        event.setDate(startObj.getString("localDate"));

                        JSONObject priceRageObj = eventObj.getJSONObject("priceRange");
                        event.setMinPrice(priceRageObj.getString("min"));
                        event.setMaxPrice(priceRageObj.getString("max"));

                        JSONArray imageArray = (JSONArray) eventObj.getJSONArray("images");
                        JSONObject imageObj = (JSONObject) imageArray.get(0);
                        event.setImage(imageObj.getString("url"));

                        eventList.add(event);
                        addToSearchResult(event);
                    }
                }
            } catch (IOException | JSONException e) {
                Log.e("DSS", e.getMessage());
            }

            return eventList;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(List<Event> eventList) {
            super.onPostExecute(eventList);

            events.clear();
            events.addAll(eventList);

            eventsAdapter.notifyDataSetChanged();

            progressBar.setVisibility(View.GONE);

            Toast.makeText(TicketMasterMainActivity.this,
                    String.format(getString(R.string.ticketmaster_find_events_number), eventList.size()),
                    Toast.LENGTH_LONG).show();
        }
    }


    class EventsAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return events.size();
        }

        @Override
        public Event getItem(int position) {
            return events.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater layoutInflater = getLayoutInflater();
                convertView = layoutInflater.inflate(R.layout.ticketmaster_event_item, parent, false);
            }
            TextView eventDateTV = convertView.findViewById(R.id.event_date_tv);
            TextView eventNameTV = convertView.findViewById(R.id.event_name_tv);

            Event event = getItem(position);
            eventNameTV.setText(event.getName());
            eventDateTV.setText(event.getDate());

            return convertView;
        }
    }


}








