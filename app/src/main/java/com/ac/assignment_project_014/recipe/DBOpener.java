package com.ac.assignment_project_014.recipe;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBOpener extends SQLiteOpenHelper {

    protected final static String DATABASE_NAME = "ChatDB2";
    protected final static int VERSION_NUM = 1;
    public final static String TABLE_NAME = "RECIPE";
    public final static String COL_TITLE = "TITLE";
    public final static String COL_HREF = "HREF";
    public final static String COL_INGREDIENT = "INGREDIENT";
    public final static String COL_THUMBNAIL = "THUMBNAIL";
    public final static String COL_ID = "_id";


    public DBOpener(Context ctx)
    {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }


    //This function gets called if no database file exists.
    //Look on your device in the /data/data/package-name/database directory.
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL( "CREATE TABLE " + TABLE_NAME + " ( _id INTEGER 	PRIMARY KEY AUTOINCREMENT, " +
                COL_TITLE+" TEXT, " +
                COL_HREF+" TEXT , " +
                COL_INGREDIENT +" TEXT , " +
                COL_THUMBNAIL+" TEXT );" );
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {   //Drop the old table:
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create the new table:
        onCreate(db);
    }

    //this function gets called if the database version on your device is higher than VERSION_NUM
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {   //Drop the old table:
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create the new table:
        onCreate(db);
    }
}
