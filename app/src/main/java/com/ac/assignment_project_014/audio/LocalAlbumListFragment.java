package com.ac.assignment_project_014.audio;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ac.assignment_project_014.R;

import java.util.ArrayList;
import java.util.List;
/**
 * This Class is the page to display the Album list.

 * @author Baoshan Li
 */
public class LocalAlbumListFragment  extends Fragment {


    /**
     * the AlbumItem List
     */
    private ArrayList<AlbumItem> alist;
    /**
     * the database Opener.
     */
    private AudioOpener dbOpener;
    /**
     * the database handler.
     */
    private SQLiteDatabase sqldb;
    /**
     * the EditText to input the search key words
     */
    private EditText text_search;
    /**
     * the listView
     */
    private ListView listView;
    /**
     * the search button
     */
    private ImageButton btn_search;
    /**
     * the search key words
     */
    private String gCondition="";
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
        View view=inflater.inflate(R.layout.audio_fragment_localalbum,container,false);
        listView = view.findViewById(R.id.list_album);
        text_search=view.findViewById(R.id.text_search);
        btn_search= view.findViewById(R.id.btn_search);
        btn_search.setOnClickListener( click -> {
            this.gCondition=text_search.getText().toString();
            this.refreshList();
        });

        alist=new ArrayList<AlbumItem>();
        this.refreshList();

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

         this.refreshList();
        }

    }

    /**
     * reload the Album list
     */
    private void refreshList(){

        loadLocalAlbum(this.gCondition);
        AudioAdapter arrayAdapter = new AudioAdapter(alist);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ((AudioIndexActivity)getActivity()).showAlbumFragment(alist.get(position));

            }
        });
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
     * reload the Album from database by specified condition
     * @param condition the keywords
     */
    private void loadLocalAlbum(String condition)
    {
        this.connDataBase();
        // We want to get all of the columns. Look at AudioOpener.java for the definitions:
        String [] columns = {AudioOpener.COL_ALBUMID, AudioOpener.COL_ALBUMNAME,
                AudioOpener.COL_ARTIST, AudioOpener.COL_ALBUMIMGURL, AudioOpener.COL_ALBUMSTYLE};
        //query all the results from the database:
        //Cursor results = db.query(false, AudioOpener.TABLE_NAME, columns, null, null, null, null, null, null);
        String selection="";
        Cursor results;
        if(!condition.equals("")&&condition!=null){
            results = sqldb.query("ALBUM_TABLE", new String[]{"_albumid,albumName,artist,imgURL,Style"},
                    "albumName like ?", new String[]{"%"+text_search.getText().toString()+"%"},
                    null, null, null, null);
        } else {
            results = sqldb.query("ALBUM_TABLE", new String[]{"_albumid,albumName,artist,imgURL,Style"},
                    null, null, null, null, null, null);
        }

        int idIndex = results.getColumnIndex(AudioOpener.COL_ALBUMID);
        int nameIndex = results.getColumnIndex(AudioOpener.COL_ALBUMNAME);
        int aritstIndex = results.getColumnIndex(AudioOpener.COL_ARTIST);
        int urlIndex = results.getColumnIndex(AudioOpener.COL_ALBUMIMGURL);
        int styleIndex = results.getColumnIndex(AudioOpener.COL_ALBUMSTYLE);

        if(this.alist!=null)
        this.alist.clear();
        //iterate over the results, return true if there is a next item:
        while(results.moveToNext())
        {

           AlbumItem insertItem=new AlbumItem(results.getString(0),results.getString(nameIndex),
                   results.getString(aritstIndex),results.getString(urlIndex),results.getString(styleIndex));
           this.alist.add(insertItem);
        }

    }


    /**
     * The adapter to bind data to listview
     */
    class AudioAdapter extends BaseAdapter {

        private List<AlbumItem> albumList;
        private LayoutInflater inflater;

        private TextView textview_album, textview_artist;
        private ImageView img_album;

        /**
         * constructor with one parameter
         * @param  ai, the current list
         */
        public AudioAdapter(List<AlbumItem> ai) {
            albumList=ai;
        }
        /**
         * get the total album
         * @return the count number
         */
        @Override
        public int getCount() {
            return albumList.size();
        }
        /**
         * get the current item
         * @return the current item object
         */
        @Override
        public Object getItem(int position) {
            return albumList.get(position);
        }
        /**
         * get the current item id
         * @return the current item position
         */
        @Override
        public long getItemId(int position) {
            return position;
        }
        /**
         * get the view of current Item
         * @return the current view
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (inflater == null)
                inflater = getLayoutInflater();

            View newView = inflater.inflate(R.layout.audio_listview, null);
            textview_album = (TextView) newView.findViewById(R.id.textview_album);
            img_album=(ImageView) newView.findViewById(R.id.img_album);
            AlbumItem item = albumList.get(position);
            textview_album.setText(item.getAlbumName());
            new DownloadImageHelper(img_album).execute(item.getAlbumImgUrl());

            return newView;
        }


    }


}


