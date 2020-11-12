package com.ac.assignment_project_014.TicketMaster;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.ac.assignment_project_014.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TicketMasterSavedEventsActivity extends AppCompatActivity implements EventDetailFragment.OnRemoveFavoriteEventListener{

    private List<Event> events;
    private EventsAdapter myEventsAdapter;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ticketmaster_saved_events);

        // tool bar
        Toolbar toolBar = findViewById(R.id.deezer_toolbar);
        setSupportActionBar(toolBar);

        boolean isTablet = findViewById(R.id.fragment_event_detail) != null;

        ListView savedEventsLV = findViewById(R.id.savedListView);

        events = new ArrayList<>();
        myEventsAdapter = new EventsAdapter();
        savedEventsLV.setAdapter(myEventsAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();

        // always reload favorite events when enter or back from other activity
        loadSavedEvent();
    }


    private void loadSavedEvent() {
        events.clear();
        String[] columns = {Event.COL_ID, Event.COL_NAME, Event.COL_DATE, Event.COL_MINPRICE, Event.COL_MAXPRICE,Event.COL_URL,Event.COL_IMAGE};
        Cursor results = db.query(false, Event.TABLE_NAME_FAVORITE, columns,
                null, null, null, null, null, null);

        int idColIndex = results.getColumnIndex(Event.COL_ID);
        int nameColIndex = results.getColumnIndex(Event.COL_NAME);
        int dateColIndex = results.getColumnIndex(Event.COL_DATE);
        int minPriceColIndex = results.getColumnIndex(Event.COL_MINPRICE);
        int maxPriceCoverColIndex = results.getColumnIndex(Event.COL_MAXPRICE);
        int urlColIndex = results.getColumnIndex(Event.COL_URL);
        int imageColIndex = results.getColumnIndex(Event.COL_IMAGE);

        while (results.moveToNext()) {
            Event event = new Event();
            event.setId(results.getInt(idColIndex));
            event.setName(results.getString(nameColIndex));
            event.setDate(results.getString(dateColIndex));
            event.setMinPrice(results.getString(minPriceColIndex));
            event.setMaxPrice(results.getString(maxPriceCoverColIndex));
            event.setURL(results.getString(urlColIndex));
            event.setImage(results.getString(imageColIndex));

            events.add(event);
        }

        myEventsAdapter.notifyDataSetChanged();
    }

    /**
     * reference to: https://developer.android.com/training/basics/fragments/communicating.html
     * @param fragment
     */
    @Override
    public void onAttachFragment(Fragment fragment) {
        if (fragment instanceof EventDetailFragment) {
            ((EventDetailFragment)fragment).setCallback(this);
        }
    }


    @Override
    public void removeEvent(long eventId) {
        // TODO remove song from songs and update list view
        Event event = null;
        for (Event e : events) {
            if (e.getId() == eventId) {
                event = e;
                break;
            }
        }

        if (event != null) {
            events.remove(event);
            myEventsAdapter.notifyDataSetChanged();
        }
    }



    /**
     * song adapter used by list view for songs display
     */
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
            TextView eventnameLV = convertView.findViewById(R.id.event_name_tv);
            TextView eventDateLV = convertView.findViewById(R.id.event_date_tv);

            Event event = getItem(position);
            eventnameLV.setText(String.format(Locale.getDefault(), "%d. %s", position + 1, event.getName()));
            eventDateLV.setText(event.getDate());

            return convertView;
        }
    }
}