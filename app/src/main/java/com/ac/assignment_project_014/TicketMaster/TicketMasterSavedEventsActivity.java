package com.ac.assignment_project_014.TicketMaster;

import android.content.Intent;
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
import androidx.fragment.app.Fragment;

import com.ac.assignment_project_014.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TicketMasterSavedEventsActivity extends AppCompatActivity implements EventDetailFragment.OnRemoveFavoriteEventListener{

    /**
     * data holder of saved events
     */
    private List<Event> events;

    /**
     * adapter for saved events
     */
    private EventsAdapter myEventsAdapter;

    /**
     * database of saved events
     */
    private SQLiteDatabase db;

    /**
     * onCreat method
     * @param savedInstanceState saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ticketmaster_saved_events);

        boolean isTablet = findViewById(R.id.fragment_event_details) != null;

        ListView savedEventsLV = findViewById(R.id.savedListView);

        events = new ArrayList<>();
        myEventsAdapter = new EventsAdapter();
        savedEventsLV.setAdapter(myEventsAdapter);

        savedEventsLV.setOnItemClickListener((parent, view, position, id) -> {
            view.setSelected(true);

            Event event = events.get(position);
            // view detail
            Bundle bundle = new Bundle();
            bundle.putBoolean(EventDetailFragment.KEY_IS_TABLET, isTablet);
            bundle.putBoolean(EventDetailFragment.KEY_IS_FAVORITE, true);
            bundle.putLong(EventDetailFragment.KEY_EVENT_ID, event.getId());
            bundle.putString(EventDetailFragment.KEY_EVENT_NAME, event.getName());
            bundle.putString(EventDetailFragment.KEY_EVENT_DATE, event.getDate());
            bundle.putString(EventDetailFragment.KEY_EVENT_MIN_PRICE, event.getMinPrice());
            bundle.putString(EventDetailFragment.KEY_EVENT_MAX_PRICE, event.getMaxPrice());
            bundle.putString(EventDetailFragment.KEY_EVENT_URL, event.getURL());
            bundle.putString(EventDetailFragment.KEY_EVENT_IMAGE, event.getImage());

            if (isTablet) {
                // init fragment
                EventDetailFragment eventDetailFragment = new EventDetailFragment();
                eventDetailFragment.setArguments( bundle );
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_event_details, eventDetailFragment) //Add the fragment in FrameLayout
                        .commit(); //actually load the fragment. Calls onCreate() in DetailFragment

            } else {
                Intent intent = new Intent(TicketMasterSavedEventsActivity.this, TicketMasterEventDetailsActivity.class);
                intent.putExtra(TicketMasterEventDetailsActivity.EVENT_DETAIL, bundle);
                startActivity(intent);
            }
        });

        EventOpenHelper eventDB = new EventOpenHelper(this);
        db = eventDB.getWritableDatabase();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSavedEvent();
    }

    /**
     * method to load saved events
     */
    private void loadSavedEvent() {
        events.clear();
        String[] columns = {Event.COL_ID, Event.COL_NAME, Event.COL_DATE, Event.COL_MINPRICE, Event.COL_MAXPRICE,Event.COL_URL,Event.COL_IMAGE};
        Cursor results = db.query(false, Event.EVENT_SAVED_TABLE, columns,
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
     * method to communicate with other fragment
     * reference to: https://developer.android.com/training/basics/fragments/communicating.html
     * @param fragment the fragment to be communicate with
     */
    @Override
    public void onAttachFragment(Fragment fragment) {
        if (fragment instanceof EventDetailFragment) {
            ((EventDetailFragment)fragment).setCallback(this);
        }
    }


    /**
     * remove event
     * @param eventId id of the event to be removed
     */
    @Override
    public void removeEvent(long eventId) {
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
     * event adapter used by list view to display events
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
            TextView eventNameLV = convertView.findViewById(R.id.event_name_tv);
            TextView eventDateLV = convertView.findViewById(R.id.event_date_tv);

            Event event = getItem(position);
            eventNameLV.setText(String.format(Locale.getDefault(), "%d. %s", position + 1, event.getName()));
            eventDateLV.setText(event.getDate());

            return convertView;
        }
    }
}