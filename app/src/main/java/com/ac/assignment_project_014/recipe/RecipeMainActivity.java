package com.ac.assignment_project_014.recipe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ac.assignment_project_014.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Chunyan Ren
 * @version 1.0
 * This class is the entrance and Main Activity of recipe Activity. It extends from
 * a base class RecipeDrawerBase, which includes a toolbar and drawer for this main activity.
 *<p>
 * In the recipe Activity, it contains a list to display the recipe retrieved from the url request
 * or database search. Two button take the responsibility to switch between the search result and
 * favourite list. a search navigation lead to a search page.
 *
 *
 *
 */

public class RecipeMainActivity extends RecipeDrawerBase {

    public static  ListView mainlistView;
    public static  List<JsonResults> searchResultsList =new ArrayList<JsonResults>();
    public static  List<JsonResults> favouritelist =new ArrayList<JsonResults>();
    static MyResultAdapter myResultAdapter;
    static MyfavouriteAdapter myfavouriteAdapter;
    public static  ProgressBar progressbar;
    public static int viewLoadIndicator=0; // 1:  from searchlist; 0: from favourite list
    public static  String keyWorkd = "";
    public static  String THUMBNAIL = "thumbnail";
    public static  String TITLE = "title";
    public static  String INGREDIENTS = "ingredients";
    public static  String HREF = "href";
    public static  String ISLIKE = "like";
    public static SQLiteDatabase db;
    public static DBOpener dbOpener;
    /*Above attributes are set to be static, can be manipulated in Jsonloader */

    public static final String SHARED_PREFS = "shared Prefs";
    public static final String SEARCH_HISTORY = "search_History";
    Button btn_myFavourite;
    Button btn_search;
    DrawerLayout drawer;


    /**
     * Called when the main activity of recipe is first created, including initialization.
     * this page will also have different apperences
     * <p>
     * @param savedInstanceState the State of current activity is saved in this parameter
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Database initialization
        dbOpener = new DBOpener(this);

        // search area
        TextView searchViewBtn = findViewById(R.id.searchView);
        Intent goToSearchHistory  = new Intent(this, RecipeSearchHistoryActivity.class);
        searchViewBtn.setOnClickListener(click -> {startActivity( goToSearchHistory );});

        // used to switch the listview
        progressbar = findViewById(R.id.progress_bar);
        mainlistView = findViewById(R.id.recipe_listView);

        myResultAdapter = new MyResultAdapter(); //to show search result
        myfavouriteAdapter = new MyfavouriteAdapter(); //to show favourite
        loadFavouriteRecipeformDB();

        btn_search = findViewById(R.id.searchResult);
        btn_search.setOnClickListener((parent)->{         //Respond to user actions,Load Json from url
            if(searchResultsList.isEmpty()) loadJsonfromURL("http://www.recipepuppy.com/api/?i=onions");
            else
                mainlistView.setAdapter(myResultAdapter);
            viewLoadIndicator=1;});

        btn_myFavourite = findViewById(R.id.myFavourite);
        btn_myFavourite.setOnClickListener((parent)->{         //LoadJson from Database, prepared for favourite
            loadFavouriteRecipeformDB();
            viewLoadIndicator=0;
        });



        boolean isTablet = findViewById(R.id.fragmentLocation) != null; //check if the FrameLayout is loaded

        mainlistView.setOnItemClickListener((parent, view, pos, id) ->  {
            Bundle dataToPass = new Bundle();
            if (viewLoadIndicator==1){
                dataToPass.putString(THUMBNAIL, searchResultsList.get(pos).getThumbnail());
                dataToPass.putString(TITLE, searchResultsList.get(pos).getTitle());
                dataToPass.putString(INGREDIENTS, searchResultsList.get(pos).getIngredients());
                dataToPass.putString(HREF, searchResultsList.get(pos).getHref());
                dataToPass.putBoolean(ISLIKE, false);
                for(JsonResults resFavourite:favouritelist)
                    if(searchResultsList.get(pos).getHref().equals(resFavourite.getHref())) dataToPass.putBoolean("like", true);
            }
            else{
                favouritelist.clear();
                loadFavouriteRecipeformDB();
                dataToPass.putString(THUMBNAIL, favouritelist.get(pos).getThumbnail());
                dataToPass.putString(TITLE, favouritelist.get(pos).getTitle());
                dataToPass.putString(INGREDIENTS, favouritelist.get(pos).getIngredients());
                dataToPass.putString(HREF, favouritelist.get(pos).getHref());
                dataToPass.putBoolean(ISLIKE, true);
            }


            if(isTablet) {
                DetailFragment dFragment = new DetailFragment(); //add a DetailFragment
                dFragment.setArguments( dataToPass ); //pass it a bundle for information
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentLocation, dFragment) //Add the fragment in FrameLayout
                        .commit(); //actually load the fragment. Calls onCreate() in DetailFragment
            }
            else //isPhone
            {
                Intent nextActivity = new Intent(this, EmptyActivity.class);
                nextActivity.putExtras(dataToPass); //send data to next activity
                startActivity(nextActivity); //make the transition
            }

        });
    }

    /**
     * Return the specified layout ID as the current loaded layout ID
     * <p>
     * @return the int number of layout ID
     *
     */
    @Override
    protected int getLayoutId() {
        return R.layout.recipe_activity_main;
    }


