package com.ac.assignment_project_014.audio;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import com.ac.assignment_project_014.R;
/**
 * This Class is Main activity of Audio Activity.
 * @author Baoshan Li
 */
public class AudioIndexActivity extends AudioDrawerBase {
    /**
     * the search page button
     */
    private RadioButton searchRb;
    /**
     * the local audio button
     */
    private RadioButton localRb;
    /**
     * the album button
     */
    private RadioButton albumRb;
    /**
     * the song button
     */
    private RadioButton songRb;
    /**
     * the parent of RadioButtons
     */
    private RadioGroup mRadioGroup;
    /**
     * the search fragement page
     */
    private SearchnewFragment searchnewFragment;
    /**
     * the local Album List fragement page
     */
    private LocalAlbumListFragment localAlbumListFragment;
    /**
     * the local Album detail fragement page
     */
    private AlbumDetailFragment albumDetailFragment;
    /**
     * the local song detail fragement page
     */
    private SongDetailFragment songDetailFragment;
    private String albumId;


    /**
     * load the view xml when initializing this activity
     * @param savedInstanceState the current saved stated
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.audio_index;
    }

    /**
     * initial the view
     */
    private void initView() {
        mRadioGroup = findViewById(R.id.radio_group);
        mRadioGroup.setOnCheckedChangeListener(this::onCheckedChanged);
        searchRb = findViewById(R.id.rd_new);
        localRb = findViewById(R.id.rd_local);
        albumRb = findViewById(R.id.rd_album);
        songRb = findViewById(R.id.rd_song);
        searchRb.setChecked(true);
        Drawable search = ContextCompat.getDrawable(this, R.drawable.selector_audio_searchnew);
        search.setBounds(0, 0, 80, 80);
        searchRb.setCompoundDrawables(null, search, null, null);
        Drawable local = ContextCompat.getDrawable(this, R.drawable.selector_audio_local);
        local.setBounds(0, 0, 80, 80);
        localRb.setCompoundDrawables(null, local, null, null);
        Drawable album = ContextCompat.getDrawable(this, R.drawable.selector_audio_album);
        album.setBounds(0, 0, 80, 80);
        albumRb.setCompoundDrawables(null, album, null, null);
        Drawable song = ContextCompat.getDrawable(this, R.drawable.selector_audio_song);
        song.setBounds(0, 0, 80, 80);
        songRb.setCompoundDrawables(null, song, null, null);
    }

    /**
     * show album fragment
     */
    public void showAlbumFragment(AlbumItem currentAlbum ){
                mRadioGroup.check(R.id.rd_album);
                albumDetailFragment.setCurrentAlbum(currentAlbum);
    }
    /**
     * show Google search fragment
     */
    public void showGoogleFragment(TrackItem item){
        mRadioGroup.check(R.id.rd_song);
        songDetailFragment.setTrackId(item);
    }


    /**
     * switch  different fragments
     */
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideAllFragment(transaction);
        switch (checkedId) {
            case R.id.rd_new:
                if (searchnewFragment == null) {
                    searchnewFragment = new SearchnewFragment();
                    transaction.add(R.id.fragment_container, searchnewFragment);
                } else {
                    transaction.show(searchnewFragment);
                }
                break;
            case R.id.rd_local:
                if (localAlbumListFragment == null) {
                    localAlbumListFragment = new LocalAlbumListFragment();
                    transaction.add(R.id.fragment_container, localAlbumListFragment);
                } else {
                    transaction.show(localAlbumListFragment);
                }
                break;
            case R.id.rd_album:
                if (albumDetailFragment == null) {
                    albumDetailFragment = new AlbumDetailFragment();
                    //albumDetailFragment.setCurrentAlbum(currentAlbum);
                    transaction.add(R.id.fragment_container, albumDetailFragment);
                } else {
                    transaction.show(albumDetailFragment);
                }
                break;
            case R.id.rd_song:
                if (songDetailFragment == null) {
                    songDetailFragment = new SongDetailFragment();
                    transaction.add(R.id.fragment_container,  songDetailFragment);
                } else {
                    transaction.show( songDetailFragment);
                }
                break;
        }
        transaction.commit();
    }

    /**
     * hide all  fragments
     */
    public void hideAllFragment(FragmentTransaction transaction){
        if(searchnewFragment!=null){
            transaction.hide(searchnewFragment);
        }
        if(localAlbumListFragment!=null){
            transaction.hide(localAlbumListFragment);
        }
        if(albumDetailFragment!=null){
            transaction.hide(albumDetailFragment);
        }
        if(songDetailFragment!=null){
            transaction.hide(songDetailFragment);
        }
    }


}
