package com.ac.assignment_project_014.recipe;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.drawerlayout.widget.DrawerLayout;

import com.ac.assignment_project_014.R;
import com.ac.assignment_project_014.ToolBarBase;
import com.google.android.material.navigation.NavigationView;

import static android.view.View.TEXT_ALIGNMENT_TEXT_START;


/**
 *
 * define the Navigation drawer layout and actions on clicked the buttons.
 * extends from the shared components ToolBarBase.
 *
 * @author chunyan ren
 */
public abstract class RecipeDrawerBase extends ToolBarBase implements NavigationView.OnNavigationItemSelectedListener{

    /**
     * define the drawer;
     */
    protected DrawerLayout drawer;
    /**
     * used to implement NavigationDrawer;
     */
    protected ActionBarDrawerToggle drawerToggle;
    /**
     * to implement the outlook view of NavigationView;
     */
    protected NavigationView navigationView;

    /**
     * Called when initializing the Drawer, and set the response to the button on the drawer
     * @param savedInstanceState is to save the instance state
     */
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
        navigationView.setNavigationItemSelectedListener(menuItem->{
            menuItem.setChecked(true);
            drawer.closeDrawers();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final View view =this.getLayoutInflater().inflate(R.layout.actionbar_dialog,null);
            TextView text = view.findViewById(R.id.action_dialog_text);
            switch (menuItem.getItemId()){

                case R.id.recipe_goto_search:
                    startActivity(new Intent(this, RecipeSearchHistoryActivity.class));
                    break;
                case R.id.recipe_help:
                    text.setText(R.string.recipe_help);
                    text.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
                    builder.setView(view);
                    builder.create().show();
                    break;
                case R.id.recipe_about:
                    text.setText(R.string.recipe_about);
                    text.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
                    builder.setView(view);
                    builder.create().show();
                    break;
                case R.id.recipe_version:
                    text.setText(R.string.recipe_version);
                    text.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
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
     * @param menuItem this menu is displayed on the navigation drawer
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        menuItem.setChecked(true);
        drawer.closeDrawers();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View view =this.getLayoutInflater().inflate(R.layout.actionbar_dialog,null);
        TextView text = view.findViewById(R.id.action_dialog_text);
        switch (menuItem.getItemId()){
            case R.id.recipe_goto_search:
                startActivity(new Intent(this, RecipeSearchHistoryActivity.class));
                break;
            case R.id.recipe_help:
                text.setText("1. User could query date by input the ingredients." +
                             "\n2. User could view the top 10 search results retrived from  http://www.recipepuppy.com."+
                             "\n3. User could save the ingredients they like and to see it next time without access online data.");
                text.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
                builder.setView(view);
                builder.create().show();
                break;
            case R.id.recipe_about:
                text.setText("Developed By: Chunyan Ren\nStudent No: 040980795");
                text.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
                builder.setView(view);
                builder.create().show();
                break;
            case R.id.recipe_version:
                text.setText("The current Version is V1.0.0");
                text.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
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
