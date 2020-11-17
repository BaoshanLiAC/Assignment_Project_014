package com.ac.assignment_project_014.covid19;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;




import com.ac.assignment_project_014.R;

import java.util.ArrayList;


public class Covid19ArchivedActivity extends CovidDrawerBase {

    protected ArrayList<Covid19CountryData> datalist = new ArrayList<>();
    protected ListView listView;
    protected CountryList dataAdapter;
    protected Covid19DateHelper data;
    protected SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);


        //initial list view
        dataAdapter = new CountryList(this);
        listView = findViewById(R.id.covid_archived_listView);
        listView.setAdapter(dataAdapter);
        //load data from db to initial datalist;
        loadData();

        listView.setOnItemClickListener(( parent, view, position, id)->{
            Toast.makeText(listView.getContext(), "Open detail for selected data.", Toast.LENGTH_LONG).show();
            Covid19CountryData data = datalist.get(position);
            Intent showResult = new Intent(Covid19ArchivedActivity.this, Covid19SearchResultActivity.class);
            showResult.putExtra("search_result", data);
            startActivity(showResult);

        });

        listView.setOnItemLongClickListener(( parent, view, position, id)->{

            Covid19CountryData item = datalist.get(position);
            db.delete(data.TABLE_NAME, data.KEY_ID + "= ?", new String[] {Long.toString(item.getId())});
            datalist.remove(item);
            dataAdapter.notifyDataSetChanged();
            Toast.makeText(listView.getContext(), "Record has been deleted.", Toast.LENGTH_LONG).show();
            return true;
        });


    }



    @Override
    protected int getLayoutId() {
        return R.layout.covid19_activity_archived_data;
    }




    //inner class
    private class CountryList extends ArrayAdapter<Covid19CountryData> {

        public CountryList(@NonNull Context context) {
            super(context,0);
        }
        public Covid19CountryData getItem(int position){
            return datalist.get(position);
        }
        @Override
        public int getCount(){
            return datalist.size();
        }
        @Override
        public int getPosition(@Nullable Covid19CountryData item) {
            return super.getPosition(item);
        }


        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = Covid19ArchivedActivity.this.getLayoutInflater();
            View result = inflater.inflate(R.layout.covid19_country_data_item, null);
            Covid19CountryData item = getItem(position);
            TextView name = result.findViewById(R.id.covid19_country_item_name);
            TextView confirmed = result.findViewById(R.id.covid19_country_item_confirmed);
            TextView daily = result.findViewById(R.id.covid19_country_item_daily);
            TextView date = result.findViewById(R.id.covid19_country_item_date);
            name.setText(item.getCountryName());
            int cases = item.getTotalCase();
            confirmed.setText(String.valueOf(cases));
            if(cases > 100000){
                confirmed.setTextColor(Color.argb(255,193,44,27));
            }
            else if(cases > 10000){
                confirmed.setTextColor(Color.argb(255,224,145,27));
            }
            else{
                confirmed.setTextColor(Color.argb(255,20,151,20));
            }
            int inc = item.getDailyIncrease();
            daily.setText(String.valueOf(inc));
            if(inc > 1000){
                daily.setTextColor(Color.argb(255,193,44,27));
            }
            else if(inc > 100){
                daily.setTextColor(Color.argb(255,224,145,27));
            }
            else{
                daily.setTextColor(Color.argb(255,20,151,20));
            }
            date.setText(item.getSearchDateTime());

            return result;
        }
    }

    private void loadData(){
        data = new Covid19DateHelper(this);
        db = data.getWritableDatabase();
        String [] columns = {data.KEY_ID, data.KEY_COUNTRY, data.KEY_TOTAL,data.KEY_DAILY,data.KEY_QUERY_DATE,data.KEY_DATA};
        Cursor results = db.query(false, data.TABLE_NAME, columns, null, null, null, null, null, null);
        //Now the results object has rows of results that match the query.
        //find the column indices:
        int idColIndex = results.getColumnIndex(data.KEY_ID);
        int countryColIndex = results.getColumnIndex(data.KEY_COUNTRY);
        int totalColIndex = results.getColumnIndex(data.KEY_TOTAL);
        int dailyColIndex = results.getColumnIndex(data.KEY_DAILY);
        int dateColIndex = results.getColumnIndex(data.KEY_QUERY_DATE);
        int dataColIndex = results.getColumnIndex(data.KEY_DATA);

        //iterate over the results, return true if there is a next item:
        while(results.moveToNext())
        {
            long id = results.getLong(idColIndex);
            String country = results.getString(countryColIndex);
            int totalConfirmed = results.getInt(totalColIndex);
            int dailyIncrease = results.getInt(dailyColIndex);
            String queryDate = results.getString(dateColIndex);
            byte[] data = results.getBlob(dataColIndex);

            ArrayList<Covid19ProvinceData> plist = Covid19Util.readByteArray(data);
            //add the new Contact to the array list:
            Covid19CountryData item = new Covid19CountryData(country,queryDate);
            item.setId(id);
            item.setTotalCase(totalConfirmed);
            item.setDailyIncrease(dailyIncrease);
            item.setDataList(plist);
            datalist.add(item);
        }
        dataAdapter.notifyDataSetChanged();
    }





    public void clearDb(View view) {
        db.execSQL("delete from "+ data.TABLE_NAME);
        dataAdapter.notifyDataSetChanged();
        Toast.makeText(this, "DataBase has wiped all records out.", Toast.LENGTH_LONG).show();
    }


}