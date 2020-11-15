package com.ac.assignment_project_014.BaoshanLi;

        import android.os.Bundle;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.webkit.WebView;
        import android.webkit.WebViewClient;

        import androidx.annotation.Nullable;
        import androidx.fragment.app.Fragment;
        import com.ac.assignment_project_014.R;

public class SongDetailFragment  extends Fragment {

    private TrackItem currentItem;
    private  WebView wv;

    public void setTrackId(TrackItem currentItem) {
        this.currentItem = currentItem;
    }

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


    @Override
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



