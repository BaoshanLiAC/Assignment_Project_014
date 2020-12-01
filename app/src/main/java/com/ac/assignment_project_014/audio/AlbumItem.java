package com.ac.assignment_project_014.audio;
/**
 * The entity class of AlbumItem
 *
 * @author Baoshan Li
 */
public class AlbumItem {
    /**
     * id of the AlbumItem
     */
    private String albumId;
    /**
     * name of the AlbumItem
     */
    private String albumName;
    /**
     * artist name of the AlbumItem
     */
    private String artistName;
    /**
     * album image url of the AlbumItem
     */
    private String albumImgUrl;
    /**
     * album image style
     */
    private String albumStyle;
    /**
     * the constructor of AlbumItem
     */
    public AlbumItem(String albumId,String albumName,String artistName,String albumImgUrl,String albumStyle) {
        this.albumId=albumId;
        this.albumName=albumName;
        this.artistName=artistName;
        this.albumImgUrl=albumImgUrl;
        this.albumStyle=albumStyle;
    }

    /**
     * get the Album Id
     * @return the id of the Album
     */
    public String getAlbumId(){
        return albumId;
    }
    /**
     * get the Album name
     * @return the name of the Album
     */
    public String getAlbumName(){
        return albumName;
    }
    /**
     * get the Artist name
     * @return the name of the Artist
     */
    public String getArtistName(){ return artistName; }
    /**
     * get the Album image url
     * @return the image url of the Album
     */
    public String getAlbumImgUrl(){
        return albumImgUrl;
    }
    /**
     * get the Album Style
     * @return the Style of the Album
     */
    public String getAlbumStyle(){return albumStyle;}

}
