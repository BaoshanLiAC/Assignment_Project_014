package com.ac.assignment_project_014.audio;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.fragment.app.FragmentActivity;

/**
 * This Class is a database manipulation helper.
 * <p>
 * <o>create a database table if not exist</>
 * <o>drop a old version table, and recreate a new table, the data in the table will lost also</>
 * <o>this function gets called if the database version on your device is higher than VERSION_NUM</>
 *
 * @author Baoshan Li
 */
public class AudioOpener extends SQLiteOpenHelper {
    /**
     * name of the database
     */
    protected final static String DATABASE_NAME = "AudioDB_test";
    /**
     * version of database
     */
    protected final static int VERSION_NUM = 1;
    /**
     * table name of database
     */
    public final static String TABLE_NAME_A = "ALBUM_TABLE";
    /**
     * column name of database
     */
    public final static String COL_ALBUMID = "_albumid";
    /**
     * column name of database
     */
    public final static String COL_ALBUMNAME = "albumName";
    /**
     * column name of database
     */
    public final static String COL_ARTIST = "artist";
    /**
     * column name of database
     */
    public final static String COL_ALBUMIMGURL = "imgURL";
    /**
     * column name of database
     */
    public final static String COL_ALBUMSTYLE = "Style";
    /**
     * column name of database
     */
    public final static String COL_YEARRELEASED = "yearReleased";

    /**
     * the constructor of DBOpener, used to initial the database
     * @param ctx context
     */
    public AudioOpener(FragmentActivity ctx) {
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
        db.execSQL( "CREATE TABLE " + TABLE_NAME_A + " ( _albumId INTEGER 	PRIMARY KEY , " +
                COL_ALBUMNAME+" TEXT, " +
                COL_ARTIST+" TEXT , " +
                COL_ALBUMIMGURL+" TEXT , " +
                COL_ALBUMSTYLE+" TEXT , " +
                COL_YEARRELEASED+" TEXT );" );

    }


    /**
     * this method is called when the database is updated
     * @param db database
     * @param oldVersion old version of the database
     * @param newVersion new version of the database
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {   //Drop the old table:
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME_A);

        onCreate(db);
    }

    /**
     * this method is called when the database is downgrade
     * @param db database
     * @param oldVersion old version of the database
     * @param newVersion new version of the database
     */    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {   //Drop the old table:
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME_A);
        //db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME_T);

        //Create the new table:
        onCreate(db);
    }
}
