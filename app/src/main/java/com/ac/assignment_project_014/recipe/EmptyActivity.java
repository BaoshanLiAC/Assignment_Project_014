package com.ac.assignment_project_014.recipe;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.ac.assignment_project_014.R;

public class EmptyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_activity_empty);

        Bundle dataToPass = getIntent().getExtras(); //get the data that was passed from FragmentExample
        //This is copied directly from FragmentExample.java lines 47-54
        DetailFragment dFragment = new DetailFragment();
        dFragment.setArguments( dataToPass ); //pass data to the the fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentLocation, dFragment)
                .commit();
    }
}