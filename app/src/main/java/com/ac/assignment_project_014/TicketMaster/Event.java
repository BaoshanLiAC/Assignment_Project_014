package com.ac.assignment_project_014.TicketMaster;

public class Event {

    /**
     * table name of the search result
     */
    public final static String TABLE_NAME_SEARCH_RESULT = "TICKET_MASTER_EVENT_LIST";

    /**
     * table name of the saved events
     */
    public final static String TABLE_NAME_SAVED = "EVENT_SAVED";

    /**
     * column: id of the event
     */
    public final static String COL_ID = "_id";

    /**
     * column: name of the event
     */
    public final static String COL_NAME = "NAME";

    /**
     * column: date of the event
     */
    public final static String COL_DATE = "DATE";

    /**
     * column: minimum price of the event
     */
    public final static String COL_MINPRICE = "MINPRICE";
    /**
     * column: max price of the event
     */
    public final static String COL_MAXPRICE = "MAXPRICE";

    /**
     * column: url of the event
     */
    public final static String COL_URL = "URL";

    /**
     * column: image of the event
     */
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

    /**
     * getter of id in database
     * @return id
     */
    public long getId() {
        return id;
    }

    /**
     * setter of id
     * @param id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * getter of event name
     * @return event name
     */
    public String getName() {
        return name;
    }

    /**
     * setter of event name
     * @param name event name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * getter of event date
     * @return event date
     */
    public String getDate() {
        return date;
    }

    /**
     * setter of event date
     * @param date event date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * getter of min price of the event
     * @return min price of the event
     */
    public String getMinPrice() {
        return minPrice;
    }

    /**
     * setter of the min price of the event
     * @param minPrice min price of the event
     */
    public void setMinPrice(String minPrice) {
        this.minPrice = minPrice;
    }

    /**
     * getter of the max price of the event
     * @return max price of the event
     */
    public String getMaxPrice() {
        return maxPrice;
    }

    /**
     * setter of the max price
     * @param maxPrice max price of the event
     */
    public void setMaxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
    }

    /**
     * getter of url
     * @return url event
     */
    public String getURL() {
        return URL;
    }

    /**
     * setter of url
     * @param URL rul of the event
     */
    public void setURL(String URL) {
        this.URL = URL;
    }

    /**
     * getter of the image
     * @return image of the event
     */
    public String getImage() {
        return image;
    }

    /**
     * setter of the image
     * @param image image of the event
     */
    public void setImage(String image) {
        this.image = image;
    }
}
