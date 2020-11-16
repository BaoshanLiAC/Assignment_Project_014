package com.ac.assignment_project_014.audio;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import com.ac.assignment_project_014.R;
import com.ac.assignment_project_014.recipe.RecipeDrawerBase;

public class AudioIndexActivity extends AudioDrawerBase {

    private RadioButton searchRb, localRb, albumRb, songRb;
    private RadioGroup mRadioGroup;
    private SearchnewFragment searchnewFragment;
    private LocalAlbumListFragment localAlbumListFragment;
    private  AlbumDetailFragment albumDetailFragment;
    private SongDetailFragment songDetailFragment;
    private String albumId;
    //private AlbumItem currentAlbum;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio_index);
        initView();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.recipe_activity_main;
    }

    private void initView() {
        mRadioGroup = findViewById(R.id.radio_group);
        mRadioGroup.setOnCheckedChangeListener(this::onCheckedChanged);
        searchRb = findViewById(R.id.rd_new);
        localRb = findViewById(R.id.rd_local);
        albumRb = findViewById(R.id.rd_album);
        songRb = findViewById(R.id.rd_song);
        searchRb.setChecked(true);

        /**图片的优化，其他三个图片做类似处理
         * 底部导航的时候会发生图片的颜色变化，所以radiobutton中的照片不是一张，而是引用了自定义的选择器照片
         * 本来使用的是getResources.getDrawable,不过已经过时，所以使用ContextCompat
         */
        Drawable search = ContextCompat.getDrawable(this, R.drawable.selector_audio_searchnew);
        /**
         *  当这个图片被绘制时，给他绑定一个矩形规定这个矩形
         *  参数前两个对应图片相对于左上角的新位置，后两个为图片的长宽
         */
        search.setBounds(0, 0, 80, 80);
        /**
         *   设置图片在文字的哪个方向,分别对应左，上，右，下
         */

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


    public void showAlbumFragment(AlbumItem currentAlbum ){
                mRadioGroup.check(R.id.rd_album);
                albumDetailFragment.setCurrentAlbum(currentAlbum);
    }

    public void showGoogleFragment(TrackItem item){
        mRadioGroup.check(R.id.rd_song);
        songDetailFragment.setTrackId(item);
    }





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