    /**
     * Called when came back from another activity
     * <p>
     * <li> IF go back from the search page with a specified search key words
     * \n then will load the result from the searched url.
     * <li>IF go back from the search page without a specified search key words
     * \n will load the historical search result
     * <li>IF the previously page was my favourite list, then go back and
     * \n reload the favourite list
     *
     */
    @Override
    protected void onResume() {
        super.onResume();
        if(viewLoadIndicator==0){
            favouritelist.clear();
            loadFavouriteRecipeformDB();
        }
        else{
            if(keyWorkd == "")  mainlistView.setAdapter(myResultAdapter);
            else{
                loadJsonfromURL("http://www.recipepuppy.com/api/?i=" + keyWorkd);
                keyWorkd = "";
            }

        }
    }


    /**
     * load data into ArrayList favourite list, and displayed on the page
     *
     */
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
        mainlistView.setAdapter(myfavouriteAdapter);
    }


    /**
     * load data into ArrayList favourite list, and displayed on the page
     *
     */
    class MyResultAdapter extends BaseAdapter {
        @Override
        public int getCount() { return searchResultsList.size(); }
        @Override
        public Object getItem(int position) { return searchResultsList.get(position); }
        @Override
        public long getItemId(int position) { return (long) position; }
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View newView = inflater.inflate(R.layout.recipe_result_row, parent, false);
            newView.findViewById(R.id.title);
            TextView text = newView.findViewById(R.id.title);
            ImageView imageView = newView.findViewById(R.id.resultRow_imageView);

            //set Text and image
            //imageView.setImageResource(R.drawable.ic_baseline_history_24);
            if(!searchResultsList.isEmpty()){
                Picasso.get().load(searchResultsList.get(position).getThumbnail()).into(imageView);
                text.setText(searchResultsList.get(position).getTitle());
            }
            return newView;
        }
    }

    /**
     * Inner class, used to Set My favourite Adapter
     *
     */
    class MyfavouriteAdapter extends BaseAdapter {

        /**
         * get the total item
         * @return favouritelist, which will load my favourite recipe
         */
        @Override
        public int getCount() { return favouritelist.size(); }

        /**
         * get the current item
         * @return the current item position
         */
        @Override
        public Object getItem(int position) { return favouritelist.get(position); }
        /**
         * get the current item id
         * @return the current item position
         */
        @Override
        public long getItemId(int position) { return (long) position; }

        /**
         * get the view of current Item
         * @return the current item position
         */
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View newView = inflater.inflate(R.layout.recipe_result_row, parent, false);
            newView.findViewById(R.id.title);
            TextView text = newView.findViewById(R.id.title);
            ImageView imageView = newView.findViewById(R.id.resultRow_imageView);

            Picasso.get().load(favouritelist.get(position).getThumbnail()).into(imageView);
            text.setText(favouritelist.get(position).getTitle());
            return newView;

        }
    }

    /**
     * load Json data from url
     * @param  urlString is the specified URL
     */
    public void loadJsonfromURL(String urlString) {
        viewLoadIndicator=1;
        Jsonloader loader = new Jsonloader(this,"", mainlistView);
        loader.execute(urlString);
        Toast.makeText(this, R.string.toast_load, Toast.LENGTH_LONG).show();
    }


}