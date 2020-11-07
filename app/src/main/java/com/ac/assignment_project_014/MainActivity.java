package com.ac.assignment_project_014;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.ac.assignment_project_014.recipe.RecipeMainActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_recipe = findViewById(R.id.btn_recipe);
        btn_recipe.setOnClickListener(click -> {startActivity( new Intent(this, RecipeMainActivity.class) );});
    }
}