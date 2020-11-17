package com.ac.assignment_project_014.recipe;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * This Class is a database manipulation helper.
 * <p>
 * <o>create a database table if not exist</>
 * <o>drop a old version table, and recreate a new table, the data in the table will lost also</>
 * <o>this function gets called if the database version on your device is higher than VERSION_NUM</>
 */
public class DBOpener extends SQLiteOpenHelper {

    protected final static String DATABASE_NAME = "ChatDB2";
    protected final static int VERSION_NUM = 5;
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

    /**
     * This function gets called if no database file exists, will create a table by a predefined SQL.
     * The database is located in your device in the /data/data/package-name/database directory.
     * @param db an database manipulation object
     *
     */
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
    {   
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
