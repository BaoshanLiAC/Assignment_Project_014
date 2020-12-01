package com.ac.assignment_project_014.audio;
/**
 * The entity class of TrackItem
 *
 * @author Baoshan Li
 */
public class TrackItem {
    /**
     * id of the AlbumItem
     */
    private String albumId;
    /**
     * id of the track
     */
    private String trackId;
    /**
     * name of the track
     */
    private String trackName;
    /**
     * artist Name
     */
    private String artistName;
    /**
     * track Genre
     */
    private String trackGenre;

    /**
     * the constructor of TrackItem
     */
    public TrackItem(String albumId,String trackId,String trackName,String artistName,String trackGenre) {

        this.albumId=albumId;
        this.trackId=trackId;
        this.trackName=trackName;
        this.artistName=artistName;
        this.trackGenre=trackGenre;


    }


    /**
     * get the Track name
     * @return the name of the Track
     */
    public String getTrackName(){
        return trackName;
    }
    /**
     * get the Artist name
     * @return the name of the Artist
     */
    public String getArtistName(){ return artistName; }
    /**
     * get thetrack Genre
     * @return the track Genre
     */
    public String getTrackGenre(){
        return trackGenre;
    }

}
