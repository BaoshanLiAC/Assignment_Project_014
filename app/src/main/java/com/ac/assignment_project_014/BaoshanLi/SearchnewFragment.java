package com.ac.assignment_project_014.BaoshanLi;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.ac.assignment_project_014.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchnewFragment  extends Fragment {

    private  ArrayList<AlbumItem> alist;
    private String gArtistURL;
    private ListView listView;
    private Button btn_search;
    private EditText text_search;

    //SwipeRefreshLayout swipeRefresh;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.audio_fragment_searchnew,container,false);

        super.onCreate(savedInstanceState);

        listView = view.findViewById(R.id.list_album);
        text_search=view.findViewById(R.id.text_search);
        btn_search= view.findViewById(R.id.btn_search);
        gArtistURL="https://www.theaudiodb.com/api/v1/json/1/searchalbum.php?s="+text_search.getText();


        btn_search.setOnClickListener( click -> {
            gArtistURL="https://www.theaudiodb.com/api/v1/json/1/searchalbum.php?s="+text_search.getText();
            new fetchAlbum().execute();
        });


        alist=new ArrayList<AlbumItem>();


        //list.setTextFilterEnabled(true);

        //AudioAdapter arrayAdapter = new AudioAdapter(alist);
        //listView.setAdapter(arrayAdapter);
        new fetchAlbum().execute();

        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
//todo:

        //AlbumDetailFragment adFragment=new AlbumDetailFragment();
        //adFragment.refresh();

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
            new DownloadImageHelper(img_album).execute(item.getAlbumImgUrl());

            return newView;


        }


    }

    public class fetchAlbum extends AsyncTask<String, String, String> {

        @Override
        public void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String result = null;
            if(gArtistURL!=null ) {
                alist.clear();

                try {
                    URL url = new URL(gArtistURL);
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
                JSONArray array = object.getJSONArray("album");

                for (int i = 0; i < array.length(); i++) {

                    JSONObject jsonObject = array.getJSONObject(i);
                    String idAlbum = jsonObject.getString("idAlbum");
                    String strAlbum = jsonObject.getString("strAlbum");
                    String strArtist = jsonObject.getString("strArtist");
                    String strAlbumThumb = jsonObject.getString("strAlbumThumb");
                    String strStyle = jsonObject.getString("strStyle");
                    String intYearReleased = jsonObject.getString("intYearReleased");

                    //todo:
                    AlbumItem model = new AlbumItem(idAlbum,strAlbum,strArtist,strAlbumThumb,strStyle);
                    alist.add(model);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            AudioAdapter arrayAdapter = new AudioAdapter(alist);
            listView.setAdapter(arrayAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



                 /*   Bundle bundle = new Bundle();
                    bundle.putInt("photo", photo[arg2]);
                    bundle.putString("message", message[arg2]);
                    Intent intent = new Intent();
                    intent.putExtras(bundle);
                    intent.setClass(MainActivity.this, MoveList.class);
                    Log.i("message", message[arg2]);*/
                    //AudioIndexActivity.


                    //HashMap<String, String> map = (HashMap<String, String>) listView.getItemAtPosition(position);

                    //AudioIndexActivity activity = (AudioIndexActivity) getActivity();
                    ((AudioIndexActivity)getActivity()).showAlbumFragment(alist.get(position));

                }
            });



            //CustomAdapter adapter = new CustomAdapter(MainActivity.this, arrayList);
            //listView.setAdapter(adapter);

        }
    }






}