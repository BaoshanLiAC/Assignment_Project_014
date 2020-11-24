package com.ac.assignment_project_014.recipe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.ac.assignment_project_014.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
/**
 * This class responsible for the  activities of search page, this page will load the histrical
 * search key words.
 *
 */
public class RecipeSearchHistoryActivity extends AppCompatActivity {

    private ListView listView;
    private MyListAdapter adapter;
    private ArrayList<String> searchHistorylist =new ArrayList<String>();
    private SearchView searchView;
    int image = R.drawable.ic_baseline_history_24;
    private SharedPreferences sharedPref =null;
    public static final String SHARED_PREFS = "shared Prefs";
    public static final String SEARCH_HISTORY = "search_History";

    /**
     * Called when the page is first loaded
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_activity_search_history);


        listView = (ListView)findViewById(R.id.recipe_listView);
        searchHistorylist = getPreferenceData();
        adapter= new MyListAdapter();
        listView.setAdapter(adapter);         //To populate the ListView with data

        listView.setOnItemClickListener((parent, view, pos, id)->{
            searchView.setQuery(searchHistorylist.get(pos),true);
        });

        searchView = (SearchView)findViewById(R.id.search_bar);
        searchView.onActionViewExpanded();
        /* Used to accept user input
        EditText searchText = findViewById(R.id.edit_text);
        searchText.getText();
        */
        searchView.setQueryHint("Search Here");
        Intent goToSearchResult  = new Intent(getBaseContext(), RecipeMainActivity.class);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            ArrayList<String> temp = new ArrayList<String>(searchHistorylist);
            @Override
            public boolean onQueryTextSubmit(String query) {
                setPreferenceData(query);
                if(query!=null){
                    RecipeMainActivity.viewLoadIndicator = 1;
                    RecipeMainActivity.keyWorkd = query;
                    startActivity( goToSearchResult );
                    return true;
                }
                startActivity( goToSearchResult );
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });

    }

    /**
     * Called when the page is first loaded
     *
     * @return  a searchHistorylist or an empty list, this list will be stored into
     * the Preference file
     */
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

    /**
     * Called when the page is first loaded
     *
     * @param   newQuery will be saved into Preference file
     * the Preference file
     */
    private void setPreferenceData(String newQuery){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        if(!searchHistorylist.contains(newQuery)) searchHistorylist.add(searchView.getQuery().toString());
        else {searchHistorylist.remove(newQuery); searchHistorylist.add(0,newQuery);}
        String json = gson.toJson(searchHistorylist);
        editor.putString(SEARCH_HISTORY,json);
        editor.commit();
    }

    /**
     * Inner class, used to Set My favourite Adapter
     *
     */
    class MyListAdapter extends BaseAdapter{
        /**
         * get the total item
         * @return favouritelist, which will load my favourite recipe
         */
        @Override
        public int getCount() {
            return searchHistorylist.size();
        }
        /**
         * get the current item
         * @return the current item position
         */
        @Override
        public Object getItem(int position) {
            return searchHistorylist.get(position);
        }
        /**
         * get the current item id
         * @return the current item position
         */
        @Override
        public long getItemId(int position) {
            return (long) position;
        }
        /**
         * get the view of current Item
         * @return the current item position
         */
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View newView = inflater.inflate(R.layout.recipe_search_history_row, parent, false );
            newView.findViewById(R.id.searchHistoryItem);
            TextView text  = newView.findViewById(R.id.searchHistoryItem);
            ImageView image =   newView.findViewById(R.id.searchImage);
            image.setImageResource(R.drawable.ic_baseline_history_24);
            text.setText(searchHistorylist.get(position));
            return newView;
        }
    }


}