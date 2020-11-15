package com.ac.assignment_project_014;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //audioDatabase
        Button btn_audio = findViewById(R.id.btn_audio);
        Intent goToAduioIndex  = new Intent(this, com.ac.assignment_project_014.BaoshanLi.AudioIndexActivity.class);
        btn_audio.setOnClickListener( click -> {

            startActivity( goToAduioIndex );
        });



    }
}