package com.ac.assignment_project_014.audio;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.fragment.app.FragmentActivity;


public class AudioOpener extends SQLiteOpenHelper {

    protected final static String DATABASE_NAME = "AudioDB_test";
    protected final static int VERSION_NUM = 1;
    public final static String TABLE_NAME_A = "ALBUM_TABLE";
    public final static String COL_ALBUMID = "_albumid";
    public final static String COL_ALBUMNAME = "albumName";
    public final static String COL_ARTIST = "artist";
    public final static String COL_ALBUMIMGURL = "imgURL";
    public final static String COL_ALBUMSTYLE = "Style";
    public final static String COL_YEARRELEASED = "yearReleased";



    public final static String TABLE_NAME_T = "TRACK_TABLE";
    public final static String COL_TRACKID = "_trackid";
    public final static String COL_TRACKNAME = "trackName";
    public final static String COL_TRACKGENRE = "trackGenre";





    public AudioOpener(FragmentActivity ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }



    //This function gets called if no database file exists.
    //Look on your device in the /data/data/package-name/database directory.
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL( "CREATE TABLE " + TABLE_NAME_A + " ( _albumId INTEGER 	PRIMARY KEY , " +
                COL_ALBUMNAME+" TEXT, " +
                COL_ARTIST+" TEXT , " +
                COL_ALBUMIMGURL+" TEXT , " +
                COL_ALBUMSTYLE+" TEXT , " +
                COL_YEARRELEASED+" TEXT );" );

     /*   db.execSQL( "CREATE TABLE " + TABLE_NAME_T + " ( _trackid INTEGER 	PRIMARY KEY , " +
                COL_ALBUMID+" INTEGER, " +
                COL_TRACKNAME+" TEXT, " +
                COL_ARTIST+" TEXT , " +
                COL_ALBUMNAME+" TEXT , " +
                COL_TRACKGENRE+" TEXT );" );*/

    }


    //this function gets called if the database version on your device is lower than VERSION_NUM
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {   //Drop the old table:
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME_A);
        //db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME_T);

        //Create the new table:
        onCreate(db);
    }

    //this function gets called if the database version on your device is higher than VERSION_NUM
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {   //Drop the old table:
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME_A);
        //db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME_T);

        //Create the new table:
        onCreate(db);
    }
}
