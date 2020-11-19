package com.ac.assignment_project_014.recipe;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Jsonloader extends AsyncTask<String,Integer,String> {
    /**
     * retrieve the favourite list
     */
    static public String jsonString = "";
    /**
     * the current Activity context
     */
    Context context;
    /**
     * the url used to retrieve json objects
     */
    String jsonURL;
    /**
     * used to hold the recipe list
     */
    ListView recopelistView;

    /**
     * constructor, to set the current context, url and the listview wait to update
     * @param context the current context
     * @param jsonURL is concatenated with user input keywords
     * @param listView the current listView to be updated
     */
        public Jsonloader(Context context, String jsonURL, ListView listView){
            this.context = context;
            this.jsonURL = jsonURL;
            this.recopelistView = listView;
        }

    /**
     * set the progressbar during the PreExecute stage
     * @param args is String type array
     */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            RecipeMainActivity.progressbar.setVisibility(View.VISIBLE);

        }

        //Type 1

    /**
     * the thread processed in the backed
     * @param args is String type array
     */
        @Override
        protected String doInBackground(String... args) {
            for(int i = 0; i< 4; i++){
                publishProgress((i*100) / 4); //Type2
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            try{
                RecipeMainActivity.searchResultsList.clear();
                //create a URL object of what server to contact:
                URL url = new URL(args[0]);
                //open the connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                //wait for data:
                InputStream response = urlConnection.getInputStream();

                //InputStream response = new BufferedInputStream(urlConnection.getInputStream());
                if (urlConnection.getResponseCode() == urlConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);

                    String line;
                    StringBuffer jsonData = new StringBuffer();

                    //READ
                    while ((line = reader.readLine()) != null)
                        jsonData.append(line + "\n");
                    //CLOSE RESOURCES
                    reader.close();
                    response.close();
                    JSONArray jsonArray;
                    if(jsonData.toString()!=""){
                        jsonString = jsonData.toString();
                        JSONObject jsonObjectobj = new JSONObject(jsonString);
                        jsonArray = jsonObjectobj.getJSONArray("results");
                        String title,href,ingredients,thumbnail;
                        for (int i=0; i < jsonArray.length(); i++){
                            JSONObject anObject = jsonArray.getJSONObject(i);
                            // Pulling items from the array
                            title = anObject.getString("title");
                            //anObject.getBoolean(booleanName);
                            href = anObject.getString("href");
                            ingredients = anObject.getString("ingredients");
                            thumbnail= anObject.getString("thumbnail");
                            RecipeMainActivity.searchResultsList.add(new JsonResults(title,href,ingredients,thumbnail));
                        }
                    }
                    //listView.setAdapter(adapter1);
                    return jsonString;
                } else
                    return "Error" + urlConnection.getResponseMessage();

            }catch (IOException | JSONException e) {
                e.printStackTrace();
                return "Error" + e.getMessage();
            }
        }

    /**
     * the progressbar status update while the thread processed in the backed
     * @param integers is Integer type array
     */
        @Override
        protected void onProgressUpdate(Integer... integers) {
            super.onProgressUpdate(integers);
            RecipeMainActivity.progressbar.setProgress(integers[0]);
        }

     /**
     * the progressbar status updated when the thread is finished
     * @param jsonString is the url
     */
        @Override //Type 3
        protected void onPostExecute(String jsonString) {
            super.onPostExecute(jsonString);
            RecipeMainActivity.progressbar.setProgress(75);
            RecipeMainActivity.progressbar.setVisibility(View.INVISIBLE);
            if(RecipeMainActivity.searchResultsList !=null)
                RecipeMainActivity.mainlistView.setAdapter(RecipeMainActivity.myResultAdapter);

        }

    }

