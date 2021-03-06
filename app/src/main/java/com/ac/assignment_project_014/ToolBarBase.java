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
import com.ac.assignment_project_014.TicketMaster.TicketMasterMainActivity;
import com.ac.assignment_project_014.audio.AudioIndexActivity;
import com.ac.assignment_project_014.covid19.Covid19CaseDataMainActivity;
import com.ac.assignment_project_014.recipe.RecipeMainActivity;


/**
 * Abstract base for toolbar to be shared across all activities in Group Project.
 *
 */
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

        final View view =this.getLayoutInflater().inflate(R.layout.actionbar_dialog,null);
        TextView text = view.findViewById(R.id.action_dialog_text);
        switch(mi.getItemId()){
            case R.id.audio:
                builder.setTitle(R.string.button_title)
                        .setMessage(getString(R.string.audio_button_meaasge))
                        .setPositiveButton(R.string.button_yes, (click, arg) -> {
                            startActivity(new Intent(this, AudioIndexActivity.class)); })
                        .setNegativeButton(R.string.button_back,(click, arg) -> {; })
                        .create().show();
                break;
            case R.id.covid:
                builder.setTitle(R.string.button_title)
                        .setMessage(R.string.Covid19_button_meaasge)
                        .setPositiveButton(R.string.button_yes, (click, arg) -> {
                            startActivity(new Intent(this, Covid19CaseDataMainActivity.class)); })
                        .setNegativeButton(R.string.button_back,(click, arg) -> {; })
                        .create().show();
                break;
            case R.id.recipes:
                builder.setTitle(R.string.button_title)
                        .setMessage(R.string.Recipe_button_meaasge)
                        .setPositiveButton(R.string.button_yes, (click, arg) -> {
                            startActivity(new Intent(this, RecipeMainActivity.class)); })
                        .setNegativeButton(R.string.button_back,(click, arg) -> {; })
                        .create().show();
                break;
            case R.id.ticket:
                builder.setTitle(R.string.button_title)
                        .setMessage(R.string.ticket_button_meaasge)
                        .setPositiveButton(R.string.button_yes, (click, arg) -> {
                            startActivity(new Intent(this, TicketMasterMainActivity.class)); })
                        .setNegativeButton(R.string.button_back,(click, arg) -> {; })
                        .create().show();
                break;
            default:
                break;
        }
        return true;
    }
}
