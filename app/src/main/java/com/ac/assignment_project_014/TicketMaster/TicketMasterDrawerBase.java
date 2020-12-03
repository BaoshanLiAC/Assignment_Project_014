package com.ac.assignment_project_014.TicketMaster;

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
 *subclass of toolBarBase, to implement the toolbar and drawer function
 */
public abstract class TicketMasterDrawerBase extends ToolBarBase implements NavigationView.OnNavigationItemSelectedListener{

    protected DrawerLayout drawer;
    protected ActionBarDrawerToggle drawerToggle;
    protected NavigationView navigationView;

    /**
     * Called when initializing the Drawer, and set the response to the button on the drawer
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(getLayoutId());
        super.onCreate(savedInstanceState);

        drawer = findViewById(R.id.ticketmaster_drawer_layout);
        drawerToggle
                = new ActionBarDrawerToggle(this,drawer,toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(drawerToggle);

        drawerToggle.syncState();
        navigationView = (NavigationView)findViewById(R.id.ticketmaster_navigation_view);
        navigationView.setNavigationItemSelectedListener(menuItem->{
            menuItem.setChecked(true);
            drawer.closeDrawers();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final View view =this.getLayoutInflater().inflate(R.layout.actionbar_dialog,null);
            TextView text = view.findViewById(R.id.action_dialog_text);
            switch (menuItem.getItemId()){
                case R.id.ticketmaster_goto_search:
                    startActivity(new Intent(this, TicketMasterMainActivity.class));
                    Toast.makeText(this,R.string.ticketmaster_go_to_search, Toast.LENGTH_LONG).show();
                    break;
                case R.id.ticketmaster_favourite_list:
                    startActivity(new Intent(this, TicketMasterSavedEventsActivity.class));
                    Toast.makeText(this,R.string.ticketmaster_go_to_saved, Toast.LENGTH_LONG).show();
                    break;
                case R.id.ticketmaster_help:
                    text.setText(R.string.ticketmaster_help_infor);
                    text.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
                    builder.setView(view);
                    builder.create().show();
                    break;
                case R.id.ticketmaster_about:
                    text.setText(R.string.ticketmaster_about);
                    text.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
                    builder.setView(view);
                    builder.create().show();
                    break;
                case R.id.ticketmaster_version:
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

    /**
     * set the response to the button on the drawer
     * @param menuItem
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        menuItem.setChecked(true);
        drawer.closeDrawers();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View view =this.getLayoutInflater().inflate(R.layout.actionbar_dialog,null);
        TextView text = view.findViewById(R.id.action_dialog_text);
        switch (menuItem.getItemId()){
            case R.id.ticketmaster_goto_search:
                startActivity(new Intent(this, TicketMasterMainActivity.class));
                Toast.makeText(this,R.string.ticketmaster_go_to_search, Toast.LENGTH_LONG).show();
                break;
            case R.id.ticketmaster_favourite_list:
                startActivity(new Intent(this, TicketMasterSavedEventsActivity.class));
                Toast.makeText(this,R.string.ticketmaster_go_to_saved, Toast.LENGTH_LONG).show();
                break;
            case R.id.ticketmaster_help:
                text.setText(R.string.ticketmaster_help_infor);
                text.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
                builder.setView(view);
                builder.create().show();
                break;
            case R.id.ticketmaster_about:
                text.setText(R.string.ticketmaster_about);
                text.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
                builder.setView(view);
                builder.create().show();
                break;
            case R.id.ticketmaster_version:
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
