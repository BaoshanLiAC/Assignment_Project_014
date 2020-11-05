package com.ac.assignment_project_014.recipe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ac.assignment_project_014.R;
import com.ac.assignment_project_014.recipe.JsonEntityClass.Results;
import com.squareup.picasso.Picasso;

public class RecipeContentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_content);

        Intent fromRecipeMain = getIntent();

        ImageView imageView = findViewById(R.id.Image_large);
        Picasso.get().load(fromRecipeMain.getStringExtra("thumbnail")).into(imageView);

        TextView textView_title = findViewById(R.id.textView_title);
        textView_title.setText(fromRecipeMain.getStringExtra("title"));

        TextView textView_content = findViewById(R.id.textView_recipe);
        textView_content.setText(fromRecipeMain.getStringExtra("ingredients"));

        TextView textView_url = findViewById(R.id.textView_url);
        textView_url.setText(fromRecipeMain.getStringExtra("href"));

        ImageButton imageButton_like = findViewById(R.id.btn_like);
        if(fromRecipeMain.getStringExtra("like").equals("Y"))
            imageButton_like.setImageResource(R.drawable.ic_baseline_favorite_24);
        else
            imageButton_like.setImageResource(R.drawable.ic_baseline_favorite_border_24);

        textView_url.setOnClickListener((parent)->{
            Uri uri = Uri.parse(fromRecipeMain.getStringExtra("href"));
            Intent goToURL = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(goToURL);
        });



    }
}