package com.ac.assignment_project_014.covid19;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

<<<<<<< HEAD

=======
>>>>>>> d8e0c1553fa43925d70b8c3d89b278b74041bfc7
import com.ac.assignment_project_014.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

<<<<<<< HEAD
public class Covid19SearchResultActivity extends DrawerBase {
=======
public class Covid19SearchResultActivity extends CovidDrawerBase {
>>>>>>> d8e0c1553fa43925d70b8c3d89b278b74041bfc7
    protected ArrayList<Covid19ProvinceData>  dataList;
    protected Covid19CountryData result;
    protected ListView listView;
    protected Button save,back;
    protected Covid19DateHelper data;
    protected SQLiteDatabase db;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {

<<<<<<< HEAD

        super.onCreate(savedInstanceState);

        //received data from query
        result = (Covid19CountryData) getIntent().getSerializableExtra("search_result");
        if(result == null){
            Toast.makeText(this, "null data", Toast.LENGTH_LONG).show();
            dataList = new ArrayList<>();
        }else{
            //initial list view data
            dataList = result.getDataList();
            //populate country name & search date & total cases
            TextView.class.cast(findViewById(R.id.covid_result_country)).setText(result.getCountryName());
            TextView.class.cast(findViewById(R.id.covid_result_description)).setText(result.toString());
        }

        //populate country name & search date & total cases
//        TextView.class.cast(findViewById(R.id.covid_result_country)).setText(result.getCountryName());
//        TextView.class.cast(findViewById(R.id.covid_result_description)).setText(result.toString());
=======
        super.onCreate(savedInstanceState);
        //received data from query
        result = (Covid19CountryData) getIntent().getSerializableExtra("search_result");
        //initial list view data
        dataList = result.getDataList();
        //populate country name & search date & total cases
        TextView.class.cast(findViewById(R.id.covid_result_country)).setText(result.getCountryName());
        TextView.class.cast(findViewById(R.id.covid_result_description)).setText(result.toString());
>>>>>>> d8e0c1553fa43925d70b8c3d89b278b74041bfc7


        //initial list view
        final ProvinceList countryDataAdapter = new ProvinceList(this);
        listView = findViewById(R.id.covid_search_result_listView);
        listView.setAdapter(countryDataAdapter);
        countryDataAdapter.notifyDataSetChanged();

        //initial database
        data = new Covid19DateHelper(this);
        db = data.getWritableDatabase();
<<<<<<< HEAD

=======
>>>>>>> d8e0c1553fa43925d70b8c3d89b278b74041bfc7
        //register button event handlers.
        save = findViewById(R.id.covid_result_archive_btn);
        back = findViewById(R.id.covid_result_back_to_search_btn);
        save.setOnClickListener(e->saveItemToDataBase());
        back.setOnClickListener(e->finish());
    }



    @Override
    protected int getLayoutId() {
        return R.layout.covid19_activity_search_result;
    }


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



}
