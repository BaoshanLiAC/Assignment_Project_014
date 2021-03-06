package com.ac.assignment_project_014.covid19;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.ac.assignment_project_014.R;
import com.ac.assignment_project_014.recipe.EmptyActivity;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Activity for: 1. display search result for a single country come up with province list regarding their COVID-19 data
 *               2. offering functionality to save data into SQLite db for later use.
 *               3. UI controls to sort data by column in the list view.
 *
 */
public class Covid19SearchResultActivity extends Covid19DrawerBase {
    /***UI controls **/
    private Button save,chart, back, province_sort, confirmed_sort,daily_sort;
    /***flag: help variables***/
    private boolean flag1 = true, flag2 = true, flag3 = true;
    /***province list of a country that pass by result***/
    protected ArrayList<Covid19ProvinceData>  dataList;
    /***search result pass by main activity***/
    protected Covid19CountryData result;
    /***UI component as list container***/
    protected ListView listView;
    /***implemented instance of SQLightOpenHelper***/
    protected Covid19DateHelper data;
    /**Local db***/
    protected SQLiteDatabase db;

    protected boolean isTablet;




    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        //identify device
        isTablet = findViewById(R.id.covid19_fragmentLocation) != null;
        //received data from query
        result = (Covid19CountryData) getIntent().getSerializableExtra("search_result");
        //initial list view data
        if(result != null) {
            dataList = result.getDataList();
            //populate country name & search date & total cases
            TextView.class.cast(findViewById(R.id.covid_result_country)).setText(result.getCountryName());
            TextView.class.cast(findViewById(R.id.covid_result_description)).setText(result.toString());
            //initial list view
            final ProvinceList countryDataAdapter = new ProvinceList(this);
            listView = findViewById(R.id.covid_search_result_listView);
            listView.setAdapter(countryDataAdapter);
            countryDataAdapter.notifyDataSetChanged();

            //initial database
            data = new Covid19DateHelper(this);
            db = data.getWritableDatabase();

            //register button event handlers.
            save = findViewById(R.id.covid_result_archive_btn);
            chart = findViewById(R.id.covid_result_chart_btn);
            back = findViewById(R.id.covid_result_back_to_search_btn);

            save.setOnClickListener(e -> saveItemToDataBase());
            chart.setOnClickListener(e->{
                if(result == null){
                    Toast.makeText(this,"No data could be show in chart", Toast.LENGTH_LONG);
                }else {
                    Bundle dataToPass = new Bundle();
                    dataToPass.putSerializable("country-data", result);
                    if (isTablet) {
                        //change layout dynamics
                        LinearLayout left = findViewById(R.id.covid19_result_top);
                        ViewGroup.LayoutParams para1 = left.getLayoutParams();
                        RelativeLayout.LayoutParams para2 = new RelativeLayout.LayoutParams(-1, 600);

                        left.setLayoutParams(para2);
                        Covid19ChartFragment chartFragment = new Covid19ChartFragment(left);
                        chartFragment.setTablet(isTablet);
                        chartFragment.setLayOutParameter(para1);
                        chartFragment.setArguments(dataToPass);
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.covid19_fragmentLocation, chartFragment) //Add the fragment in FrameLayout
                                .commit(); //actually load the fragment. Calls onCreate() in DetailFragment
                    }
                    else{
                        Intent nextActivity = new Intent(this, Covid19EmptyActivity.class);
                        nextActivity.putExtras(dataToPass); //send data to next activity
                        startActivity(nextActivity); //make the transition
                    }
                }
            });
            back.setOnClickListener(e -> startActivity(new Intent(this, Covid19CaseDataMainActivity.class)));

