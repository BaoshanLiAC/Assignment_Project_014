package com.ac.assignment_project_014.audio;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ac.assignment_project_014.R;
import com.ac.assignment_project_014.recipe.RecipeMainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AlbumDetailFragment  extends Fragment {
    private String albumId;
    private AlbumItem currentAlbum;
    private TextView text_album;
    private ImageView img_album;
    private String gURL;
    private  ArrayList<TrackItem> tlist;
    private ListView listView;
    private Button btnStore;
    private AudioOpener dbOpener;
    private SQLiteDatabase sqldb;


    public void setCurrentAlbum(AlbumItem item) {
        this.currentAlbum = item;
    }

    public ArrayList<TrackItem> getTlist(){
        if(this.tlist==null)
            tlist = new ArrayList<TrackItem>();

        return this.tlist;
    }

  /*  private ListView getListView(){
        if(listView==null) {
            View view=inflater.inflate(R.layout.audio_fragment_album_detail,container,false);
            listView = view.findViewById(R.id.list_track);
        }

    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.audio_fragment_album_detail,container,false);

        //if(this.currentAlbum!=null){
            text_album=view.findViewById(R.id.text_album);
            img_album = (ImageView) view.findViewById(R.id.img_album);
            listView = view.findViewById(R.id.list_track);
            btnStore=view.findViewById(R.id.btn_store);

            btnStore.setOnClickListener( click -> {
                if(currentAlbum!=null) {



                    if (btnStore.getText().equals("Save")) {
                        this.saveLocalAlbum(this.currentAlbum);
                        btnStore.setText("Remove");
                    } else if (btnStore.getText().equals("Remove")) {
                        this.removeLocalAlbum(this.currentAlbum);
                        btnStore.setText("Save");
                    }
                } else
                    btnStore.setText("Please select one album.");
            });

            if(currentAlbum!=null) {
                text_album.setText(currentAlbum.getAlbumName() + " / " + currentAlbum.getArtistName());

                new DownloadImageHelper(img_album).execute(currentAlbum.getAlbumImgUrl());

                gURL = "https://theaudiodb.com/api/v1/json/1/track.php?m=" + currentAlbum.getAlbumId();

                new fetchTrack().execute();

                if(this.checkSave())
                    //btnStore.setImageResource(R.drawable.ic_star);
                    btnStore.setText("Remove");
                else
                    //btnStore.setImageResource(R.drawable.ic_star_line);
                    btnStore.setText("Save");
            }
   // }



       // }

        return view;
    }



    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if(currentAlbum!=null) {
                //refresh whole page

                if(this.checkSave())
                    btnStore.setText("Remove");
                else
                    btnStore.setText("Save");

                gURL = "https://theaudiodb.com/api/v1/json/1/track.php?m=" + currentAlbum.getAlbumId();
                new fetchTrack().execute();
                text_album.setText(currentAlbum.getAlbumName() + " / " + currentAlbum.getArtistName());
                new DownloadImageHelper(img_album).execute(currentAlbum.getAlbumImgUrl());


            }

        }
    }

    private void connDataBase(){
        if(dbOpener==null) {
            dbOpener=new AudioOpener(getActivity());
        }

        if(sqldb==null) {
            sqldb= dbOpener.getWritableDatabase();
        }

    }

    private boolean checkSave(){

        //AudioIndexActivity activity = (AudioIndexActivity) getActivity();
        //get a database connection:
        connDataBase();
        // We want to get all of the columns. Look at AudioOpener.java for the definitions:
        String [] columns = {AudioOpener.COL_ALBUMID, AudioOpener.COL_ALBUMNAME,
                AudioOpener.COL_ARTIST, AudioOpener.COL_ALBUMIMGURL, AudioOpener.COL_ALBUMSTYLE};
        //query all the results from the database:
        //Cursor results = db.query(false, AudioOpener.TABLE_NAME, columns, null, null, null, null, null, null);

        Cursor results = sqldb.query("ALBUM_TABLE", new String[]{"_albumid,albumName,artist,imgURL,Style"},
                "_albumid = ?", new String[]{currentAlbum.getAlbumId()}, null, null, null, null);

        if(results.getCount()>0)
            return true;
        else
            return false;
    }

    private void saveLocalAlbum(AlbumItem saveItem){
        connDataBase();

        ContentValues newRowValues = new ContentValues();

        //Now provide a value for every database column defined in MyOpener.java:
        newRowValues.put(dbOpener.COL_ALBUMID, saveItem.getAlbumId());
        newRowValues.put(dbOpener.COL_ALBUMIMGURL, saveItem.getAlbumImgUrl());
        newRowValues.put(dbOpener.COL_ALBUMNAME, saveItem.getAlbumName());
        newRowValues.put(dbOpener.COL_ARTIST, saveItem.getArtistName());
        newRowValues.put(dbOpener.COL_ALBUMSTYLE, saveItem.getAlbumStyle());
        sqldb.insert(dbOpener.TABLE_NAME_A, null, newRowValues);

    }

    private void removeLocalAlbum(AlbumItem delItem){
        connDataBase();
        sqldb.delete(dbOpener.TABLE_NAME_A,"_albumid=?",new String[]{delItem.getAlbumId()});
    }







    private void saveTrack(){


    }



  /*  @Override
    public void onHiddenChanged(boolean hidden) {
        if (hidden){
            text_album.setText(currentAlbum.getAlbumName()+" / "+currentAlbum.getArtistName());
            new DownloadImageHelper(img_album).execute(currentAlbum.getAlbumImgUrl());
        }
        super.onHiddenChanged(hidden);
    }*/



    class TrackAdapter extends BaseAdapter {// implements Filterable

        private Activity activity;
        private List<TrackItem> trackList;
        private LayoutInflater inflater;

        private TextView textview_track;

        public TrackAdapter(){

        }

        public TrackAdapter(List<TrackItem> ti) {
            // super();
            trackList=ti;

            //  inflater = (LayoutInflater) activity.getSystemService((Context.LAYOUT_INFLATER_SERVICE));
        }


    @Override
    public int getCount() {
        return trackList.size();
    }

    @Override
    public Object getItem(int position) {
        return trackList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = getLayoutInflater();
        //inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //if (convertView == null)
        // convertView = inflater.inflate(R.layout.audio_listview, null);

        View newView = inflater.inflate(R.layout.audio_tracklistview, null);
        textview_track = (TextView) newView.findViewById(R.id.textview_track);

        TrackItem item = trackList.get(position);
        textview_track.setText(item.getTrackName() + " / "+ item.getArtistName() + " / "+ item.getTrackGenre());

        return newView;

    }


}


    public class fetchTrack extends AsyncTask<String, String, String> {

        @Override
        public void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            String result = null;
            if(gURL!=null ) {

                getTlist().clear();

                try {
                    URL url = new URL(gURL);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();

                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        InputStreamReader inputStreamReader = new InputStreamReader(conn.getInputStream());
                        BufferedReader reader = new BufferedReader(inputStreamReader);
                        StringBuilder stringBuilder = new StringBuilder();
                        String temp;

                        while ((temp = reader.readLine()) != null) {
                            stringBuilder.append(temp);
                        }
                        result = stringBuilder.toString();
                    } else {
                        result = "error";
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return result;
        }

        @Override
        public void onPostExecute(String s) {
            super .onPostExecute(s);
            //swipeRefresh.setRefreshing(false);
            try {
                JSONObject object = new JSONObject(s);
                JSONArray array = object.getJSONArray("track");

                for (int i = 0; i < array.length(); i++) {

                    JSONObject jsonObject = array.getJSONObject(i);
                    String idTrack = jsonObject.getString("idTrack");
                    String idAlbum = jsonObject.getString("idAlbum");
                    String strTrack = jsonObject.getString("strTrack");
                    String strArtist = jsonObject.getString("strArtist");
                    String strGenre = jsonObject.getString("strGenre");

                    //todo:
                    TrackItem model = new TrackItem(idAlbum,idTrack,strTrack,strArtist,strGenre);
                    getTlist().add(model);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            TrackAdapter trackAdapter = new TrackAdapter(getTlist());
            listView.setAdapter(trackAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    ((AudioIndexActivity)getActivity()).showGoogleFragment(getTlist().get(position));

                }
            });

        }
    }



}