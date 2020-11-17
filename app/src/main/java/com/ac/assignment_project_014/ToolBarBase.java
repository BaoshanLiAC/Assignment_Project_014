package com.ac.assignment_project_014;

import android.content.Intent;
<<<<<<< HEAD

=======
>>>>>>> d8e0c1553fa43925d70b8c3d89b278b74041bfc7
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
<<<<<<< HEAD


import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
=======
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
>>>>>>> d8e0c1553fa43925d70b8c3d89b278b74041bfc7
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

<<<<<<< HEAD


import com.ac.assignment_project_014.covid19.Covid19CaseDataMainActivity;
import com.ac.assignment_project_014.recipe.RecipeMainActivity;

import com.google.android.material.snackbar.Snackbar;

import static android.view.View.TEXT_ALIGNMENT_TEXT_START;

public abstract class ToolBarBase extends AppCompatActivity {
    protected Toolbar toolbar;


=======
import com.ac.assignment_project_014.covid19.Covid19CaseDataMainActivity;
import com.ac.assignment_project_014.recipe.RecipeMainActivity;

public abstract class ToolBarBase extends AppCompatActivity {
    protected Toolbar toolbar;

>>>>>>> d8e0c1553fa43925d70b8c3d89b278b74041bfc7
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar = (Toolbar)findViewById(R.id.shared_action_bar);
        setSupportActionBar(toolbar);
<<<<<<< HEAD

=======
>>>>>>> d8e0c1553fa43925d70b8c3d89b278b74041bfc7
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.shared_toolbar_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem mi){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
<<<<<<< HEAD
//        final View view =this.getLayoutInflater().inflate(R.layout.covid19_actionbar_dialog,null);
//        TextView text = view.findViewById(R.id.covid19_action_dialog_text);
        switch(mi.getItemId()){
            case R.id.audio:
                //ToDo: change Acitivity class
                Snackbar.make(findViewById(R.id.shared_action_bar), "Go to Audio Activity.", Snackbar.LENGTH_LONG)
                        .setAction("GO", e->startActivity(new Intent(this, RecipeMainActivity.class))).show();
                break;
            case R.id.covid:
                Snackbar.make(findViewById(R.id.shared_action_bar), "Go to COVID-19 Activity.", Snackbar.LENGTH_LONG)
                        .setAction("GO", e->startActivity(new Intent(this, Covid19CaseDataMainActivity.class))).show();


                break;
            case R.id.recipes:
                Snackbar.make(findViewById(R.id.shared_action_bar), "Go to Recipe Activity.", Snackbar.LENGTH_LONG)
                        .setAction("GO", e->startActivity(new Intent(this, RecipeMainActivity.class))).show();
                break;
            case R.id.ticket:
                //ToDo: change Acitivity class
                Snackbar.make(findViewById(R.id.shared_action_bar), "Go to Ticket Master Activity.", Snackbar.LENGTH_LONG)
                        .setAction("GO", e->startActivity(new Intent(this, RecipeMainActivity.class))).show();
                break;
=======
        final View view =this.getLayoutInflater().inflate(R.layout.actionbar_dialog,null);
        TextView text = view.findViewById(R.id.action_dialog_text);
        switch(mi.getItemId()){
            case R.id.audio:
                builder.setTitle("Switch Channel")
                        .setMessage("You are going to Audio channel")
                        .setPositiveButton("Yes", (click, arg) -> {
                            startActivity(new Intent(this, RecipeMainActivity.class)); })
                        .setNegativeButton("Go Back",(click, arg) -> {; })
                        .create().show();
                break;
            case R.id.covid:
                builder.setTitle("Switch Channel")
                        .setMessage("You are going to Covid19 Channel")
                        .setPositiveButton("YES", (click, arg) -> {
                            startActivity(new Intent(this, Covid19CaseDataMainActivity.class)); })
                        .setNegativeButton("Go Back",(click, arg) -> {; })
                        .create().show();
                break;
            case R.id.recipes:
                builder.setTitle("Switch Channel")
                        .setMessage("You are going to Recipe channel")
                        .setPositiveButton("Yes", (click, arg) -> {
                            startActivity(new Intent(this, RecipeMainActivity.class)); })
                        .setNegativeButton("Go Back",(click, arg) -> {; })
                        .create().show();
                break;
            case R.id.ticket:
                builder.setTitle("Switch Channel")
                        .setMessage("You are going to ticket channel")
                        .setPositiveButton("Yes", (click, arg) -> {
                            startActivity(new Intent(this, RecipeMainActivity.class)); })
                        .setNegativeButton("Go Back",(click, arg) -> {; })
                        .create().show();
                break;

>>>>>>> d8e0c1553fa43925d70b8c3d89b278b74041bfc7
            default:
                break;
        }
        return true;
    }
}
