package com.ac.assignment_project_014.recipe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.ac.assignment_project_014.R;
import com.ac.assignment_project_014.recipe.JsonEntityClass.JsonRootBean;
import com.ac.assignment_project_014.recipe.JsonEntityClass.Results;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecipeMainActivity extends AppCompatActivity {

    private ArrayList<Recipe> recipeArrayList = new ArrayList<>();
    ListView listView;
    private TextView textView ;
    public List<Results> resultsList =new ArrayList<Results>();
    static public  List<Results> favouritelist =new ArrayList<Results>();
    public String local_json = ""; //retrieve the favourite list
    MyResultAdapter adapter1;
    MyfavouriteAdapter adapter2;
    int dataLoadindIcator=0;
    private ArrayList<String> searchHistorylist =new ArrayList<String>();
    public static final String SHARED_PREFS = "shared Prefs";
    public static final String SEARCH_HISTORY = "search_History";
    SQLiteDatabase db;
    DBOpener dbOpener;// = new DBOpener(this);

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

        ProgressBar progressBar = findViewById(R.id.progress_circular);
        progressBar.setVisibility(View.INVISIBLE);//隐藏进度条
        listView = findViewById(R.id.recipe_listView);
        adapter1 = new MyResultAdapter(); //to show search result
        adapter2= new MyfavouriteAdapter(); //to show favourite

        //Respond to user actions,Load Json from url
        btn_search.setOnClickListener((parent)->{
            loadJasonfromUrl();
            dataLoadindIcator=0;});

        //LoadJson from Database, prepared for favourite
        btn_myFavourite.setOnClickListener((parent)->{
            loadFavouriteRecipeformDB();
            dataLoadindIcator=1;
        });

        //Default Load Json from url
        searchHistorylist = getPreferenceData();
        loadJasonfromUrl();

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
                    for(Results res:favouritelist)
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
                    for(Results res:favouritelist)
                        if(url.equals(favouritelist.get(pos).getHref())) goToContent.putExtra("like", "Y");
                startActivity( goToContent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(dataLoadindIcator==0){
                loadJasonfromUrl();
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

    public void LoadJsonfromLocal() {
        dataLoadindIcator=1;
        local_json = getJson(this, "favourite_result");
        Gson gson = new Gson();
        if(local_json!=""){
            JsonRootBean rootClassObject = gson.fromJson(local_json, JsonRootBean.class);
            favouritelist = rootClassObject.getResults();
            String content = "";

            for (Results post: favouritelist){
                content += "title: " +  post.getTitle() +"\n"
                        +  "href: " +   post.getHref()  + "\n"
                        +  "ingredients: " + post.getIngredients() +"\n"
                        +  "thumbnail: "   + post.getThumbnail() +"\n\n";
            }
            listView.setAdapter(adapter2);
        }
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
            favouritelist.add(new Results(title,herf,ingredients,thumbnail));
        }
        listView.setAdapter(adapter2);
    }


    //Load from Url
    public void loadJasonfromUrl(){
        dataLoadindIcator=0;
        Retrofit retrofit = new Retrofit.Builder()
                //.baseUrl("http://jsonplaceholder.typicode.com/")
                .baseUrl("http://www.recipepuppy.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderAPI jsonPlaceHolderAPI = retrofit.create(JsonPlaceHolderAPI.class);
        Call<JsonRootBean> call = jsonPlaceHolderAPI.getPosts();
        //Call<JsonRootBean> call = jsonPlaceHolderAPI.getPosts("onions",null);
       //Call<JsonRootBean> call = jsonPlaceHolderAPI.getPosts(searchHistorylist.get(searchHistorylist.size()-1));

        call.enqueue(new Callback<JsonRootBean>() {
            @Override
            public void onResponse(Call<JsonRootBean> call, Response<JsonRootBean> response) {
                if(!response.isSuccessful()){
                    //textView.setText("Code" + response.code());
                    return;
                }
                JsonRootBean rootBean = response.body();
                String content = "\n";
                //Root Class Information
                content +=  "title:"  + rootBean.getTitle() + "\n"
                        +   "version:" + rootBean.getVersion() + "\n"
                        +   "href:"     + rootBean.getHref()+"\n\n\n";
                //Recipe class information
                resultsList = rootBean.getResults();
                for (Results post: resultsList){
                    content += "title: " +  post.getTitle() +"\n"
                            +  "href: " +   post.getHref()  + "\n"
                            +  "ingredients: " + post.getIngredients() +"\n"
                            +  "thumbnail: "   + post.getThumbnail() +"\n\n";
                }
                listView.setAdapter(adapter1);
            }
            public void onFailure(Call<JsonRootBean> call, Throwable t) {
                //textView.setText(t.getMessage());
            }
        });
    }

    //get jason string from txt file
    public static String getJson(Context mContext, String fileName) {
        // TODO Auto-generated method stub
        StringBuilder sb = new StringBuilder();
        AssetManager am = mContext.getAssets();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    am.open(fileName)));
            String next = "";
            while (null != (next = br.readLine())) {
                sb.append(next);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            sb.delete(0, sb.length());
        }
        return sb.toString().trim();
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

}