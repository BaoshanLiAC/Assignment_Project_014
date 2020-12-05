package com.ac.assignment_project_014.covid19;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.ac.assignment_project_014.R;
import com.ac.assignment_project_014.recipe.DetailFragment;

/**
 * link Fragment activity within an empty activity to handle phone device.
 */
public class Covid19EmptyActivity extends Covid19DrawerBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Bundle dataToPass = getIntent().getExtras(); //get the data that was passed from FragmentExample
        //This is copied directly from FragmentExample.java lines 47-54
        Covid19ChartFragment covidFragment = new Covid19ChartFragment();
        covidFragment.setArguments( dataToPass ); //pass data to the the fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.covid19_fragmentLocation, covidFragment)
                .commit();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.covid19_activity_empty;
    }
}
