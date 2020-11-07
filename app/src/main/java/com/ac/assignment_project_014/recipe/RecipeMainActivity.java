package com.ac.assignment_project_014.recipe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ac.assignment_project_014.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class RecipeMainActivity extends AppCompatActivity {

    ListView listView;
    private TextView textView ;
    static public List<JsonResults> resultsList =new ArrayList<JsonResults>();
    static public  List<JsonResults> favouritelist =new ArrayList<JsonResults>();
    static public String jsonString = ""; //retrieve the favourite list
    static MyResultAdapter adapter1;
    static MyfavouriteAdapter adapter2;
    int dataLoadindIcator=0;
    private ArrayList<String> searchHistorylist =new ArrayList<String>();
    public static final String SHARED_PREFS = "shared Prefs";
    public static final String SEARCH_HISTORY = "search_History";
    SQLiteDatabase db;
    DBOpener dbOpener;// = new DBOpener(this);
    public static ProgressBar progressbar;// = findViewById(R.id.progress_bar);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_activity_main);

        //Database initialization
        dbOpener = new DBOpener(this);

        //Layout initialization
        ImageButton searchImgBtn = findViewById(R.id.searchIcon);
        Intent goToSearchHistory  = new Intent(this, SearchHistoryActivity.class);
        Button searchBtn = findViewById(R.id.searchButton);
        ImageButton searchCriteriaBtn = findViewById(R.id.SearchCriteria);
        searchImgBtn.setOnClickListener(click -> {startActivity( goToSearchHistory );});
        searchBtn.setOnClickListener(click -> {startActivity( goToSearchHistory );});
        searchCriteriaBtn.setOnClickListener(click -> {startActivity( new Intent(this, SearchCriteria.class) );});
        Button btn_myFavourite = findViewById(R.id.myFavourite);
        Button btn_search = findViewById(R.id.searchResult);

        progressbar = findViewById(R.id.progress_bar);
        listView = findViewById(R.id.recipe_listView);
        adapter1 = new MyResultAdapter(); //to show search result
        adapter2= new MyfavouriteAdapter(); //to show favourite
        //Initial recipe_listView
        LoadJsonfromURL();

        //Respond to user actions,Load Json from url
        btn_search.setOnClickListener((parent)->{
            LoadJsonfromURL();
            dataLoadindIcator=0;});

        //LoadJson from Database, prepared for favourite
        btn_myFavourite.setOnClickListener((parent)->{
            loadFavouriteRecipeformDB();
            dataLoadindIcator=1;
        });

        //Default Load Json from url
        searchHistorylist = getPreferenceData();

        //Click on each item to see the detailed content
        listView.setOnItemClickListener((parent, view, pos, id) ->  {
            if(dataLoadindIcator==0){
                Intent goToContent  = new Intent(this, RecipeContentActivity.class);
                goToContent.putExtra("thumbnail", resultsList.get(pos).getThumbnail());
                goToContent.putExtra("title", resultsList.get(pos).getTitle());
                goToContent.putExtra("ingredients", resultsList.get(pos).getIngredients());

                String url =  resultsList.get(pos).getHref();
                goToContent.putExtra("href", url);

                //default value is N: not in the favourite list;
                goToContent.putExtra("like", "N");
                if (favouritelist!=null)
                    for(JsonResults res:favouritelist)
                        if(url.equals(resultsList.get(pos).getHref())) goToContent.putExtra("like", "Y");
                startActivity( goToContent);
            }

            else if(dataLoadindIcator==1){
                Intent goToContent  = new Intent(this, RecipeContentActivity.class);
                favouritelist.clear();
                loadFavouriteRecipeformDB();
                goToContent.putExtra("thumbnail", favouritelist.get(pos).getThumbnail());
                goToContent.putExtra("title", favouritelist.get(pos).getTitle());
                goToContent.putExtra("ingredients", favouritelist.get(pos).getIngredients());

                String url =  favouritelist.get(pos).getHref();
                goToContent.putExtra("href", url);

                //default value is N: not in the favourite list;
                goToContent.putExtra("like", "N");
                if (favouritelist!=null)
                    for(JsonResults res:favouritelist)
                        if(url.equals(favouritelist.get(pos).getHref())) goToContent.putExtra("like", "Y");
                startActivity( goToContent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(dataLoadindIcator==0){
                LoadJsonfromURL();
                dataLoadindIcator=0;
        }
        else{
            loadFavouriteRecipeformDB();
            dataLoadindIcator=1;
        }
    }

    //load data from SharedPreferences
    private ArrayList<String> getPreferenceData(){
        //Temperate
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(SEARCH_HISTORY,null);
        Type type = new TypeToken<ArrayList<String>>(){}.getType();
        searchHistorylist = gson.fromJson(json,type);
        if(searchHistorylist!=null)
            return searchHistorylist;
        else
            return new ArrayList<String>();
    }



    //load data into ArrayList favouritelist
    public void loadFavouriteRecipeformDB() {
        favouritelist.clear();
        db = dbOpener.getWritableDatabase(); //This calls onCreate() if you've never built the table before, or onUpgrade if the version here is newer
        String [] columns = {dbOpener.COL_ID, dbOpener.COL_TITLE, dbOpener.COL_HREF, dbOpener.COL_INGREDIENT, dbOpener.COL_THUMBNAIL};
        Cursor results = db.query(false, dbOpener.TABLE_NAME, columns, null, null, null, null, null, null);

        int id_index = results.getColumnIndex(dbOpener.COL_ID);
        int title_index = results.getColumnIndex(dbOpener.COL_TITLE);
        int href_index = results.getColumnIndex(dbOpener.COL_HREF);
        int ingredients_index = results.getColumnIndex(dbOpener.COL_INGREDIENT);
        int thumbnail_index = results.getColumnIndex(dbOpener.COL_THUMBNAIL);

        while(results.moveToNext()){
            Boolean send, receive;
            long id = results.getLong(id_index);
            String title = results.getString(title_index);
            String herf = results.getString(href_index);
            String ingredients = results.getString(ingredients_index);
            String thumbnail = results.getString(thumbnail_index);
            favouritelist.add(new JsonResults(title,herf,ingredients,thumbnail));
        }
        listView.setAdapter(adapter2);
    }


    class MyResultAdapter extends BaseAdapter {
        @Override
        public int getCount() { return resultsList.size(); }
        @Override
        public Object getItem(int position) { return resultsList.get(position); }
        @Override
        public long getItemId(int position) { return (long) position; }
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View newView = inflater.inflate(R.layout.result_row, parent, false);
            newView.findViewById(R.id.title);
            TextView text = newView.findViewById(R.id.title);
            ImageView imageView = newView.findViewById(R.id.resultRow_imageView);

            //set Text and image
            //imageView.setImageResource(R.drawable.ic_baseline_history_24);
            Picasso.get().load(resultsList.get(position).getThumbnail()).into(imageView);
            text.setText(resultsList.get(position).getTitle());
            return newView;
        }
    }


    class MyfavouriteAdapter extends BaseAdapter {

        @Override
        public int getCount() { return favouritelist.size(); }

        @Override
        public Object getItem(int position) { return favouritelist.get(position); }

        @Override
        public long getItemId(int position) { return (long) position; }

        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View newView = inflater.inflate(R.layout.result_row, parent, false);
            newView.findViewById(R.id.title);
            TextView text = newView.findViewById(R.id.title);
            ImageView imageView = newView.findViewById(R.id.resultRow_imageView);

            //set Text and image
            //imageView.setImageResource(R.drawable.ic_baseline_history_24);
            Picasso.get().load(favouritelist.get(position).getThumbnail()).into(imageView);
            text.setText(favouritelist.get(position).getTitle());
            return newView;

        }
    }

    public void LoadJsonfromURL() {
        dataLoadindIcator=1;
        Jsonloader loader = new Jsonloader(this,"http://www.recipepuppy.com/api/?i=onions",listView);
        loader.execute("http://www.recipepuppy.com/api/?i=onions");

    }

    public class Jsonloader extends AsyncTask<String,Integer,String>{

        Context context;
        String jsonURL;
        ListView recopelistView;

        public Jsonloader(Context context, String jsonURL, ListView listView){
            this.context = context;
            this.jsonURL = jsonURL;
            this.recopelistView = listView;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            RecipeMainActivity.progressbar.setVisibility(View.VISIBLE);

        }

        //Type 1
        @Override
        protected String doInBackground(String... args) {
            for(int i = 0; i< 4; i++){
                publishProgress((i*100) / 4); //Type2
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            try{
                resultsList.clear();
                //create a URL object of what server to contact:
                URL url = new URL(args[0]);
                //open the connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                //wait for data:
                InputStream response = urlConnection.getInputStream();

                //InputStream response = new BufferedInputStream(urlConnection.getInputStream());
                if (urlConnection.getResponseCode() == urlConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);

                    String line;
                    StringBuffer jsonData = new StringBuffer();

                    //READ
                    while ((line = reader.readLine()) != null)
                        jsonData.append(line + "\n");
                    //CLOSE RESOURCES
                    reader.close();
                    response.close();
                    JSONArray jsonArray;
                    if(jsonData.toString()!=""){
                        jsonString = jsonData.toString();
                        JSONObject jsonObjectobj = new JSONObject(jsonString);
                         jsonArray = jsonObjectobj.getJSONArray("results");
                         String title,href,ingredients,thumbnail;
                        for (int i=0; i < jsonArray.length(); i++){
                                JSONObject anObject = jsonArray.getJSONObject(i);
                                // Pulling items from the array
                                title = anObject.getString("title");
                                //anObject.getBoolean(booleanName);
                                href = anObject.getString("href");
                                ingredients = anObject.getString("ingredients");
                                thumbnail= anObject.getString("thumbnail");
                                resultsList.add(new JsonResults(title,href,ingredients,thumbnail));
                    }
                    }
                    //listView.setAdapter(adapter1);
                    return jsonString;
                } else
                    return "Error" + urlConnection.getResponseMessage();

            }catch (IOException | JSONException e) {
                e.printStackTrace();
                return "Error" + e.getMessage();
            }
        }

        @Override
        protected void onProgressUpdate(Integer... integers) {
            super.onProgressUpdate(integers);
            RecipeMainActivity.progressbar.setProgress(integers[0]);
        }

        @Override //Type 3
        protected void onPostExecute(String jsonString) {
            super.onPostExecute(jsonString);
            RecipeMainActivity.progressbar.setProgress(75);
            RecipeMainActivity.progressbar.setVisibility(View.INVISIBLE);
            if(resultsList!=null)
                listView.setAdapter(adapter1);
        }

    }

}