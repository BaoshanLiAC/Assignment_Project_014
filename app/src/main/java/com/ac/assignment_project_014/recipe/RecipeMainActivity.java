package com.ac.assignment_project_014.recipe;

import android.os.Bundle;

import com.ac.assignment_project_014.R;

public class RecipeMainActivity extends RecipeDrawerBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.recipe_activity_main;
    }
}