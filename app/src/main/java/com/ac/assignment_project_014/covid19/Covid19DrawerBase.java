package com.ac.assignment_project_014.covid19;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.drawerlayout.widget.DrawerLayout;

import com.ac.assignment_project_014.ToolBarBase;
import com.ac.assignment_project_014.R;
import com.google.android.material.navigation.NavigationView;

import static android.view.View.TEXT_ALIGNMENT_TEXT_START;

public abstract class Covid19DrawerBase extends ToolBarBase{

    protected DrawerLayout drawer;
    protected ActionBarDrawerToggle drawerToggle;
    protected NavigationView navigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(getLayoutId());
        super.onCreate(savedInstanceState);

        drawer = findViewById(R.id.covid_drawer_layout);
        drawerToggle
                = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(drawerToggle);

        drawerToggle.syncState();
        navigationView = (NavigationView)findViewById(R.id.covid_nav_view);
        navigationView.setNavigationItemSelectedListener(menuItem->{
            menuItem.setChecked(true);
            drawer.closeDrawers();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final View view =this.getLayoutInflater().inflate(R.layout.covid19_actionbar_dialog,null);
            TextView text = view.findViewById(R.id.covid19_action_dialog_text);
            switch (menuItem.getItemId()){
                case R.id.covid_nav_search:
                    startActivity(new Intent(this, Covid19CaseDataMainActivity.class));
                    Toast.makeText(this,"Go search by country and date.", Toast.LENGTH_LONG).show();
                    break;
                case R.id.covid_nav_review:
                    startActivity(new Intent(this, Covid19SearchResultActivity.class));
                    Toast.makeText(this,"Go review last search", Toast.LENGTH_LONG).show();
                    break;
                case R.id.covid_nav_archive:
                    startActivity(new Intent(this, Covid19ArchivedActivity.class));
                    Toast.makeText(this,"Go review archived data", Toast.LENGTH_LONG).show();
                    break;
                case R.id.covid_nav_help:
                    text.setText("1. Query date from https://api.covid19api.com/country by input country and date." +
                            "\n2. User could save result to archive for later analysis. A fragment handles chart displaying" +
                            "\n3. User could navigate to archived tap to view saved record and manipulate data.");
                    text.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
                    builder.setView(view);
                    builder.create().show();

                    break;
                case R.id.covid_nav_about:
                    text.setText("Developed By: Li Sha Wu\nStudent No: 040980947");
                    text.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
                    builder.setView(view);
                    builder.create().show();
                    break;
                case R.id.covid_nav_version:
                    builder.setView(view);
                    builder.create().show();
                    break;
                default:
                    break;

            }


            return true;
        });
        toolbar.setBackgroundColor(Color.parseColor("#FFFF6D00"));
    }


    protected abstract int getLayoutId();
}
