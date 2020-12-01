package com.ac.assignment_project_014.audio;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ac.assignment_project_014.R;
/**
 * This Class is the page to display the details of a song.
 * @author Baoshan Li
 */
public class SongDetailFragment  extends Fragment {
    /**
     * the current track Item
     */
    private TrackItem currentItem;
    /**
     * the current web view
     */
    private  WebView wv;
    /**
     * to get the current track id
     * @param currentItem the current track item object
     */
    public void setTrackId(TrackItem currentItem) {
        this.currentItem = currentItem;
    }

    /**
     * Called when initializing the fragment
     * @param inflater to load the view xml
     * @param container the parent view contains children views
     * @param savedInstanceState the current saved stated
     */
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.audio_fragment_song_detail,container,false);
        super.onCreate(savedInstanceState);

        wv= view.findViewById(R.id.webview_track);
       /* if(currentItem!=null) {
            String url = "http://www.google.com/search?q=" + currentItem.getArtistName() + "+" + currentItem.getTrackName();
            wv.setWebViewClient(new WebViewClient());
            wv.getSettings().setJavaScriptEnabled(true);
            wv.loadUrl(url);
        }*/
        return view;
    }

    /**
     * to control the visible of current fragment
     * @param hidden to indicate if the current fragement visible
     *
     */    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if(currentItem!=null) {
                //refresh whole page
                String url = "http://www.google.com/search?q="+currentItem.getArtistName()+"+"+currentItem.getTrackName();
                wv.setWebViewClient(new WebViewClient());
                wv.getSettings().setJavaScriptEnabled(true);
                wv.loadUrl(url);

            }

        }
    }
}



