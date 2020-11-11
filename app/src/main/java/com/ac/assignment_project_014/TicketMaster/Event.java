package com.ac.assignment_project_014.TicketMaster;

public class Event {

    public final static String TABLE_NAME_SEARCH_RESULT = "EVENT_LIST";
    public final static String TABLE_NAME_FAVORITE = "EVENT_FAV";

    public final static String COL_ID = "_id";
    public final static String COL_NAME = "NAME";
    public final static String COL_DATE = "DATE";
    public final static String COL_MINPRICE = "MINPRICE";
    public final static String COL_MAXPRICE = "MAXPRICE";
    public final static String COL_URL = "URL";
    public final static String COL_IMAGE = "IMAGE";

    /**
     * the id in db
     */
    private long id;

    /**
     * the name of the event
     */
    private String name;

    /**
     * the starting date of the event
     */
    private String date;

    /**
     * the min price of the event
     */
    private String minPrice;

    /**
     * the max price of the event
     */
    private String maxPrice;

    /**
     * the URL of the event
     */
    private String URL;

    /**
     * the promotion image of the event
     */
    private String image;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(String minPrice) {
        this.minPrice = minPrice;
    }

    public String getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
