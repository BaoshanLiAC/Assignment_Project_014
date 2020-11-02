package com.ac.assignment_project_014.recipe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.ac.assignment_project_014.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.Inflater;

public class SearchHistoryActivity extends AppCompatActivity {

    ListView listView;
    MyListAdapter adapter;
    ArrayList<String> searchHistoryText = new ArrayList<String>();
    SearchView searchView;
    int image = R.drawable.ic_baseline_history_24;

    class MyListAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return searchHistoryText.size();
        }

        @Override
        public Object getItem(int position) {
            return searchHistoryText.get(position);
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
            text.setText(searchHistoryText.get(position));

            return newView;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_history);

        listView = (ListView)findViewById(R.id.recipe_listView);

        //Temperate
        searchHistoryText.add("Monday");
        searchHistoryText.add("Tuesday");
        searchHistoryText.add("Wednesday");
        searchHistoryText.add("Thursday");
        searchHistoryText.add("Friday");
        searchHistoryText.add("Saturday");
        searchHistoryText.add("Sunday");

        adapter= new MyListAdapter();
        listView.setAdapter(adapter);         //To populate the ListView with data

        searchView = (SearchView)findViewById(R.id.search_bar);
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint("Search Here");
        searchView.setSubmitButtonEnabled(true);

        // History List update when editing search bar
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            ArrayList<String> temp = new ArrayList<String>(searchHistoryText);
                @Override
                public boolean onQueryTextSubmit(String query) {
                    searchHistoryText.clear();
                    searchHistoryText.addAll(temp);
                    adapter.notifyDataSetChanged();
                    return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText != null){
                    searchHistoryText.clear();
                    searchHistoryText.addAll(temp);
                    Object str[] = searchHistoryText.toArray();
                    for(Object obj: str)
                        if(!obj.toString().contains(newText))
                            searchHistoryText.remove(obj);
                        adapter.notifyDataSetChanged();
                }
                return false;
            }
        });
    }





}