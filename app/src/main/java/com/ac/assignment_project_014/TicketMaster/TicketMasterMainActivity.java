package com.ac.assignment_project_014.TicketMaster;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.ac.assignment_project_014.R;

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

/**
 * Main activity of the Ticket Master Event Search api to search events by city name and radius
 */
public class TicketMasterMainActivity extends TicketMasterDrawerBase {

    public static final String EVENT_ID = "EVENT_ID";
    public static final String EVENT_NAME = "EVENT_NAME";
    public static final String EVENT_DATE = "EVENT_DATE";
    public static final String EVENT_MIN_PRICE = "EVENT_MINPRICE";
    public static final String EVENT_MAX_PRICE = "EVENT_MAXPRICE";
    public static final String EVENT_URL = "EVENT_URL";
    public static final String EVENT_IMAGE = "EVENT_IMAGE";
    public static final String IS_TABLET = "IS_TABLET";
    public static final String IS_FAVORITE = "IS_FAV";


    /**
     * table name of the search result
     */
    public final static String SEARCH_RESULT_TABLE = "TICKET_MASTER_EVENT_LIST";

    /**
     * table name of the saved events
     */
    public final static String EVENT_SAVED_TABLE = "EVENT_SAVED";

    /**
     * column: id of the event
     */
    public final static String COL_ID = "_id";

    /**
     * column: name of the event
     */
    public final static String COL_NAME = "NAME";

    /**
     * column: date of the event
     */
    public final static String COL_DATE = "DATE";

    /**
     * column: minimum price of the event
     */
    public final static String COL_MINPRICE = "MINPRICE";
    /**
     * column: max price of the event
     */
    public final static String COL_MAXPRICE = "MAXPRICE";

    /**
     * column: url of the event
     */
    public final static String COL_URL = "URL";

    /**
     * column: image of the event
     */
    public final static String COL_IMAGE = "IMAGE";

    /**
     * data holder of events
     */
    private List<Event> events;

    /**
     * database to save the seach result.
     */
    private SQLiteDatabase db;

    /**
     * the progress bar to show that the app is fetching data from server
     */
    private ProgressBar progressBar;

    /**
     * the adapter used to show events in the listview
     */
    private EventsAdapter eventsAdapter;

    /**
     * shared preference to store the city name that the user typed last time
     */
    private SharedPreferences prefsCity;

    /**
     * shared preference to store the radius that the user typed last time
     */
    private SharedPreferences prefsRadius;

    /**
     * to check if the device is Tablet
     */
    private boolean isTablet;


    /**
     * onCreate method
     * @param savedInstanceState saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefsCity = getSharedPreferences("city", Context.MODE_PRIVATE);
        prefsRadius = getSharedPreferences("radius", Context.MODE_PRIVATE);
        String searchCity = prefsCity.getString("SEARCH_CITY", "");
        String searchRadius = prefsRadius.getString("SEARCH_RADIUS", "");


        EditText cityNameEditText = findViewById(R.id.cityName);
        EditText radiusEditText = findViewById(R.id.radius);
        cityNameEditText.setOnEditorActionListener((v, actionId, event) -> {
            searchEvent(cityNameEditText.getText().toString(), radiusEditText.getText().toString());
            return true;
        });
        cityNameEditText.setText(searchCity);
        radiusEditText.setText(searchRadius);

        Button searchBtn = findViewById(R.id.searchBtn);
        Button savedEventsBtn = findViewById(R.id.savedEventsBtn);

        progressBar = findViewById(R.id.processBar);

        ListView eventListView = findViewById(R.id.eventListView);

        isTablet = findViewById(R.id.fragment_event_details) != null;

        events = new ArrayList<>();
        eventsAdapter = new EventsAdapter();
        eventListView.setAdapter(eventsAdapter);

        eventListView.setOnItemClickListener((parent, view, position, id) -> {
            view.setSelected(true);
            Event event = events.get(position);
            Bundle dataToPass = new Bundle();
            dataToPass.putBoolean(IS_TABLET, isTablet);
            dataToPass.putBoolean(IS_FAVORITE, false);
            dataToPass.putString(EVENT_NAME, event.getName());
            dataToPass.putString(EVENT_DATE, event.getDate());
            dataToPass.putString(EVENT_MIN_PRICE, event.getMinPrice());
            dataToPass.putString(EVENT_MAX_PRICE, event.getMaxPrice());
            dataToPass.putString(EVENT_URL, event.getURL());
            dataToPass.putString(EVENT_IMAGE, event.getImage());

            if (isTablet) {
                EventDetailFragment eventDetailFragment = new EventDetailFragment();
                eventDetailFragment.setArguments(dataToPass);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_event_details, eventDetailFragment)
                        .commit();

            } else {
                Intent intent = new Intent(this, TicketMasterEventDetailsActivity.class);
                intent.putExtra("EVENT_DETAIL", dataToPass);
                startActivity(intent);
            }
        });

        searchBtn.setOnClickListener(v -> searchEvent(cityNameEditText.getText().toString(), radiusEditText.getText().toString()));

        savedEventsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, TicketMasterSavedEventsActivity.class);
            startActivity(intent);
        });

        EventOpenHelper eventDB = new EventOpenHelper(this);
        db = eventDB.getWritableDatabase();
    }

    /**
     * search events by city name and radius
     * @param cityName city name that user entered
     * @param radius radius that user entered
     */
    private void searchEvent(String cityName, String radius) {

        SharedPreferences.Editor editorCity = prefsCity.edit();
        SharedPreferences.Editor editorRadius = prefsRadius.edit();
        editorCity.putString("SEARCH_CITY", cityName);
        editorRadius.putString("SEARCH_RADIUS", radius);
        editorCity.apply();
        editorRadius.apply();

        if (cityName.isEmpty() || radius.isEmpty()) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle(R.string.ticketmaster_alert_title);
            alertDialogBuilder.setMessage(R.string.ticketmaster_alert_message);
            alertDialogBuilder.setPositiveButton("OK", (v, arg) -> {
            });
            alertDialogBuilder.create().show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        GetEvent getEvent = new GetEvent();
        getEvent.execute(String.format("https://app.ticketmaster.com/discovery/v2/events.json?apikey=7UF99kWXsCJZmFLtlXGJ11mx77gM2v1D&city=%s&radius=%s", cityName, radius));
    }


