package com.ac.assignment_project_014.recipe;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.ac.assignment_project_014.R;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

/**
 * This Class is the page to display the Detailed recipe page.
 * <p>
 * <o>receive the data from empty, </>
 * <o>and retrieve data from database</>
 * @author chunyan ren
 */
public class DetailFragment extends Fragment {
    /**
     * data passed from previous activity
     */
    private Bundle dataFromActivity;
    /**
     * the parent activity
     */
    private AppCompatActivity parentActivity;
    /**
     * the title,image,ingredients and url of the current recipe
     */
    String title,thumbnail,ingredients,href;
    /**
     * if this recipe is saved in the favourite recipe
     */
    Boolean isLike;
    /**
     * the button used to dispaly and response to the add to favourite list action
     */
    ImageButton imageButton;
    /**
     * used to connect the database
     */
    SQLiteDatabase db;
    /**
     * used to access the database
     */
    DBOpener dbOpener;


    /**
     * called when this fragment is loaded
     * @param inflater to load the view xml
     * @param container the parent view contains children views
     * @param savedInstanceState the current saved stated
     * */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dataFromActivity = getArguments();
        dbOpener = new DBOpener(parentActivity);

        View result =  inflater.inflate(R.layout.fragment_detail, container, false);

        ImageView image = result.findViewById(R.id.image_large);
        thumbnail = dataFromActivity.getString(RecipeMainActivity.THUMBNAIL);
        Picasso.get().load(thumbnail).into(image);

        TextView titleView = (TextView)result.findViewById(R.id.textView_title);
        title = dataFromActivity.getString(RecipeMainActivity.TITLE);
        titleView.setText(title);

        imageButton = result.findViewById(R.id.btn_like);
        isLike = dataFromActivity.getBoolean(RecipeMainActivity.ISLIKE);
        if(isLike)
            imageButton.setImageResource(R.drawable.ic_baseline_favorite_24);
        else
            imageButton.setImageResource(R.drawable.ic_baseline_favorite_border_24);

        TextView textView_Recipe = result.findViewById(R.id.textView_recipe);
        ingredients = dataFromActivity.getString(RecipeMainActivity.INGREDIENTS);
        textView_Recipe.setText(ingredients);

        TextView textView_URL = result.findViewById(R.id.textView_url);
        href = dataFromActivity.getString(RecipeMainActivity.HREF);
        textView_URL.setText(href);


        imageButton.setOnClickListener((parent)->{
            if( dataFromActivity.getBoolean(RecipeMainActivity.ISLIKE) ){
                unlikeRecipe();
                Snackbar snackbar = Snackbar.make(imageButton,  R.string.snackbar_keep , Snackbar.LENGTH_LONG);
                snackbar.setAction("Undo", click->likeRecipe());
                snackbar.show();
            }
            else{
                likeRecipe();
                Snackbar snackbar = Snackbar.make(imageButton,  R.string.snackbar_remove , Snackbar.LENGTH_LONG);
                snackbar.setAction("Undo", click->unlikeRecipe());
                snackbar.show();
            }
        });

        return result;
    }

    /**
     * to process the "unlike" the recipe
     * */
    public void unlikeRecipe(){
        dataFromActivity.remove(RecipeMainActivity.ISLIKE);
        dataFromActivity.putBoolean(RecipeMainActivity.ISLIKE,false);
        imageButton.setImageResource(R.drawable.ic_baseline_favorite_border_24);
        removeRepipeItem(new JsonResults(title,href,ingredients,thumbnail));
    }

    /**
     * to process the "like" the recipe
     * */
    public void likeRecipe (){
        dataFromActivity.remove(RecipeMainActivity.ISLIKE);
        dataFromActivity.putBoolean(RecipeMainActivity.ISLIKE,true);
        imageButton.setImageResource(R.drawable.ic_baseline_favorite_24);
        addRepipeItem(new JsonResults(title,href,ingredients,thumbnail));
    }

    /**
     * to remove the recipe Item from favourite list
     * @param res is an object of JsonResults
     *
     * */
    public void removeRepipeItem(JsonResults res){
        db = RecipeMainActivity.dbOpener.getWritableDatabase();
        String [] columns = {dbOpener.COL_ID, dbOpener.COL_TITLE, dbOpener.COL_HREF, dbOpener.COL_INGREDIENT, dbOpener.COL_THUMBNAIL};
        Cursor results = db.query(false, dbOpener.TABLE_NAME, columns, null, null, null, null, null, null);
        int href_index = results.getColumnIndex(dbOpener.COL_HREF);
        while(results.moveToNext())
            if(results.getString(href_index).equals(res.getHref()))
                db.delete(dbOpener.TABLE_NAME,"HREF=?",new String[]{res.getHref()});
    }

    /**
     * to add a recipe item into favourite list
     * @param res is an object of JsonResults
     *
     * */
    public void addRepipeItem(JsonResults res){
        db = RecipeMainActivity.dbOpener.getWritableDatabase();
        String [] columns = {dbOpener.COL_ID, dbOpener.COL_TITLE, dbOpener.COL_HREF, dbOpener.COL_INGREDIENT, dbOpener.COL_THUMBNAIL};
        Cursor results = db.query(false, dbOpener.TABLE_NAME, columns, null, null, null, null, null, null);
        int href_index = results.getColumnIndex(dbOpener.COL_HREF);
        //delete item if cancelled
      while(results.moveToNext())
            if(results.getString(href_index).equals(res.getHref()))
                return;
        //update database
        ContentValues newRowValues = new ContentValues();
        newRowValues.put(dbOpener.COL_TITLE, res.getTitle());
        newRowValues.put(dbOpener.COL_HREF, res.getHref());
        newRowValues.put(dbOpener.COL_INGREDIENT, res.getIngredients());
        newRowValues.put(dbOpener.COL_THUMBNAIL, res.getThumbnail());
        long newId = db.insert(dbOpener.TABLE_NAME, null, newRowValues);
    }

    /**
     * @param context is the current activity context
     *
     * */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parentActivity = (AppCompatActivity)context;
    }
}
