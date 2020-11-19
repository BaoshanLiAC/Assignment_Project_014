package com.ac.assignment_project_014.covid19;



import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.service.autofill.RegexValidator;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import com.ac.assignment_project_014.R;
import com.ac.assignment_project_014.recipe.RecipeMainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Pattern;

/**
 * Main Activity for COVID-19 CASE DATA: entry of sub project.
 * user enter country name and date(yyyy-mm-dd) to get CASE DATA in a specific day.
 * sharePreference stores country name and date of last search entry.
 *
 */
public class Covid19CaseDataMainActivity extends CovidDrawerBase {
    /**SharedPreference data KEY***/
    protected static final String COVID_PREFERENCES = "Cov_Prefs" ;
    /**SharedPreference data EDITOR***/
    protected SharedPreferences.Editor editor;

    /***Fields***/
    private String url;
    //Default value
    private String countryName = "CANADA";
    //Default value
    private String date = "2020-10-10";
    private Button search;
    private SharedPreferences sharedpreferences;

   // @RequiresApi(api = Build.VERSION_CODES.O_MR1)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initial class fields
        sharedpreferences = getSharedPreferences(COVID_PREFERENCES, Context.MODE_PRIVATE);
        search = findViewById(R.id.covid_data_search_btn);
        search.setOnClickListener(e->{
            EditText name = findViewById(R.id.covid19_search_country_name);
            setCountryName(name.getText().toString());
            EditText date = findViewById(R.id.covid19_search_date);
            Pattern DATE_PATTERN = Pattern.compile("^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])$");
                    //input validation.
                    if(DATE_PATTERN.matcher(date.getText().toString()).matches()){
                        setDate(date.getText().toString());
                        setUrl("https://api.covid19api.com/country/{0}/status/confirmed/live?from={1}T00:00:00Z&to={2}T00:00:00Z");
                        Covid19Server server = new Covid19Server();
                        server.execute();
                    }
                    else{
                        androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("Error")
                                .setMessage("Please enter date in correct format: (YYYY-MM-DD)")
                                .setPositiveButton("OK", (click, arg) -> {date.setText("");})
                                .create().show();
                    }
        });

    }


    /*******************getters and setters****************************/


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url.replace("{0}", getCountryName()).replace("{1}", getDate()).replace("{2}",getNextDate());
    }

    public String getCountryName() {return this.countryName; }

    public void setCountryName(String countryName) {this.countryName = countryName;}

    public String getDate() { return date;}

    public void setDate(String date) {
        if(date == null || date.isEmpty()) this.date = "2020-10-10";
        else this.date = date;
    }

    /**
     * 
     * @return date for next day of input
     */
    private String getNextDate(){
        String result="";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();
            c.setTime(sdf.parse(getDate()));
            c.add(Calendar.DATE, 1);
            result = sdf.format(c.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }






    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }

    /**
     * 
     * @return get ID for super class
     */
    @Override
    protected int getLayoutId() {
        return R.layout.covid19_activity_main;
    }


    //inner class
     class Covid19Server extends AsyncTask< String,JSONArray, JSONArray >
    {
        private final ProgressDialog dialog = new ProgressDialog(Covid19CaseDataMainActivity.this);

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            // display a progress dialog for good user experiance
            dialog.setMessage("Please Wait");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected JSONArray doInBackground(String... params) {
            // implement API in background and store the response in current variable
            JSONObject current;
            try {
                URL url;
                HttpURLConnection urlConnection = null;
                try {
                    url = new URL(getUrl());
                    urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream in = urlConnection.getInputStream();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(in,"utf-8"),8);
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    in.close();
                   return  new JSONArray(sb.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONArray jsonObject)
        {
            ArrayList<Covid19ProvinceData> dataList = new ArrayList<>();
            ArrayList<String> names = new ArrayList<>();
            ArrayList<Integer> cases = new ArrayList<>();
            if (jsonObject != null) {
                try {
                for (int i=0;i<jsonObject.length();i++){
                    JSONObject obj = jsonObject.getJSONObject(i);
                    String pro = obj.getString("Province");
                    int caseNumber = obj.getInt("Cases");
                    //skip bad data
                    if(pro.isEmpty()) continue;
                    //transforming data
                    if(names.contains(pro)){
                        int index = names.indexOf(pro);
                        int oldNumber = cases.get(index);
                        int change = caseNumber - oldNumber;
                        Covid19ProvinceData item = new Covid19ProvinceData(pro,caseNumber,change);
                        dataList.add(item);
                    }
                    else{
                        names.add(pro);
                        cases.add(caseNumber);
                    }
                }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            // dismiss the progress dialog after receiving data from API
            dialog.dismiss();
            Covid19CountryData data = new Covid19CountryData(getCountryName(), getDate());
            data.setDataList(dataList);
            //intent to show search result in another activity
            Intent showResult = new Intent(Covid19CaseDataMainActivity.this, Covid19SearchResultActivity.class);
            showResult.putExtra("search_result", data);
            startActivity(showResult);

            editor = sharedpreferences.edit();
            editor.putString("country",getCountryName());
            editor.putString("date", getDate());
            editor.commit();

        }
    }
}