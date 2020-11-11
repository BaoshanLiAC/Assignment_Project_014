package com.ac.assignment_project_014.ticketmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.ac.assignment_project_014.R;

public class TicketMasterMainActivity extends AppCompatActivity {

    SharedPreferences prefsCity = null;
    SharedPreferences prefsRadius = null;
    EditText cityNameText = null;
    EditText radiusText = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ticketmaster_activity_main);

        prefsCity = getSharedPreferences("city", Context.MODE_PRIVATE);
        prefsRadius = getSharedPreferences("radius", Context.MODE_PRIVATE);

        String citySearch = prefsCity.getString("cityName","");
        cityNameText = findViewById(R.id.cityName);
        cityNameText.setText(citySearch);

        String radiusSearch = prefsCity.getString("radius","");
        radiusText = findViewById(R.id.radius);
        radiusText.setText(radiusSearch);

        Button searchBtn = findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(e->
                {
                    Intent goToEventList = new Intent(TicketMasterMainActivity.this, TicketMasterEventListActivity.class);
                    goToEventList.putExtra("CITY", cityNameText.getText().toString());
                    goToEventList.putExtra("RADIUS", radiusText.getText().toString());
                    startActivity(goToEventList);
                }
        );


        Button savedEventsBtn = findViewById(R.id.savedEventsBtn);
        savedEventsBtn.setOnClickListener(e->
                {
                    Intent goToSavedEventList = new Intent(TicketMasterMainActivity.this, TicketMasterSavedEventsActivity.class);
                    startActivity(goToSavedEventList);
                }
        );

    }
    @Override
    protected void onPause() {
        super.onPause();
        saveSharedPrefs(cityNameText.getText().toString());
        saveSharedPrefs(radiusText.getText().toString());
    }

    private void saveSharedPrefs(String stringToSave) {
        SharedPreferences.Editor editorCity = prefsCity.edit();
        editorCity.putString("citySaved", stringToSave);
        editorCity.commit();

        SharedPreferences.Editor editorRadius = prefsCity.edit();
        editorRadius.putString("radiusSaved", stringToSave);
        editorRadius.commit();
    }


}