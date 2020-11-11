package com.ac.assignment_project_014.ticketmaster;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class EventOpenHelper extends SQLiteOpenHelper {
    protected final static String DATABASE_NAME = "EventDB";
    protected final static int VERSION_NUM = 1;
    protected final static String TABLE_NAME = "EVENTS";
    protected final static String COL_NAME = "NAME";
    protected final static String COL_DATE = "DATE";
    protected final static String COL_MINPRICE = "MINPRICE";
    protected final static String COL_MAXPRICE = "MAXPRICE";
    protected final static String COL_URL = "URL";
    protected final static String COL_IMAGE = "IMAGE";
    protected final static String COL_ID = "_id";

    public EventOpenHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_NAME + " text,"
                + COL_DATE  + " text,"
                + COL_MINPRICE  + " text,"
                + COL_MAXPRICE  + " text,"
                + COL_URL  + " text,"
                + COL_IMAGE  + " text);");    //add or remove columns
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop the old table:
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create the new table:
        onCreate(db);
    }


    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {   //Drop the old table:
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create the new table:
        onCreate(db);
    }
}
