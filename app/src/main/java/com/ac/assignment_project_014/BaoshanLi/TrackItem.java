package com.ac.assignment_project_014.BaoshanLi;

public class TrackItem {

    private String albumId,trackId,trackName, artistName,trackGenre;


    public TrackItem(String albumId,String trackId,String trackName,String artistName,String trackGenre) {
        this.albumId=albumId;
        this.trackId=trackId;
        this.trackName=trackName;
        this.artistName=artistName;
        this.trackGenre=trackGenre;


    }


    public String getAlbumId(){
        return albumId;
    }
    public String getTrackId(){
        return trackId;
    }
    public String getTrackName(){
        return trackName;
    }
    public String getArtistName(){ return artistName; }
    public String getTrackGenre(){
        return trackGenre;
    }

}
