package com.ac.assignment_project_014.TicketMaster;

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