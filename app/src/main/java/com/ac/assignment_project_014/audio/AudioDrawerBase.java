package com.ac.assignment_project_014.audio;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.drawerlayout.widget.DrawerLayout;

import com.ac.assignment_project_014.R;
import com.ac.assignment_project_014.ToolBarBase;
import com.google.android.material.navigation.NavigationView;

public abstract class AudioDrawerBase extends ToolBarBase implements NavigationView.OnNavigationItemSelectedListener{
    protected DrawerLayout drawer;
    protected ActionBarDrawerToggle drawerToggle;
    protected NavigationView navigationView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        setContentView(getLayoutId());
        super.onCreate(savedInstanceState);

        drawer = findViewById(R.id.Recipe_drawer_layout);
        drawerToggle
                = new ActionBarDrawerToggle(this,drawer,toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(drawerToggle);

        drawerToggle.syncState();
        navigationView = (NavigationView)findViewById(R.id.recipe_nav_view);


    }

    protected abstract int getLayoutId();

    protected boolean stayInSameActivity(int id){
        return getLayoutId() == id;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        menuItem.setChecked(true);
        drawer.closeDrawers();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View view =this.getLayoutInflater().inflate(R.layout.actionbar_dialog,null);
        TextView text = view.findViewById(R.id.action_dialog_text);
        return true;
    }
}