            sortList(countryDataAdapter);
        }
        else{

            //populate country name & search date & total cases
            TextView.class.cast(findViewById(R.id.covid_result_country)).setText("Not Found.");
            TextView.class.cast(findViewById(R.id.covid_result_description)).setText("Please go back to reach another country.");
        }





    }



    @Override
    protected int getLayoutId() {
        return R.layout.covid19_activity_search_result;
    }

    /**
     * Insertion:  applied Duplicate Prevention
     */
    private void saveItemToDataBase(){

        //prevent duplication

        final String whereClause = data.KEY_COUNTRY + "=? AND " + data.KEY_QUERY_DATE + "=?";
        final String[] args = {result.getCountryName(),result.getSearchDateTime()};
        String [] columns = {data.KEY_ID, data.KEY_COUNTRY, data.KEY_TOTAL,data.KEY_DAILY,data.KEY_QUERY_DATE,data.KEY_DATA};
        Cursor cursor = db.query(false, data.TABLE_NAME, columns, whereClause, args, null, null, null, null);
        if(cursor.getCount() > 0){
            Toast.makeText(this,"Data has already archived", Toast.LENGTH_LONG).show();
        }
        else{
            ContentValues newRowValues = new ContentValues();
            //Now provide a value for every database column defined in MyOpener.java:
            //put string name in the NAME column:

            newRowValues.put(Covid19DateHelper.KEY_COUNTRY, result.getCountryName());
            newRowValues.put(Covid19DateHelper.KEY_QUERY_DATE, result.getSearchDateTime());
            newRowValues.put(Covid19DateHelper.KEY_TOTAL, result.getTotalCase());
            newRowValues.put(Covid19DateHelper.KEY_DAILY, result.getDailyIncrease());
            newRowValues.put(Covid19DateHelper.KEY_DATA, Covid19Util.createByteArray(result.getDataList()));
            long id = db.insert(Covid19DateHelper.TABLE_NAME,null, newRowValues);
            if(id > 0){
                Toast.makeText(this,"Data has been archived.", Toast.LENGTH_LONG).show();
            }
            else{
                Snackbar.make(findViewById(R.id.covid_search_result_listView), "Data archiving failed. Please try again later.", Snackbar.LENGTH_LONG).show();
            }

        }

    }

    /**
     * LIST VIEW Sort Method
     * @param countryDataAdapter
     */
    private void sortList(ProvinceList countryDataAdapter){
        province_sort = findViewById(R.id.covid_result_province_btn);
        province_sort.setOnClickListener(e->{
            if(flag1){
                Collections.sort(dataList, (a,b)->a.getName().compareTo(b.getName()));
            }
            else{
                Collections.sort(dataList, (a,b)->b.getName().compareTo(a.getName()));
            }
            flag1 = !flag1;
            countryDataAdapter.notifyDataSetChanged();
        });

        confirmed_sort = findViewById(R.id.covid_result_confirmed_btn);

        confirmed_sort.setOnClickListener(e->{
            if(flag2){
                Collections.sort(dataList, (a,b)->a.getCaseNumber()-b.getCaseNumber());
            }
            else{
                Collections.sort(dataList, (a,b)->b.getCaseNumber()-a.getCaseNumber());
            }
            flag2 = !flag2;
            countryDataAdapter.notifyDataSetChanged();
        });
        daily_sort = findViewById(R.id.covid_result_daily_btn);
        daily_sort.setOnClickListener(e->{
            if(flag3){
                Collections.sort(dataList, (a,b)->a.getIncrease()-b.getIncrease());
            }
            else{
                Collections.sort(dataList, (a,b)->b.getIncrease()-a.getIncrease());
            }
            flag3 = !flag3;
            countryDataAdapter.notifyDataSetChanged();
        });

    }

    //inner class
    private class ProvinceList extends ArrayAdapter<Covid19ProvinceData>{

        public ProvinceList(@NonNull Context context) {
            super(context,0);
        }
        public Covid19ProvinceData getItem(int position){
            return dataList.get(position);
        }
        @Override
        public int getCount(){
            return dataList.size();
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
            TextView increase = result.findViewById(R.id.covid19_province_data_item_province_change);
            name.setText(item.getName());
            int cases = item.getCaseNumber();
            number.setText(String.valueOf(cases));
            if(cases > 10000){
                number.setTextColor(Color.argb(255,193,44,27));
            }
            else if(cases > 1000){
                number.setTextColor(Color.argb(255,224,145,27));
            }
            else{
                number.setTextColor(Color.argb(255,20,151,20));
            }
            int inc = item.getIncrease();
            increase.setText(String.valueOf(inc));
            if(inc > 100){
                increase.setTextColor(Color.argb(255,193,44,27));
            }
            else if(inc > 10){
                increase.setTextColor(Color.argb(255,224,145,27));
            }
            else{
                increase.setTextColor(Color.argb(255,20,151,20));
            }


            return result;
        }
    }
}
