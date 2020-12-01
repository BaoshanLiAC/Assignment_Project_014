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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.ac.assignment_project_014.R;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * This Class is the page to display the details of an Album.
 * <p>
 * <o>and retrieve data from database</>
 * @author Baoshan Li
 */
public class AlbumDetailFragment  extends Fragment {
    /**
     * the current album id
     */
    private String albumId;
    /**
     * the current album object
     */
    private AlbumItem currentAlbum;
    /**
     * the album content
     */
    private TextView text_album;
    /**
     * the album image
     */
    private ImageView img_album;
    /**
     * the album image url
     */
    private String gURL;
    /**
     * define current TrackItem list
     */
    private  ArrayList<TrackItem> tlist;
    /**
     * define current list view
     */
    private ListView listView;
    /**
     * define button
     */
    private Button btnStore;
    /**
     * define database opener
     */
    private AudioOpener dbOpener;
    /**
     * define database opener
     */
    private SQLiteDatabase sqldb;

    /**
     * To get the current Album
     */
    public void setCurrentAlbum(AlbumItem item) {
        this.currentAlbum = item;
    }

    /**
     * To get the current Album list
     */
    public ArrayList<TrackItem> getTlist(){
        if(this.tlist==null)
            tlist = new ArrayList<TrackItem>();

        return this.tlist;
    }


    /**
     * Load the current view
     * <p>
     * @param savedInstanceState the State of current activity is saved in this parameter
     * @param inflater to load the xml view
     * @param container contain other children views
     *
     */
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

                    if (btnStore.getText().equals(this.getString(R.string.audio_save))) {
                        this.saveLocalAlbum(this.currentAlbum);
                        btnStore.setText(this.getString(R.string.audio_remove));
                        Snackbar.make(listView, this.getString(R.string.audio_remove), Snackbar.LENGTH_LONG).show();


                    } else if (btnStore.getText().equals(this.getString(R.string.audio_remove))) {
                        this.removeLocalAlbum(this.currentAlbum);
                        btnStore.setText(this.getString(R.string.audio_save));
                        Snackbar.make(listView, this.getString(R.string.audio_save), Snackbar.LENGTH_LONG).show();
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
                    btnStore.setText(this.getString(R.string.audio_remove));
                else
                    //btnStore.setImageResource(R.drawable.ic_star_line);
                    btnStore.setText(this.getString(R.string.audio_save));
            }

        return view;
    }


    /**
     * Control the visible of current fragment
     * <p>
     * @param hidden to control the visible of this fragment
     *
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if(currentAlbum!=null) {
                //refresh whole page

                if(this.checkSave())
                    btnStore.setText(this.getString(R.string.audio_remove));
                else
                    btnStore.setText(this.getString(R.string.audio_save));

                gURL = "https://theaudiodb.com/api/v1/json/1/track.php?m=" + currentAlbum.getAlbumId();
                new fetchTrack().execute();
                text_album.setText(currentAlbum.getAlbumName() + " / " + currentAlbum.getArtistName());
                new DownloadImageHelper(img_album).execute(currentAlbum.getAlbumImgUrl());
            }

        }
    }

    /**
     * connect DataBase
     */
    private void connDataBase(){
        if(dbOpener==null) {
            dbOpener=new AudioOpener(getActivity());
        }

        if(sqldb==null) {
            sqldb= dbOpener.getWritableDatabase();
        }

    }


    /**
     * To get all of the columns.
     * <p>
     * Look at AudioOpener.java for the definitions
     * query all the results from the database
     */
    private boolean checkSave(){

        connDataBase();
        String [] columns = {AudioOpener.COL_ALBUMID, AudioOpener.COL_ALBUMNAME,
                AudioOpener.COL_ARTIST, AudioOpener.COL_ALBUMIMGURL, AudioOpener.COL_ALBUMSTYLE};


        Cursor results = sqldb.query("ALBUM_TABLE", new String[]{"_albumid,albumName,artist,imgURL,Style"},
                "_albumid = ?", new String[]{currentAlbum.getAlbumId()}, null, null, null, null);

        if(results.getCount()>0)
            return true;
        else
            return false;
    }


    /**
     * Now provide a value for every database column defined in MyOpener.java.
     */
    private void saveLocalAlbum(AlbumItem saveItem){
        connDataBase();

        ContentValues newRowValues = new ContentValues();
        newRowValues.put(dbOpener.COL_ALBUMID, saveItem.getAlbumId());
        newRowValues.put(dbOpener.COL_ALBUMIMGURL, saveItem.getAlbumImgUrl());
        newRowValues.put(dbOpener.COL_ALBUMNAME, saveItem.getAlbumName());
        newRowValues.put(dbOpener.COL_ARTIST, saveItem.getArtistName());
        newRowValues.put(dbOpener.COL_ALBUMSTYLE, saveItem.getAlbumStyle());
        sqldb.insert(dbOpener.TABLE_NAME_A, null, newRowValues);

    }


    /**
     * remove the album from database
     * @param delItem delItem
     */
    private void removeLocalAlbum(AlbumItem delItem){
        connDataBase();
        sqldb.delete(dbOpener.TABLE_NAME_A,"_albumid=?",new String[]{delItem.getAlbumId()});
    }


    /**
     * load data into ArrayList favourite list, and displayed on the page
     */
    class TrackAdapter extends BaseAdapter {
        private List<TrackItem> trackList;
        private LayoutInflater inflater;
        private TextView textview_track;

        public TrackAdapter(){

        }
        /**
         * constructor with one parameter
         * @param  ti, the current list
         */
        public TrackAdapter(List<TrackItem> ti) {
            trackList=ti;
        }

        /**
         * get the total item
         * @return the count number
         */
    @Override
    public int getCount() {
        return trackList.size();
    }
        /**
         * get the current item
         * @return the current item object
         */
    @Override
    public Object getItem(int position) {
        return trackList.get(position);
    }
        /**
         * get the current item id
         * @param position, the current item position
         * @return the current item ID
         */
    @Override
    public long getItemId(int position) {
        return position;
    }
        /**
         * get the view of current Item
         * @param position, the current item position
         * @param convertView
         * @param parent this parent contains the children view
         * @return the current view
         */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = getLayoutInflater();

        View newView = inflater.inflate(R.layout.audio_tracklistview, null);
        textview_track = (TextView) newView.findViewById(R.id.textview_track);

        TrackItem item = trackList.get(position);
        textview_track.setText(item.getTrackName() + " / "+ item.getArtistName() + " / "+ item.getTrackGenre());

        return newView;

    }

}

    /**
     * This class is used to load json data from remote server .
     */
    public class fetchTrack extends AsyncTask<String, String, String> {
        /**
         * set the progressbar during the PreExecute stage
         */
        @Override
        public void onPreExecute() {
            super.onPreExecute();
        }

        /**
         * the thread processed in the backed to download album detail
         * @param params is String type array
         */
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

        /**
         * Update the UI after data loaded
         * @param s is the url
         */
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
                    Toast.makeText(getActivity(),R.string.toast1,Toast.LENGTH_SHORT).show();
                    //Toast.makeText(this,"Go search by country and date.", Toast.LENGTH_LONG).show();
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