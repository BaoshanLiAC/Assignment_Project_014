package com.ac.assignment_project_014.covid19;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.ac.assignment_project_014.MainActivity;
import com.ac.assignment_project_014.R;
import com.google.android.material.snackbar.Snackbar;

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

import static android.view.View.TEXT_ALIGNMENT_TEXT_START;

public class Covid19CaseDataMainActivity extends AppCompatActivity {
    private String url;
    private String countryName;
    private String date;
    protected Button search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.covid19_activity_main);

        setSupportActionBar(findViewById(R.id.covid19_toolbar));
        search = findViewById(R.id.covid_data_search_btn);
        search.setOnClickListener(e->{
            EditText name = findViewById(R.id.covid19_search_country_name);
            setCountryName(name.getText().toString());
            EditText date = findViewById(R.id.covid19_search_date);
            setDate(date.getText().toString());
            setUrl("https://api.covid19api.com/country/{0}/status/confirmed/live?from={1}T00:00:00Z&to={2}T00:00:00Z");
            Covid19Server server = new Covid19Server();
            server.execute();
           // startActivity(new Intent(this, Covid19SearchResultActivity.class));
        });


    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url.replace("{0}", getCountryName()).replace("{1}", getDate()).replace("{2}",getNextDate());
    }

    public String getCountryName() {
        if(this.countryName == null || this.countryName.isEmpty()){return "CANADA";}
        else {return countryName.toUpperCase();}
    }

    public void setCountryName(String countryName) {
        if(countryName == null || countryName.isEmpty()){this.countryName = "CANADA";}
        this.countryName = countryName;
    }

    public String getDate() {
        if(date == null || date.isEmpty()) {return "2020-10-10";}
        else{ return date;}
    }

    public void setDate(String date) {
        if(date == null || date.isEmpty()) this.date = "2020-10-10";
        else this.date = date;
    }
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

    /**
     * TOOL BAR
     *
     */
    public boolean onCreateOptionsMenu(Menu m){

        getMenuInflater().inflate(R.menu.covid19_toolbar_menu, m );
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem mi){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View view =this.getLayoutInflater().inflate(R.layout.covid19_actionbar_dialog,null);
        TextView text = view.findViewById(R.id.covid19_action_dialog_text);
        switch(mi.getItemId()){
            case R.id.covid19_action_home:
                Snackbar.make(findViewById(R.id.covid19_toolbar), "Back to Home Page", Snackbar.LENGTH_LONG)
                        .setAction("Action", e->startActivity(new Intent(this, MainActivity.class))).show();
                break;
            case R.id.covid19_action_search:

                    Toast.makeText(this,"Already in Search Tab", Toast.LENGTH_LONG).show();

                break;
            case R.id.covid19_action_archive:
                Snackbar.make(findViewById(R.id.covid19_toolbar), "Jump to Archived Case Data", Snackbar.LENGTH_LONG)
                        .setAction("Action", e->startActivity(new Intent(this, Covid19ArchivedActivity.class))).show();
                break;
            case R.id.covid19_action_version:

                text.setText("VERSION:1.0.0.1");
                text.setTextSize(25);
                builder.setView(view);
                builder.create().show();
                break;
            case R.id.covid19_action_help:

                text.setText("1. Query date from https://api.covid19api.com/country by input country and date." +
                            "\n2. User could save result to archive for later analysis." +
                            "\n3. User could navigate to archived tap to view saved record and manipulate data.");
                text.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
                builder.setView(view);
                builder.create().show();
                break;
            case R.id.covid19_action_about:
                text.setText("Developed By: Li Sha Wu" +
                        "\nStudent No: **********" +
                        "\nSupported By: Dr. Eric");
                text.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
                builder.setView(view);
                builder.create().show();
                break;
            default:
                break;
        }
        return true;
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
            ArrayList<Covid19ProvinceData> listdata = new ArrayList<>();
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
                        listdata.add(item);
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
            data.setDataList(listdata);
            Intent showResult = new Intent(Covid19CaseDataMainActivity.this, Covid19SearchResultActivity.class);
            showResult.putExtra("search_result", data);
            startActivity(showResult);
        }
    }
}