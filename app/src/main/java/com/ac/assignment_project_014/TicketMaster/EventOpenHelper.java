package com.ac.assignment_project_014.TicketMaster;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class EventOpenHelper extends SQLiteOpenHelper {
    protected final static String DATABASE_NAME = "EventDB";
    protected final static int VERSION_NUM = 1;


    public EventOpenHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Event.TABLE_NAME_FAVORITE + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
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

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop the old table:
        db.execSQL( "DROP TABLE IF EXISTS " + Event.TABLE_NAME_SEARCH_RESULT);
        db.execSQL( "DROP TABLE IF EXISTS " + Event.TABLE_NAME_FAVORITE);

        //Create the new table:
        onCreate(db);
    }


}
