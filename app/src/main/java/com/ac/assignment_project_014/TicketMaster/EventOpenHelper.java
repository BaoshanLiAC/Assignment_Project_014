package com.ac.assignment_project_014.TicketMaster;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class EventOpenHelper extends SQLiteOpenHelper {

    /**
     * name of the database
     */
    protected final static String DATABASE_NAME = "EventDB";

    /**
     * version of database
     */
    protected final static int VERSION_NUM = 1;


    /**
     * constructor
     * @param ctx context
     */
    public EventOpenHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    /**
     * this method is called when the database created
     * @param db database
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Event.TABLE_NAME_SAVED + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Event.COL_NAME + " text,"
                + Event.COL_DATE  + " text,"
                + Event.COL_MINPRICE  + " text,"
                + Event.COL_MAXPRICE  + " text,"
                + Event.COL_URL  + " text,"
                + Event.COL_IMAGE  + " text);");

        db.execSQL("CREATE TABLE " + Event.TABLE_NAME_SEARCH_RESULT + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Event.COL_NAME + " text,"
                + Event.COL_DATE  + " text,"
                + Event.COL_MINPRICE  + " text,"
                + Event.COL_MAXPRICE  + " text,"
                + Event.COL_URL  + " text,"
                + Event.COL_IMAGE  + " text);");
    }

    /**
     * this method is called when the database is updated
     * @param db database
     * @param oldVersion old version of the database
     * @param newVersion new version of the database
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop the old table:
        db.execSQL( "DROP TABLE IF EXISTS " + Event.TABLE_NAME_SEARCH_RESULT);
        db.execSQL( "DROP TABLE IF EXISTS " + Event.TABLE_NAME_SAVED);

        //Create the new table:
        onCreate(db);
    }


}
