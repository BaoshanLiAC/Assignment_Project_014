package com.ac.assignment_project_014.recipe;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.ac.assignment_project_014.R;
/**
 * The empty class used for tablet display.
 * It Accept the data that was passed from Fragment.
 * @author chunyan ren
 * reference: from InclassProject FragmentExample.java lines 47-54
 */
public class EmptyActivity extends AppCompatActivity {
    /**
     * Initialize the activity and get get the data that was passed from Fragment.
     * @param savedInstanceState used to save the current states
     *
     * @author chunyan ren
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_activity_empty);

        Bundle dataToPass = getIntent().getExtras();
        DetailFragment dFragment = new DetailFragment();
        dFragment.setArguments( dataToPass );
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentLocation, dFragment)
                .commit();
    }
}