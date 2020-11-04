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
import android.widget.TextView;

import com.ac.assignment_project_014.R;
import com.ac.assignment_project_014.recipe.JsonEntityClass.JsonRootBean;
import com.ac.assignment_project_014.recipe.JsonEntityClass.Results;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecipeMainActivity extends AppCompatActivity {

    private ArrayList<Recipe> recipeArrayList = new ArrayList<>();
    ListView lv;
    ArrayAdapter adapter;
    ArrayList<String> list1 = new ArrayList<String>();
    SearchView sv;


    //private TextView textView ;
    List<Results> resultsList;

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
        progressBar.setVisibility(View.INVISIBLE);//隐藏进度条

        //ListView

        // 1. 创建Gson对象
        //Gson gson = new Gson();

        // 2. 创建JavaBean类的对象
        JsonRootBean jsonRootBean = new JsonRootBean();


        // 3. 使用Gson解析：将JSON数据转为单个类实体
        //String json = "";
        //jsonRootBean = gson.fromJson(json,JsonRootBean.class);





        //textView = findViewById(R.id.text_view_result);
        Retrofit retrofit = new Retrofit.Builder()
                //.baseUrl("http://jsonplaceholder.typicode.com/")
                .baseUrl("http://www.recipepuppy.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderAPI jsonPlaceHolderAPI = retrofit.create(JsonPlaceHolderAPI.class);
        Call<JsonRootBean> call = jsonPlaceHolderAPI.getPosts();

    call.enqueue(new Callback<JsonRootBean>() {

       @Override
       public void onResponse(Call<JsonRootBean> call, Response<JsonRootBean> response) {
          if(!response.isSuccessful()){
              // textView.setText("Code" + response.code());
               return;
           }
          JsonRootBean results = response.body();
               String content = "";
               content +=  "title:"  + results.getTitle() + "\n"
                       +   "version:" + results.getVersion() + "\n"
                       +   "href:"     + results.getHref()+"\n\n\n";
               resultsList = results.getResults();
                       for (Results post: resultsList){
                           content += "title: " +  post.getTitle() +"\n"
                                   +  "href: " +   post.getHref()  + "\n"
                                   +  "ingredients: " + post.getIngredients() +"\n"
                                   +  "thumbnail: "   + post.getThumbnail() +"\n\n";
                       }
               // textView.append(content);

           }

        public void onFailure(Call<JsonRootBean> call, Throwable t) {
        //   textView.setText(t.getMessage());
       }

/*@Override
public void onResponse(Call<List<Results>> call, Response<List<Results>> response) {
    if(!response.isSuccessful()){
        textView.setText("Code" + response.code());
        return;
    }
    List<Results> results = response.body();
    for (Results  result: results){
        String content = "";
        content +=  "ID"  + result.getTitle() + "\n"
                +   "User ID" + result.getHref() + "\n"
                +   "Title"     + result.getIngredients();
        textView.append(content);
    }
}

        @Override
        public void onFailure(Call<List<Results>> call, Throwable t) {
            textView.setText(t.getMessage());
        }*/


    });

      /*  list1.add(results);

        ListView recipeListView = findViewById(R.id.recipe_listView);
        lv = (ListView)findViewById(R.id.recipe_listView);
        sv = (SearchView)findViewById(R.id.search_bar);

       *//* list1.add("Monday");
        list1.add("Tuesday");
        list1.add("Wednesday");
        list1.add("Thursday");
        list1.add("Friday");
        list1.add("Saturday");
        list1.add("Sunday");*//*
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