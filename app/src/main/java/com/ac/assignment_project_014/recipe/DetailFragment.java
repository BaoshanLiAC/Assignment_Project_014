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
import com.ac.assignment_project_014.covid19.CovidDrawerBase;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;


public class DetailFragment extends Fragment {

    private Bundle dataFromActivity;
    private AppCompatActivity parentActivity;
    String title,thumbnail,ingredients,href;
    Boolean isLike;
    ImageButton imageButton;
    SQLiteDatabase db;
    DBOpener dbOpener;
    private AlertDialog alertDialog;
    TextView textView_Url;

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
                Snackbar snackbar = Snackbar.make(imageButton,  "Undo: To keep in my favourite list" , Snackbar.LENGTH_LONG);
                snackbar.setAction("Undo", click->likeRecipe());
                snackbar.show();
            }
            else{
                likeRecipe();
                Snackbar snackbar = Snackbar.make(imageButton,  "Undo: To remove from my favourite list" , Snackbar.LENGTH_LONG);
                snackbar.setAction("Undo", click->unlikeRecipe());
                snackbar.show();
            }
        });

        return result;
    }


    public void unlikeRecipe(){
        dataFromActivity.remove(RecipeMainActivity.ISLIKE);
        dataFromActivity.putBoolean(RecipeMainActivity.ISLIKE,false);
        imageButton.setImageResource(R.drawable.ic_baseline_favorite_border_24);
        removeRepipeItem(new JsonResults(title,href,ingredients,thumbnail));
    }


    public void likeRecipe (){
        dataFromActivity.remove(RecipeMainActivity.ISLIKE);
        dataFromActivity.putBoolean(RecipeMainActivity.ISLIKE,true);
        imageButton.setImageResource(R.drawable.ic_baseline_favorite_24);
        addRepipeItem(new JsonResults(title,href,ingredients,thumbnail));
    }


    public void removeRepipeItem(JsonResults res){
        db = RecipeMainActivity.dbOpener.getWritableDatabase();
        String [] columns = {dbOpener.COL_ID, dbOpener.COL_TITLE, dbOpener.COL_HREF, dbOpener.COL_INGREDIENT, dbOpener.COL_THUMBNAIL};
        Cursor results = db.query(false, dbOpener.TABLE_NAME, columns, null, null, null, null, null, null);
        int href_index = results.getColumnIndex(dbOpener.COL_HREF);
        while(results.moveToNext())
            if(results.getString(href_index).equals(res.getHref()))
                db.delete(dbOpener.TABLE_NAME,"HREF=?",new String[]{res.getHref()});
    }


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


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parentActivity = (AppCompatActivity)context;
    }
}
