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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ac.assignment_project_014.R;

import java.util.ArrayList;
import java.util.List;

public class LocalAlbumListFragment  extends Fragment {


    private AudioAdapter Adapter = null;


    private ArrayList<AlbumItem> beanArrayList = new ArrayList<>();


    private static int ACTIVITY_VIEW_CONTACT = 33;
    private  ArrayList<AlbumItem> alist;
    int positionClicked = 0;
    private AudioOpener dbOpener;
    private SQLiteDatabase sqldb;
    private EditText text_search;
    private ListView listView;
    private Button btn_search;
    private String gCondition="";

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


    @Override
    public void onHiddenChanged(boolean hidden) {

        super.onHiddenChanged(hidden);
        if (!hidden) {

         this.refreshList();
        }

    }


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



    private void connDataBase(){
        if(dbOpener==null) {
            dbOpener=new AudioOpener(getActivity());
        }

        if(sqldb==null) {
            sqldb= dbOpener.getWritableDatabase();
        }

    }



    private void loadLocalAlbum(String condition)
    {
        this.connDataBase();
        // We want to get all of the columns. Look at AudioOpener.java for the definitions:
        String [] columns = {AudioOpener.COL_ALBUMID, AudioOpener.COL_ALBUMNAME,
                AudioOpener.COL_ARTIST, AudioOpener.COL_ALBUMIMGURL,AudioOpener.COL_ALBUMSTYLE};
        //query all the results from the database:
        //Cursor results = db.query(false, AudioOpener.TABLE_NAME, columns, null, null, null, null, null, null);
        String selection="";
        Cursor results;
        if(!condition.equals("")&&condition!=null){
            results = sqldb.query("ALBUM_TABLE", new String[]{"_albumid,albumName,artist,imgURL,Style"},
                    "artist = ? or albumName=?", new String[]{text_search.getText().toString(), text_search.getText().toString()}, null, null, null, null);
        } else {
            results = sqldb.query("ALBUM_TABLE", new String[]{"_albumid,albumName,artist,imgURL,Style"},
                    null, null, null, null, null, null);
        }


       // Cursor results = sqldb.query("ALBUM_TABLE", new String[]{"_albumid,albumName,artist,imgURL,Style"},
        //        selection, new String[]{text_search.getText().toString(), text_search.getText().toString()}, null, null, null, null);

//


        //Now the results object has rows of results that match the query.
        //find the column indices:
        int idIndex = results.getColumnIndex(AudioOpener.COL_ALBUMID);
        int nameIndex = results.getColumnIndex(AudioOpener.COL_ALBUMNAME);
        int aritstIndex = results.getColumnIndex(AudioOpener.COL_ARTIST);
        int urlIndex = results.getColumnIndex(AudioOpener.COL_ALBUMIMGURL);
        int styleIndex = results.getColumnIndex(AudioOpener.COL_ALBUMSTYLE);

        this.alist.clear();
        //iterate over the results, return true if there is a next item:
        while(results.moveToNext())
        {

           AlbumItem insertItem=new AlbumItem(results.getString(0),results.getString(nameIndex),
                   results.getString(aritstIndex),results.getString(urlIndex),results.getString(styleIndex));
           this.alist.add(insertItem);
        }

        //At this point, the contactsList array has loaded every row from the cursor.

       // printCursor( results,db.getVersion());
    }






    class AudioAdapter extends BaseAdapter {// implements Filterable

        private Activity activity;
        private List<AlbumItem> albumList;
        private LayoutInflater inflater;

        private TextView textview_album, textview_artist;
        private ImageView img_album;
        public AudioAdapter(){

        }

        public AudioAdapter(List<AlbumItem> ai) {
            // super();
            albumList=ai;

            //  inflater = (LayoutInflater) activity.getSystemService((Context.LAYOUT_INFLATER_SERVICE));
        }

        @Override
        public int getCount() {
            return albumList.size();
        }

        @Override
        public Object getItem(int position) {
            return albumList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //return convertView;
            //CheckBox b1=new CheckBox(parent);

            //Button b=new Button(parent);
            //b.setText("ABC");


            if (inflater == null)
                inflater = getLayoutInflater();
            //inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            //if (convertView == null)
            // convertView = inflater.inflate(R.layout.audio_listview, null);

            View newView = inflater.inflate(R.layout.audio_listview, null);
            textview_album = (TextView) newView.findViewById(R.id.textview_album);
            //textview_artist = (TextView) newView.findViewById(R.id.textview_artist);

            img_album=(ImageView) newView.findViewById(R.id.img_album);
            //labCase = (img_album) convertView.findViewById(R.id.img_album);

            AlbumItem item = albumList.get(position);
            textview_album.setText(item.getAlbumName());
            new com.ac.assignment_project_014.audio.DownloadImageHelper(img_album).execute(item.getAlbumImgUrl());


            return newView;




        }


    }


}


