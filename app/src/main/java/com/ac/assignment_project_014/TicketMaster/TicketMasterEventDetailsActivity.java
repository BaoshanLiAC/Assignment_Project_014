package com.ac.assignment_project_014.TicketMaster;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ac.assignment_project_014.R;

public class TicketMasterEventDetailsActivity extends AppCompatActivity {

    public static final String EVENT_DETAIL = "EVENT_DETAIL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ticketmaster_event_details);
        setTitle(getString(R.string.ticketmaster_details_title));

        //toolbar
        Toolbar toolBar = findViewById(R.id.ticketmaster_toolbar);
        //setSupportActionBar(toolBar);

        Bundle msgInfo = getIntent().getBundleExtra(EVENT_DETAIL);

        EventDetailFragment eventDetailFragment = new EventDetailFragment();
        eventDetailFragment.setArguments( msgInfo );
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_event_details, eventDetailFragment) //Add the fragment in FrameLayout
                .commit(); //actually load the fragment. Calls onCreate() in DetailFragment
    }

}