    /**
     * override the getLayoutId() method of the TicketMasterDrawerBase class
     * @return layout of the ticketmaster_activity_main
     */
    @Override
    protected int getLayoutId() {
        return R.layout.ticketmaster_activity_main;
    }

    /**
     * event adapter used by list view for event display
     */
    class EventsAdapter extends BaseAdapter {

        /**
         * getter of the size of the events
         * @return size of the events
         */
        @Override
        public int getCount() {
            return events.size();
        }

        /**
         * getter for event instance
         * @param position index in event list
         * @return the event instance with the index passed
         */
        @Override
        public Event getItem(int position) {
            return events.get(position);
        }

        /**
         * getter for database id
         * @param position the index in event list
         * @return the database id
         */
        @Override
        public long getItemId(int position) {
            return position;
        }

        /**
         * getter for view
         * @param position index
         * @param convertView view
         * @param parent parent view
         * @return the view
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = getLayoutInflater();
            View view = layoutInflater.inflate(R.layout.ticketmaster_event_item, parent, false);
            TextView eventDateTV = view.findViewById(R.id.event_date_tv);
            TextView eventNameTV = view.findViewById(R.id.event_name_tv);

            Event event = getItem(position);
            eventNameTV.setText(event.getName());
            eventDateTV.setText(event.getDate());

            return view;
        }
    }

    /**
     * class of AsyncTask to get events information from server
     */
    private class GetEvent extends AsyncTask<String, Integer, List<Event>> {

        /**
         * fetch data in the backgroud thread
         * @param strings url
         * @return the search result as an event list
         */
        @Override
        protected List<Event> doInBackground(String... strings) {
            List<Event> eventList = new ArrayList<>();
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream response = urlConnection.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(response, StandardCharsets.UTF_8), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                String result = sb.toString();

                db.delete(SEARCH_RESULT_TABLE, null, null);

                JSONObject jsonObject = new JSONObject(result);
                if(jsonObject.getJSONObject("page").getInt("size") > 0){
                    JSONObject embeddedObj = jsonObject.getJSONObject("_embedded");
                    JSONArray eventArray = (JSONArray) embeddedObj.getJSONArray("events");
                    for (int i = 0; i < eventArray.length(); i++) {
                        JSONObject eventObj = (JSONObject) eventArray.get(i);

                        Event event = new Event();
                        event.setName(eventObj.getString("name"));
                        event.setURL(eventObj.getString("url"));

                        JSONObject dataObj = eventObj.getJSONObject("dates");
                        JSONObject startObj = dataObj.getJSONObject("start");
                        event.setDate(startObj.getString("localDate"));

                        JSONArray priceRageArray = (JSONArray)eventObj.getJSONArray("priceRanges");
                        JSONObject priceRageObj = (JSONObject)priceRageArray.get(0);
                        event.setMinPrice(priceRageObj.getString("min"));
                        event.setMaxPrice(priceRageObj.getString("max"));

                        JSONArray imageArray = (JSONArray) eventObj.getJSONArray("images");
                        JSONObject imageObj = (JSONObject) imageArray.get(0);
                        event.setImage(imageObj.getString("url"));
                        eventList.add(event);

                        addEventItem(event);
                    }
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return eventList;
        }

        /**
         * add event to the search result
         * @param event event
         */
        private void addEventItem(Event event) {
            ContentValues newRowValue = new ContentValues();
            newRowValue.put(COL_NAME, event.getName());
            newRowValue.put(COL_DATE, event.getDate());
            newRowValue.put(COL_MINPRICE, event.getMinPrice());
            newRowValue.put(COL_MAXPRICE, event.getMaxPrice());
            newRowValue.put(COL_URL, event.getURL());
            newRowValue.put(COL_IMAGE, event.getImage());

            db.insert(SEARCH_RESULT_TABLE, null, newRowValue);
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        /**
         * this method is called after the doInBcakground method
         * @param eventList search result
         */
        @Override
        protected void onPostExecute(List<Event> eventList) {
            super.onPostExecute(eventList);
            events.clear();
            events.addAll(eventList);
            eventsAdapter.notifyDataSetChanged();
            progressBar.setVisibility(View.VISIBLE);

        }
    }


}








