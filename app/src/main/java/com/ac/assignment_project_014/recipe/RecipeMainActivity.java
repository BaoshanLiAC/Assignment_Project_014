package com.ac.assignment_project_014.recipe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.ac.assignment_project_014.R;

import java.util.ArrayList;

public class RecipeMainActivity extends AppCompatActivity {

   // private ArrayList<Recipe> recipeArrayList = new ArrayList<>();
/*    ListView lv;
    ArrayAdapter adapter;
    ArrayList<String> list1 = new ArrayList<String>();
    SearchView sv;*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_activity_main);
        ImageButton searchImgBtn = findViewById(R.id.searchIcon);
        Intent goToSearchHistory  = new Intent(this, SearchHistoryActivity.class);
        Button searchBtn = findViewById(R.id.searchButton);
        ImageButton searchCriteriaBtn = findViewById(R.id.SearchCriteria);

        searchImgBtn.setOnClickListener(click -> {startActivity( goToSearchHistory );});
        searchBtn.setOnClickListener(click -> {startActivity( goToSearchHistory );});

        searchCriteriaBtn.setOnClickListener(click -> {startActivity( new Intent(this, SearchCriteria.class) );});

        ProgressBar progressBar = findViewById(R.id.progress_circular);
        progressBar.setVisibility(View.VISIBLE);//隐藏进度条

/*        ListView recipeListView = findViewById(R.id.recipe_listView);
        lv = (ListView)findViewById(R.id.recipe_listView);
        sv = (SearchView)findViewById(R.id.search_bar);

        list1.add("Monday");
        list1.add("Tuesday");
        list1.add("Wednesday");
        list1.add("Thursday");
        list1.add("Friday");
        list1.add("Saturday");
        list1.add("Sunday");
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,list1);
        lv.setAdapter(adapter);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
               return false;
            }
        });*/

    }


}