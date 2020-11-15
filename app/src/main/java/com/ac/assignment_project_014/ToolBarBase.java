package com.ac.assignment_project_014;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ac.assignment_project_014.covid19.Covid19CaseDataMainActivity;
import com.ac.assignment_project_014.recipe.RecipeMainActivity;
import com.google.android.material.snackbar.Snackbar;

public abstract class ToolBarBase extends AppCompatActivity {
    protected Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar = (Toolbar)findViewById(R.id.shared_action_bar);
        setSupportActionBar(toolbar);
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
        final View view =this.getLayoutInflater().inflate(R.layout.covid19_actionbar_dialog,null);
        TextView text = view.findViewById(R.id.covid19_action_dialog_text);
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
            default:
                break;
        }
        return true;
    }
}
