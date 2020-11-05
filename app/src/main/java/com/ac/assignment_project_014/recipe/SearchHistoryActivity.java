package com.ac.assignment_project_014.recipe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.ac.assignment_project_014.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.Inflater;

public class SearchHistoryActivity extends AppCompatActivity {

    private ListView listView;
    private MyListAdapter adapter;
    private ArrayList<String> searchHistorylist =new ArrayList<String>();
    private SearchView searchView;
    int image = R.drawable.ic_baseline_history_24;
    private SharedPreferences sharedPref =null;
    public static final String SHARED_PREFS = "shared Prefs";
    public static final String SEARCH_HISTORY = "search_History";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_history);

        listView = (ListView)findViewById(R.id.recipe_listView);

        searchHistorylist = getPreferenceData();

        adapter= new MyListAdapter();
        listView.setAdapter(adapter);         //To populate the ListView with data

        listView.setOnItemClickListener((parent, view, pos, id)->{
            searchView.setQuery(searchHistorylist.get(pos),true);
        });

        searchView = (SearchView)findViewById(R.id.search_bar);
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint("Search Here");
        searchView.setSubmitButtonEnabled(true);


        //searchView.setQuery("",true);
        Intent goToSearchResult  = new Intent(getBaseContext(), RecipeMainActivity.class);

        // History List update when editing search bar
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            ArrayList<String> temp = new ArrayList<String>(searchHistorylist);
                @Override
                public boolean onQueryTextSubmit(String query) {
                    setPreferenceData();
                    if(query!=null){
                        searchHistorylist.clear();
                        searchHistorylist.addAll(temp);
                        adapter.notifyDataSetChanged();
                        startActivity( goToSearchResult );
                        return true;
                    }
                    startActivity( goToSearchResult );
                    return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText != null){
                    searchHistorylist.clear();
                    searchHistorylist.addAll(temp);
                    Object str[] = searchHistorylist.toArray();
                    for(Object obj: str)
                        if(!obj.toString().contains(newText))
                            searchHistorylist.remove(obj);
                        adapter.notifyDataSetChanged();
                    return true;
                }
                return false;
            }
        });
    }

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


    private void setPreferenceData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        searchHistorylist.add(searchView.getQuery().toString());
        String json = gson.toJson(searchHistorylist);
        editor.putString(SEARCH_HISTORY,json);
        editor.commit();
    }


    class MyListAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return searchHistorylist.size();
        }

        @Override
        public Object getItem(int position) {
            return searchHistorylist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return (long) position;
        }

        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View newView = inflater.inflate(R.layout.search_history_row, parent, false );
            newView.findViewById(R.id.searchHistoryItem);
            TextView text  = newView.findViewById(R.id.searchHistoryItem);
            ImageView image =   newView.findViewById(R.id.searchImage);
            image.setImageResource(R.drawable.ic_baseline_history_24);
            text.setText(searchHistorylist.get(position));
            return newView;
        }
    }






}