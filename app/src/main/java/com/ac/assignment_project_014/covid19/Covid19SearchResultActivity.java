package com.ac.assignment_project_014.covid19;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import com.ac.assignment_project_014.MainActivity;
import com.ac.assignment_project_014.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class Covid19SearchResultActivity extends AppCompatActivity {
    protected ArrayList<Covid19ProvinceData> CountryData;
    protected ListView listView;

    //inner class
    private class ProvinceList extends ArrayAdapter<Covid19ProvinceData>{

        public ProvinceList(@NonNull Context context) {
            super(context,0);
        }
        public Covid19ProvinceData getItem(int position){
            return CountryData.get(position);
        }
        @Override
        public int getCount(){
            return CountryData.size();
        }
        @Override
        public int getPosition(@Nullable Covid19ProvinceData item) {
            return super.getPosition(item);
        }


        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = Covid19SearchResultActivity.this.getLayoutInflater();
            View result = inflater.inflate(R.layout.covid19_province_data_item, null);
            Covid19ProvinceData item = getItem(position);
            TextView name = result.findViewById(R.id.covid19_province_data_item_province_name);
            TextView number = result.findViewById(R.id.covid19_province_data_item_province_case);
            name.setText(item.getName());
            number.setText(String.valueOf(item.getCaseNumber()));
            return result;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.covid19_activity_search_result);
        setSupportActionBar(findViewById(R.id.covid19_toolbar));
        CountryData = (ArrayList<Covid19ProvinceData>) getIntent().getSerializableExtra("search_result");

        final ProvinceList countryDataAdapter = new ProvinceList(this);
        listView = findViewById(R.id.covid_search_result_listView);
        listView.setAdapter(countryDataAdapter);
        countryDataAdapter.notifyDataSetChanged();
    }



    /**
     * TOOL BAR
     *
     */
    public boolean onCreateOptionsMenu(Menu m){

        getMenuInflater().inflate(R.menu.covid19_toolbar_menu, m );
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem mi){
        switch(mi.getItemId()){
            case R.id.covid19_action_home:
                Snackbar.make(findViewById(R.id.covid19_toolbar), "Back to Home Page", Snackbar.LENGTH_LONG)
                        .setAction("Action", e->startActivity(new Intent(this, MainActivity.class))).show();
                break;
            case R.id.covid19_action_search:
                Snackbar.make(findViewById(R.id.covid19_toolbar), "Back to Home Page", Snackbar.LENGTH_LONG)
                        .setAction("Action", e->startActivity(new Intent(this, Covid19CaseDataMainActivity.class))).show();
                break;
            case R.id.covid19_action_archive:
                Snackbar.make(findViewById(R.id.covid19_toolbar), "Jump to Archived Case Data", Snackbar.LENGTH_LONG)
                        .setAction("Action", e->startActivity(new Intent(this, MainActivity.class))).show();
                break;
            case R.id.covid19_action_about:
                break;
        }
        return true;
    }





}
