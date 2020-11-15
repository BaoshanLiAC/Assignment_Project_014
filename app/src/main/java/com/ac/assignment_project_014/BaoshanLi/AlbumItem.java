package com.ac.assignment_project_014.BaoshanLi;

public class AlbumItem {

    private String albumId,albumName, artistName,albumImgUrl,albumStyle;


    public AlbumItem(String albumId,String albumName,String artistName,String albumImgUrl,String albumStyle) {
        this.albumId=albumId;
        this.albumName=albumName;
        this.artistName=artistName;
        this.albumImgUrl=albumImgUrl;
        this.albumStyle=albumStyle;
    }


    public String getAlbumId(){
        return albumId;
    }
    public String getAlbumName(){
        return albumName;
    }
    public String getArtistName(){ return artistName; }
    public String getAlbumImgUrl(){
        return albumImgUrl;
    }
    public String getAlbumStyle(){return albumStyle;}

}
