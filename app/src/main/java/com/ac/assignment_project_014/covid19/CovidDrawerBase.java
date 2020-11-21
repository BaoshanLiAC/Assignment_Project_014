package com.ac.assignment_project_014.covid19;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.drawerlayout.widget.DrawerLayout;

import com.ac.assignment_project_014.R;
import com.ac.assignment_project_014.ToolBarBase;
import com.google.android.material.navigation.NavigationView;

import static android.view.View.TEXT_ALIGNMENT_TEXT_START;

/**
 * Abstract base for drawer to be shared across all COVID-19 activities
 */
public abstract class CovidDrawerBase extends ToolBarBase implements NavigationView.OnNavigationItemSelectedListener{
    /**
     *fields
     */
    protected DrawerLayout drawer;
    protected ActionBarDrawerToggle drawerToggle;
    protected NavigationView navigationView;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        //retrieving implemented layout
        setContentView(getLayoutId());
        super.onCreate(savedInstanceState);

        drawer = findViewById(R.id.covid_drawer_layout);
        drawerToggle
                = new ActionBarDrawerToggle(this,drawer,toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(drawerToggle);

        drawerToggle.syncState();

        //initial navigationView
        navigationView = (NavigationView)findViewById(R.id.covid_nav_view);

        //register event handler on menu items
        navigationView.setNavigationItemSelectedListener(menuItem->{
            menuItem.setChecked(true);
            drawer.closeDrawers();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final View view =this.getLayoutInflater().inflate(R.layout.actionbar_dialog,null);
            TextView text = view.findViewById(R.id.action_dialog_text);
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
                            "\n2. User could save result to archive for later analysis." +
                            "\n3. User could navigate to archived tap to view saved record and manipulate data.");
                    text.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
                    builder.setView(view);
                    builder.create().show();
                    break;
                case R.id.covid_nav_about:
                    text.setText("Developed By: Li Sha Wu\nStudent No: **********");
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

    }



    protected boolean stayInSameActivity(int id){
        return getLayoutId() == id;
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        menuItem.setChecked(true);
        drawer.closeDrawers();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View view =this.getLayoutInflater().inflate(R.layout.actionbar_dialog,null);
        TextView text = view.findViewById(R.id.action_dialog_text);
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
                             "\n2. User could save result to archive for later analysis." +
                             "\n3. User could navigate to archived tap to view saved record and manipulate data.");
                text.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
                builder.setView(view);
                builder.create().show();
                break;
            case R.id.covid_nav_about:
                text.setText("Developed By: Li Sha Wu\nStudent No: **********");
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
    }
    protected abstract int getLayoutId();
}
