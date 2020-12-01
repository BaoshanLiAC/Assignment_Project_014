package com.ac.assignment_project_014;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;


import androidx.appcompat.app.AppCompatActivity;

import com.ac.assignment_project_014.TicketMaster.TicketMasterMainActivity;

import com.ac.assignment_project_014.covid19.Covid19CaseDataMainActivity;
import com.ac.assignment_project_014.recipe.RecipeMainActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton btn_recipe = findViewById(R.id.btn_recipe);
        btn_recipe.setOnClickListener(click -> {startActivity( new Intent(this, RecipeMainActivity.class) );});

        ImageButton btn_covid19 = findViewById(R.id.btn_covid19);
        btn_covid19.setOnClickListener(click -> {startActivity( new Intent(this, Covid19CaseDataMainActivity.class) );});


        ImageButton btn_audio = findViewById(R.id.btn_audio);
        btn_audio.setOnClickListener(click -> {startActivity( new Intent(this, com.ac.assignment_project_014.audio.AudioIndexActivity.class) );});


        ImageButton btn_ticketmaster = findViewById(R.id.btn_ticketmaster);
        btn_ticketmaster.setOnClickListener(click -> {startActivity( new Intent(this, TicketMasterMainActivity.class) );});
    }
}