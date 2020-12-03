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

import androidx.fragment.app.Fragment;

import com.ac.assignment_project_014.R;

import java.util.ArrayList;
import java.util.List;

public class TicketMasterSavedEventsActivity extends TicketMasterDrawerBase implements EventDetailFragment.CallBack{

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
     * onCreate method
     * @param savedInstanceState saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ListView savedEventsLV = findViewById(R.id.savedListView);

        boolean isTablet = findViewById(R.id.fragment_event_details) != null;

        events = new ArrayList<>();
        myEventsAdapter = new EventsAdapter();
        savedEventsLV.setAdapter(myEventsAdapter);

        savedEventsLV.setOnItemClickListener((parent, view, position, id) -> {
            view.setSelected(true);
            Event event = events.get(position);
            Bundle dataToPass = new Bundle();
            dataToPass.putBoolean(TicketMasterMainActivity.IS_TABLET, isTablet);
            dataToPass.putBoolean(TicketMasterMainActivity.IS_FAVORITE, true);
            dataToPass.putLong(TicketMasterMainActivity.EVENT_ID, event.getId());
            dataToPass.putString(TicketMasterMainActivity.EVENT_NAME, event.getName());
            dataToPass.putString(TicketMasterMainActivity.EVENT_DATE, event.getDate());
            dataToPass.putString(TicketMasterMainActivity.EVENT_MIN_PRICE, event.getMinPrice());
            dataToPass.putString(TicketMasterMainActivity.EVENT_MAX_PRICE, event.getMaxPrice());
            dataToPass.putString(TicketMasterMainActivity.EVENT_URL, event.getURL());
            dataToPass.putString(TicketMasterMainActivity.EVENT_IMAGE, event.getImage());

            if (isTablet) {
                EventDetailFragment eventDetailFragment = new EventDetailFragment();
                eventDetailFragment.setArguments( dataToPass );
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_event_details, eventDetailFragment)
                        .commit();

            } else {
                Intent intent = new Intent(TicketMasterSavedEventsActivity.this, TicketMasterEventDetailsActivity.class);
                intent.putExtra("EVENT_DETAIL", dataToPass);
                startActivity(intent);
            }
        });

        EventOpenHelper eventDB = new EventOpenHelper(this);
        db = eventDB.getWritableDatabase();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.ticketmaster_saved_events;
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSavedEvent();
    }

    /**
     * method to load saved events
     */
    public void loadSavedEvent() {
        events.clear();
        String[] columns = {TicketMasterMainActivity.COL_ID, TicketMasterMainActivity.COL_NAME, TicketMasterMainActivity.COL_DATE, TicketMasterMainActivity.COL_MINPRICE, TicketMasterMainActivity.COL_MAXPRICE,TicketMasterMainActivity.COL_URL,TicketMasterMainActivity.COL_IMAGE};
        Cursor results = db.query(false, TicketMasterMainActivity.EVENT_SAVED_TABLE, columns,
                null, null, null, null, null, null);

        while (results.moveToNext()) {
            Event event = new Event();
            event.setId(results.getInt(results.getColumnIndex(TicketMasterMainActivity.COL_ID)));
            event.setName(results.getString(results.getColumnIndex(TicketMasterMainActivity.COL_NAME)));
            event.setDate(results.getString(results.getColumnIndex(TicketMasterMainActivity.COL_DATE)));
            event.setMinPrice(results.getString(results.getColumnIndex(TicketMasterMainActivity.COL_MINPRICE)));
            event.setMaxPrice(results.getString(results.getColumnIndex(TicketMasterMainActivity.COL_MINPRICE)));
            event.setURL(results.getString(results.getColumnIndex(TicketMasterMainActivity.COL_URL)));
            event.setImage(results.getString(results.getColumnIndex(TicketMasterMainActivity.COL_IMAGE)));
            events.add(event);
        }

        myEventsAdapter.notifyDataSetChanged();
    }


    /**
     * communicate with EventDetailFragment by interface
     * reference： https://stackoverflow.com/questions/32346704/how-to-communicate-between-fragments
     * @param fragment
     */
    @Override
    public void onAttachFragment(Fragment fragment) {
        if (fragment instanceof EventDetailFragment) {
            ((EventDetailFragment)fragment).setMyCallBack(this);
        }
    }

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
            LayoutInflater layoutInflater = getLayoutInflater();
            View view = layoutInflater.inflate(R.layout.ticketmaster_event_item, parent, false);
            TextView eventNameLV = view.findViewById(R.id.event_name_tv);
            TextView eventDateLV = view.findViewById(R.id.event_date_tv);

            Event event = getItem(position);
            eventNameLV.setText(event.getName());
            eventDateLV.setText(event.getDate());

            return view;
        }
    }
}