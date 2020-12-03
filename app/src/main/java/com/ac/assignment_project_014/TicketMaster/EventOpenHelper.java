package com.ac.assignment_project_014.TicketMaster;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * This Class is a database manipulation helper.
 *  <p>
 *  <o>create a database table if not exist</>
 *  <o>drop a old version table, and recreate a new table, the data in the table will lost also</>
 *  <o>this function gets called if the database version on your device is higher than VERSION_NUM</>
 */

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
        db.execSQL("CREATE TABLE " + TicketMasterMainActivity.EVENT_SAVED_TABLE + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TicketMasterMainActivity.COL_NAME + " text,"
                + TicketMasterMainActivity.COL_DATE  + " text,"
                + TicketMasterMainActivity.COL_MINPRICE  + " text,"
                + TicketMasterMainActivity.COL_MAXPRICE  + " text,"
                + TicketMasterMainActivity.COL_URL  + " text,"
                + TicketMasterMainActivity.COL_IMAGE  + " text);");

        db.execSQL("CREATE TABLE " + TicketMasterMainActivity.SEARCH_RESULT_TABLE + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TicketMasterMainActivity.COL_NAME + " text,"
                + TicketMasterMainActivity.COL_DATE  + " text,"
                + TicketMasterMainActivity.COL_MINPRICE  + " text,"
                + TicketMasterMainActivity.COL_MAXPRICE  + " text,"
                + TicketMasterMainActivity.COL_URL  + " text,"
                + TicketMasterMainActivity.COL_IMAGE  + " text);");
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
        db.execSQL( "DROP TABLE IF EXISTS " + TicketMasterMainActivity.SEARCH_RESULT_TABLE);
        db.execSQL( "DROP TABLE IF EXISTS " + TicketMasterMainActivity.EVENT_SAVED_TABLE);

        //Create the new table:
        onCreate(db);
    }


}
