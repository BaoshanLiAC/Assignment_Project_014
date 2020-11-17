package com.ac.assignment_project_014.recipe;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ac.assignment_project_014.R;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

public class RecipeContentActivity extends AppCompatActivity {
    String title;// = fromRecipeMain.getStringExtra("title");
    String thumbnail;// = fromRecipeMain.getStringExtra("thumbnail");
    String ingredients;// = fromRecipeMain.getStringExtra("ingredients");
    String href;// = fromRecipeMain.getStringExtra("href");
    String like;// = fromRecipeMain.getStringExtra("like");
    SQLiteDatabase db;
    DBOpener dbOpener;// = new DBOpener(this);
    Intent fromRecipeMain;
    ImageButton imageButton_like;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_activity_recipe_content);
        fromRecipeMain = getIntent();
        dbOpener = new DBOpener(this);
        ImageView imageView = findViewById(R.id.Image_large);
        TextView textView_title = findViewById(R.id.textView_title);
        TextView textView_content = findViewById(R.id.textView_recipe);
        TextView textView_url = findViewById(R.id.textView_url);
        imageButton_like = findViewById(R.id.btn_like);

        title = fromRecipeMain.getStringExtra("title");
        thumbnail = fromRecipeMain.getStringExtra("thumbnail");
        ingredients = fromRecipeMain.getStringExtra("ingredients");
        href = fromRecipeMain.getStringExtra("href");
        like = fromRecipeMain.getStringExtra("like");

        Picasso.get().load(thumbnail).into(imageView);
        textView_title.setText(title);
        textView_content.setText(ingredients);
        textView_url.setText(href);
        imageButton_like.setImageResource(R.drawable.ic_baseline_favorite_border_24);

        if(like.equals("Y"))
            imageButton_like.setImageResource(R.drawable.ic_baseline_favorite_24);
        else
            imageButton_like.setImageResource(R.drawable.ic_baseline_favorite_border_24);

        textView_url.setOnClickListener((parent)->{
            Uri uri = Uri.parse(fromRecipeMain.getStringExtra("href"));
            Intent goToURL = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(goToURL);
        });

        //Toast.makeText(this, "Recipe content has been loaded", Toast.LENGTH_LONG).show();


        imageButton_like.setOnClickListener((parent)->{
            imageButton_like.setImageResource(R.drawable.ic_baseline_favorite_border_24);
            if( fromRecipeMain.getStringExtra("like").equals("Y") ){
                unlikeRecipe();
                Snackbar snackbar = Snackbar.make(imageButton_like,  "Undo: To keep in my favourite list" , Snackbar.LENGTH_LONG);
                snackbar.setAction("Undo", click->likeRecipe());
                snackbar.show();
            }
            else{
                likeRecipe();
                Snackbar snackbar = Snackbar.make(imageButton_like,  "Undo: To remove from my favourite list" , Snackbar.LENGTH_LONG);
                snackbar.setAction("Undo", click->unlikeRecipe());
                snackbar.show();
            }
        });

    }

/*    @Override
    protected int getLayoutId() {
        return R.layout.recipe_activity_recipe_content;
    }*/

    public void unlikeRecipe(){
        fromRecipeMain.removeExtra("like");
        fromRecipeMain.putExtra("like","N");
        imageButton_like.setImageResource(R.drawable.ic_baseline_favorite_border_24);
        removeRepipeItem(new JsonResults(title,href,ingredients,thumbnail));
    }


    public void likeRecipe (){
        fromRecipeMain.removeExtra("like");
        fromRecipeMain.putExtra("like","Y");
        imageButton_like.setImageResource(R.drawable.ic_baseline_favorite_24);
        removeRepipeItem(new JsonResults(title,href,ingredients,thumbnail));
        addRepipeItem(new JsonResults(title,href,ingredients,thumbnail));
    }

    public void removeRepipeItem(JsonResults res){
        db = dbOpener.getWritableDatabase();

        String [] columns = {dbOpener.COL_ID, dbOpener.COL_TITLE, dbOpener.COL_HREF, dbOpener.COL_INGREDIENT, dbOpener.COL_THUMBNAIL};
        Cursor results = db.query(false, dbOpener.TABLE_NAME, columns, null, null, null, null, null, null);
        int href_index = results.getColumnIndex(dbOpener.COL_HREF);

        while(results.moveToNext())
            if(results.getString(href_index).equals(res.getHref()))
                db.delete(dbOpener.TABLE_NAME,"HREF=?",new String[]{res.getHref()});
    }


    public void addRepipeItem(JsonResults res){
        db = dbOpener.getWritableDatabase();

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
}