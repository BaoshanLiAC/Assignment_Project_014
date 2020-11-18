package com.ac.assignment_project_014.TicketMaster;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.ac.assignment_project_014.R;

/**
 * this class shows the details of an event
 */
public class TicketMasterEventDetailsActivity extends AppCompatActivity {

    public static final String EVENT_DETAIL = "EVENT_DETAIL";

    /**
     * onCreate method
     * @param savedInstanceState saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ticketmaster_event_details);
        setTitle(getString(R.string.ticketmaster_details_title));


        Bundle msgInfo = getIntent().getBundleExtra(EVENT_DETAIL);

        EventDetailFragment eventDetailFragment = new EventDetailFragment();
        eventDetailFragment.setArguments( msgInfo );
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_event_details, eventDetailFragment)
                .commit();
    }

}